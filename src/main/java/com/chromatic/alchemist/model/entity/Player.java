package com.chromatic.alchemist.model.entity;

import com.chromatic.alchemist.logging.GameLogger;
import com.chromatic.alchemist.model.decorator.*;
import com.chromatic.alchemist.model.observer.GameEvent;
import com.chromatic.alchemist.model.observer.GameEventManager;
import com.chromatic.alchemist.model.state.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Player Entity Class
 * 
 * Represents the player's Alchemist Orb in the game.
 * Integrates with:
 * - State Pattern: Elemental states (Fire, Water, Earth, Air)
 * - Decorator Pattern: Ability enhancements (speed, shield, magnet, etc.)
 * - Observer Pattern: Notifies game systems of player events
 */

public class Player {
    
    private final String id;
    private final String name;
    
    // Position and movement
    private double x;
    private double y;
    private double velocityX;
    private double velocityY;
    private double radius;
    
    // Movement input tracking
    private boolean movingUp;
    private boolean movingDown;
    private boolean movingLeft;
    private boolean movingRight;
    
    // State Pattern - Current elemental state
    private ElementalState currentState;
    private String previousStateName;
    
    // Decorator Pattern - Current abilities
    private PlayerAbility abilities;
    private List<AbilityDecorator> activeDecorators;
    
    // Player stats
    private int health;
    private int maxHealth;
    private int score;
    private int essencesCollected;
    
    // Temporary effects
    private double temporarySpeedBoost;
    private double temporarySpeedDuration;
    private boolean phasing;
    private double phasingDuration;
    private boolean shielded;
    private double shieldDuration;
    private boolean dashing;
    private double dashDistance;
    private double dashDirectionX;
    private double dashDirectionY;
    
    // Invincibility after taking damage
    private boolean invincible;
    private double invincibilityDuration;
    private static final double INVINCIBILITY_TIME = 1.5; // seconds
    
    // Animation
    private double pulsePhase;
    private double trailPhase;
    

    public Player(double x, double y) {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.name = "Alchemist";
        this.x = x;
        this.y = y;
        this.velocityX = 0;
        this.velocityY = 0;
        this.radius = 20.0;
        
        // Initialize movement
        this.movingUp = false;
        this.movingDown = false;
        this.movingLeft = false;
        this.movingRight = false;
        
        // Initialize state (start with Fire)
        this.previousStateName = "NONE";
        this.currentState = new FireState();
        
        // Initialize abilities (base abilities, no decorators)
        this.abilities = new BasePlayerAbility();
        this.activeDecorators = new ArrayList<>();
        
        // Initialize stats
        this.maxHealth = 100;
        this.health = maxHealth;
        this.score = 0;
        this.essencesCollected = 0;
        
        // Initialize temporary effects
        this.temporarySpeedBoost = 1.0;
        this.temporarySpeedDuration = 0;
        this.phasing = false;
        this.phasingDuration = 0;
        this.shielded = false;
        this.shieldDuration = 0;
        this.dashing = false;
        this.dashDistance = 0;
        
        // Initialize invincibility
        this.invincible = false;
        this.invincibilityDuration = 0;
        
        // Initialize animation
        this.pulsePhase = 0;
        this.trailPhase = 0;
        
        GameLogger.getInstance().logEntityCreated("Player", id, x, y);
        GameLogger.getInstance().logPlayerStateChange("NONE", currentState.getStateName());
    }
    
    public void update(double deltaTime) {
        // Update elemental state
        currentState.update(this, deltaTime);
        
        // Update temporary effects
        updateTemporaryEffects(deltaTime);
        
        // Update decorators and remove expired ones
        updateDecorators(deltaTime);
        
        // Update movement
        updateMovement(deltaTime);
        
        // Update animation
        pulsePhase += deltaTime * 4.0;
        trailPhase += deltaTime * 2.0;
        
        // Update invincibility
        if (invincible) {
            invincibilityDuration -= deltaTime;
            if (invincibilityDuration <= 0) {
                invincible = false;
                GameLogger.getInstance().logGameEvent("Player", "Invincibility ended");
            }
        }
    }
    
