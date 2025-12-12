package com.chromatic.alchemist.model.state;

import com.chromatic.alchemist.logging.GameLogger;
import com.chromatic.alchemist.model.entity.Player;

/**
 * STATE PATTERN - Concrete State: Fire Element
 * 
 * Fire state characteristics:
 * - Can absorb RED and ORANGE essence particles
 * - Fast movement speed (1.2x modifier)
 * - Special ability: Burst - temporary speed boost
 * - Visual: Orange-red appearance with flame glow
 */
public class FireState implements ElementalState {
    
    private static final String STATE_NAME = "FIRE";
    private static final String[] COMPATIBLE_COLORS = {"RED", "ORANGE"};
    private static final String STATE_COLOR = "#FF4500"; // OrangeRed
    private static final String GLOW_COLOR = "#FF6347"; // Tomato
    private static final double SPEED_MODIFIER = 1.2;
    
    // Ability cooldown tracking
    private double abilityCooldown = 0;
    private static final double ABILITY_COOLDOWN_TIME = 5.0; // seconds
    
    @Override
    public String getStateName() {
        return STATE_NAME;
    }
    
    @Override
    public boolean canAbsorb(String essenceColor) {
        for (String color : COMPATIBLE_COLORS) {
            if (color.equalsIgnoreCase(essenceColor)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String[] getCompatibleColors() {
        return COMPATIBLE_COLORS.clone();
    }
    
    @Override
    public String getStateColor() {
        return STATE_COLOR;
    }
    
    @Override
    public String getGlowColor() {
        return GLOW_COLOR;
    }
    
    @Override
    public double getSpeedModifier() {
        return SPEED_MODIFIER;
    }
    
    @Override
    public void onEnter(Player player) {
        GameLogger.getInstance().logPlayerTransmutation(
            player.getPreviousStateName(), STATE_NAME);
        GameLogger.getInstance().logGameEvent("Fire State", 
            "Player now absorbs RED and ORANGE essences");
        abilityCooldown = 0;
    }
    
    @Override
    public void onExit(Player player) {
        GameLogger.getInstance().logGameEvent("Fire State", "Exiting fire state");
    }
    
    @Override
    public void update(Player player, double deltaTime) {
        // Update ability cooldown
        if (abilityCooldown > 0) {
            abilityCooldown -= deltaTime;
        }
    }
    
    @Override
    public void useSpecialAbility(Player player) {
        if (abilityCooldown <= 0) {
            // Fire Burst: Temporary massive speed boost
            GameLogger.getInstance().logGameEvent("Fire Ability", 
                "BURST activated - Speed x2 for 2 seconds");
            player.applyTemporarySpeedBoost(2.0, 2.0);
            abilityCooldown = ABILITY_COOLDOWN_TIME;
        } else {
            GameLogger.getInstance().logGameEvent("Fire Ability", 
                String.format("BURST on cooldown (%.1fs remaining)", abilityCooldown));
        }
    }
    
    @Override
    public String getSpecialAbilityDescription() {
        return "BURST: Double speed for 2 seconds (5s cooldown)";
    }
    
    /**
     * Gets the remaining cooldown time for the special ability.
     * 
     * @return Cooldown time in seconds
     */
    public double getAbilityCooldown() {
        return Math.max(0, abilityCooldown);
    }
}