    private void updateTemporaryEffects(double deltaTime) {
        // Temporary speed boost
        if (temporarySpeedDuration > 0) {
            temporarySpeedDuration -= deltaTime;
            if (temporarySpeedDuration <= 0) {
                temporarySpeedBoost = 1.0;
                GameLogger.getInstance().logGameEvent("Player", "Temporary speed boost ended");
            }
        }
        
        // Phasing
        if (phasing) {
            phasingDuration -= deltaTime;
            if (phasingDuration <= 0) {
                phasing = false;
                GameLogger.getInstance().logGameEvent("Player", "Phasing ended");
            }
        }
        
        // Shield
        if (shielded) {
            shieldDuration -= deltaTime;
            if (shieldDuration <= 0) {
                shielded = false;
                GameLogger.getInstance().logGameEvent("Player", "Shield ended");
            }
        }
        
        // Dash
        if (dashing) {
            double dashStep = 800.0 * deltaTime; // Dash speed
            if (dashDistance > 0) {
                x += dashDirectionX * dashStep;
                y += dashDirectionY * dashStep;
                dashDistance -= dashStep;
            } else {
                dashing = false;
                GameLogger.getInstance().logGameEvent("Player", "Dash ended");
            }
        }
    }
    
    private void updateDecorators(double deltaTime) {
        List<AbilityDecorator> expiredDecorators = new ArrayList<>();
        
        for (AbilityDecorator decorator : activeDecorators) {
            if (!decorator.updateDuration(deltaTime)) {
                expiredDecorators.add(decorator);
            }
        }
        
        // Remove expired decorators
        for (AbilityDecorator decorator : expiredDecorators) {
            removeDecorator(decorator);
        }
    }
    
    private void updateMovement(double deltaTime) {
        if (dashing) {
            return; 
        }
        
        // Calculate target velocity based on input
        double targetVX = 0;
        double targetVY = 0;
        
        if (movingUp) targetVY -= 1;
        if (movingDown) targetVY += 1;
        if (movingLeft) targetVX -= 1;
        if (movingRight) targetVX += 1;
        
        // Normalize diagonal movement
        double magnitude = Math.sqrt(targetVX * targetVX + targetVY * targetVY);
        if (magnitude > 0) {
            targetVX /= magnitude;
            targetVY /= magnitude;
        }
        
        // Calculate effective speed
        double effectiveSpeed = getEffectiveSpeed();
        
        // Apply velocity
        velocityX = targetVX * effectiveSpeed;
        velocityY = targetVY * effectiveSpeed;
        
        // Update position
        x += velocityX * deltaTime;
        y += velocityY * deltaTime;
    }
    
    public double getEffectiveSpeed() {
        double baseSpeed = abilities.getSpeed();
        double stateModifier = currentState.getSpeedModifier();
        double tempModifier = temporarySpeedBoost;
        
        return baseSpeed * stateModifier * tempModifier;
    }
    
    // ==================== STATE PATTERN METHODS ====================
    
    public void changeState(ElementalState newState) {
        if (currentState != null) {
            previousStateName = currentState.getStateName();
            currentState.onExit(this);
        }
        
        currentState = newState;
        currentState.onEnter(this);
        
        // Fire event
        GameEventManager.getInstance().fireEvent(
            new GameEvent(GameEvent.EventType.PLAYER_TRANSMUTED)
                .addData("oldElement", previousStateName)
                .addData("newElement", currentState.getStateName())
                .addData("playerId", id)
        );
    }
    
    public void transmuteToFire() {
        if (!(currentState instanceof FireState)) {
            changeState(new FireState());
        }
    }
    
    public void transmuteToWater() {
        if (!(currentState instanceof WaterState)) {
            changeState(new WaterState());
        }
    }
    
    public void transmuteToEarth() {
        if (!(currentState instanceof EarthState)) {
            changeState(new EarthState());
        }
    }
    
    public void transmuteToAir() {
        if (!(currentState instanceof AirState)) {
            changeState(new AirState());
        }
    }
    
    public void useSpecialAbility() {
        currentState.useSpecialAbility(this);
    }
    
    public boolean canAbsorb(String essenceColor) {
        return currentState.canAbsorb(essenceColor);
    }
    
    // ==================== DECORATOR PATTERN METHODS ====================
    
    public void applyDecorator(AbilityDecorator decorator) {
        abilities = decorator;
        activeDecorators.add(decorator);
        
        GameEventManager.getInstance().fireEvent(
            new GameEvent(GameEvent.EventType.DECORATOR_APPLIED)
                .addData("decorator", decorator.getDecoratorName())
                .addData("target", "Player")
                .addData("duration", decorator.getDuration())
        );
    }
    
    public void removeDecorator(AbilityDecorator decorator) {
        activeDecorators.remove(decorator);
        
        rebuildAbilityChain();
        
        GameEventManager.getInstance().fireEvent(
            new GameEvent(GameEvent.EventType.DECORATOR_REMOVED)
                .addData("decorator", decorator.getDecoratorName())
                .addData("target", "Player")
        );
        
        GameLogger.getInstance().logDecoratorRemoved(decorator.getDecoratorName(), "Player");
    }
    
    private void rebuildAbilityChain() {
        abilities = new BasePlayerAbility();
        for (AbilityDecorator decorator : activeDecorators) {
            // Rewrap with new base
            switch (decorator.getDecoratorName()) {
                case "SpeedBoost":
                    abilities = new SpeedBoostDecorator(abilities, decorator.getDuration());
                    break;
                case "Shield":
                    abilities = new ShieldDecorator(abilities, decorator.getDuration());
                    break;
                case "Magnet":
                    abilities = new MagnetDecorator(abilities, decorator.getDuration());
                    break;
                case "MultiAbsorb":
                    abilities = new MultiAbsorbDecorator(abilities, decorator.getDuration());
                    break;
                case "ScoreMultiplier":
                    abilities = new ScoreMultiplierDecorator(abilities, decorator.getDuration());
                    break;
                case "RangeBoost":
                    abilities = new RangeBoostDecorator(abilities, decorator.getDuration());
                    break;
            }
        }
    }
    
    public void applySpeedBoost(double duration) {
        applyDecorator(new SpeedBoostDecorator(abilities, duration));
    }
    
    public void applyShieldDecorator(double duration) {
        applyDecorator(new ShieldDecorator(abilities, duration));
    }
    
    public void applyMagnet(double duration) {
        applyDecorator(new MagnetDecorator(abilities, duration));
    }
    
    public void applyMultiAbsorb(double duration) {
        applyDecorator(new MultiAbsorbDecorator(abilities, duration));
    }
    
    public void applyScoreMultiplier(double duration) {
        applyDecorator(new ScoreMultiplierDecorator(abilities, duration));
    }
    
    public void applyRangeBoost(double duration) {
        applyDecorator(new RangeBoostDecorator(abilities, duration));
    }
    
    public String[] getActiveDecoratorNames() {
        return activeDecorators.stream()
            .map(AbilityDecorator::getDecoratorName)
            .toArray(String[]::new);
    }
    
    // ==================== SPECIAL ABILITY EFFECT METHODS ====================
    
    public void applyTemporarySpeedBoost(double multiplier, double duration) {
        this.temporarySpeedBoost = multiplier;
        this.temporarySpeedDuration = duration;
        GameLogger.getInstance().logGameEvent("Player", 
            String.format("Temporary speed boost x%.1f for %.1fs", multiplier, duration));
    }
    
    public void activatePhasing(double duration) {
        this.phasing = true;
        this.phasingDuration = duration;
        GameLogger.getInstance().logGameEvent("Player", 
            String.format("Phasing activated for %.1fs", duration));
    }
    
    public void activateShield(double duration) {
        this.shielded = true;
        this.shieldDuration = duration;
        GameLogger.getInstance().logGameEvent("Player", 
            String.format("Shield activated for %.1fs", duration));
    }
    
    public void performDash(double distance) {
        // Determine dash direction from current velocity or facing direction
        double dirX = velocityX;
        double dirY = velocityY;
        
        double magnitude = Math.sqrt(dirX * dirX + dirY * dirY);
        if (magnitude > 0) {
            dashDirectionX = dirX / magnitude;
            dashDirectionY = dirY / magnitude;
        } else {
            // Default to right if not moving
            dashDirectionX = 1;
            dashDirectionY = 0;
        }
        
        this.dashing = true;
        this.dashDistance = distance;
        GameLogger.getInstance().logGameEvent("Player", 
            String.format("Dashing %.0f pixels", distance));
    }
    
    // ==================== DAMAGE AND HEALTH ====================
    
    public void takeDamage(int damage) {
        if (invincible || shielded) {
            GameLogger.getInstance().logGameEvent("Player", 
                "Damage blocked (invincible or shielded)");
            return;
        }
        
        // Apply shield reduction from decorators
        double shieldReduction = abilities.getShieldValue() / 100.0;
        int actualDamage = (int) (damage * (1.0 - shieldReduction));
        
        int oldHealth = health;
        health = Math.max(0, health - actualDamage);
        
        GameLogger.getInstance().logHealthUpdate(oldHealth, health);
        
        GameEventManager.getInstance().fireEvent(
            new GameEvent(GameEvent.EventType.PLAYER_DAMAGED)
                .addData("damage", actualDamage)
                .addData("oldHealth", oldHealth)
                .addData("newHealth", health)
        );
        
        invincible = true;
        invincibilityDuration = INVINCIBILITY_TIME;
        
        // Check for death
        if (health <= 0) {
            GameEventManager.getInstance().fireEvent(
                new GameEvent(GameEvent.EventType.PLAYER_DIED)
                    .addData("playerId", id)
                    .addData("finalScore", score)
            );
        }
    }
    
    public void heal(int amount) {
        int oldHealth = health;
        health = Math.min(maxHealth, health + amount);
        
        GameLogger.getInstance().logHealthUpdate(oldHealth, health);
        
        GameEventManager.getInstance().fireEvent(
            new GameEvent(GameEvent.EventType.PLAYER_HEALED)
                .addData("amount", amount)
                .addData("oldHealth", oldHealth)
                .addData("newHealth", health)
        );
    }
    
    // ==================== SCORE ====================
    
    public void addScore(int points, String reason) {
        int oldScore = score;
        int actualPoints = (int) (points * abilities.getScoreMultiplier());
        score += actualPoints;
        
        GameLogger.getInstance().logScoreUpdate(oldScore, score, reason);
        
        GameEventManager.getInstance().fireEvent(
            new GameEvent(GameEvent.EventType.SCORE_CHANGED)
                .addData("oldScore", oldScore)
                .addData("newScore", score)
                .addData("points", actualPoints)
                .addData("reason", reason)
        );
    }
    
    public void incrementEssencesCollected() {
        essencesCollected++;
    }
    
    // ==================== MOVEMENT INPUT ====================
    
    public void setMovingUp(boolean moving) { this.movingUp = moving; }
    public void setMovingDown(boolean moving) { this.movingDown = moving; }
    public void setMovingLeft(boolean moving) { this.movingLeft = moving; }
    public void setMovingRight(boolean moving) { this.movingRight = moving; }
    
    public boolean isMovingUp() { return movingUp; }
    public boolean isMovingDown() { return movingDown; }
    public boolean isMovingLeft() { return movingLeft; }
    public boolean isMovingRight() { return movingRight; }
    
    public boolean isMoving() {
        return movingUp || movingDown || movingLeft || movingRight;
    }
    
    // ==================== BOUNDARY CONSTRAINTS ====================
    
    public void constrainToBounds(double minX, double minY, double maxX, double maxY) {
        x = Math.max(minX + radius, Math.min(maxX - radius, x));
        y = Math.max(minY + radius, Math.min(maxY - radius, y));
    }
    
    // ==================== GETTERS ====================
    
    public String getId() { return id; }
    public String getName() { return name; }
    public double getX() { return x; }
    public double getY() { return y; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public double getVelocityX() { return velocityX; }
    public double getVelocityY() { return velocityY; }
    public double getRadius() { return radius; }
    
    public ElementalState getCurrentState() { return currentState; }
    public String getStateName() { return currentState.getStateName(); }
    public String getPreviousStateName() { return previousStateName; }
    public String getStateColor() { return currentState.getStateColor(); }
    public String getGlowColor() { return currentState.getGlowColor(); }
    
    public PlayerAbility getAbilities() { return abilities; }
    public List<AbilityDecorator> getActiveDecorators() { return new ArrayList<>(activeDecorators); }
    public double getAbsorptionRange() { return abilities.getAbsorptionRange(); }
    public double getMagnetStrength() { return abilities.getMagnetStrength(); }
    public boolean hasMultiAbsorb() { return abilities.hasMultiAbsorb(); }
    
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getScore() { return score; }
    public int getEssencesCollected() { return essencesCollected; }
    
    public boolean isPhasing() { return phasing; }
    public boolean isShielded() { return shielded; }
    public boolean isDashing() { return dashing; }
    public boolean isInvincible() { return invincible; }
    public boolean isAlive() { return health > 0; }
    
    public double getPulsePhase() { return pulsePhase; }
    public double getTrailPhase() { return trailPhase; }
    
    public double getVisualRadius() {
        return radius + Math.sin(pulsePhase) * 3.0;
    }
}
