package com.chromatic.alchemist.model;

import com.chromatic.alchemist.logging.GameLogger;
import com.chromatic.alchemist.model.composite.*;
import com.chromatic.alchemist.model.decorator.*;
import com.chromatic.alchemist.model.entity.Player;
import com.chromatic.alchemist.model.observer.GameEvent;
import com.chromatic.alchemist.model.observer.GameEventManager;
import com.chromatic.alchemist.model.observer.GameObserver;
import com.chromatic.alchemist.model.state.GameState;

import java.util.ArrayList;
import java.util.List;

/**
 * Main Game Model
 * 
 * Central game logic class that manages:
 * - Game state (menu, playing, paused, game over, victory)
 * - Player entity
 * - Current level (Composite pattern structure)
 * - Collision detection
 * - Game loop updates
 */

public class GameModel implements GameObserver {
    
    private GameState currentState;
    private boolean running;
    
    private Player player;
    
    private List<Level> levels;
    private int currentLevelIndex;
    private Level currentLevel;
    
    private double gameTime;
    private double levelTime;
    
    private double gameWidth;
    private double gameHeight;
    private int difficulty; 
    
    private List<Recipe> activeRecipes;
    private int recipesCompleted;
    
    public GameModel(double width, double height) {
        this.gameWidth = width;
        this.gameHeight = height;
        this.currentState = GameState.MENU;
        this.running = false;
        this.difficulty = 1;
        this.levels = new ArrayList<>();
        this.activeRecipes = new ArrayList<>();
        this.recipesCompleted = 0;
        
        GameEventManager.getInstance().subscribe(GameEvent.EventType.PLAYER_DIED, this);
        GameEventManager.getInstance().subscribe(GameEvent.EventType.LEVEL_COMPLETED, this);
        
        GameLogger.getInstance().logInfo("GameModel initialized");
    }
    
    public void initializeGame() {
        GameLogger.getInstance().logInfo("Initializing new game...");
        
        player = new Player(gameWidth / 2, gameHeight / 2);
        
        gameTime = 0;
        levelTime = 0;
        recipesCompleted = 0;
        
        generateLevels();
        
        currentLevelIndex = 0;
        loadLevel(currentLevelIndex);
        
        GameLogger.getInstance().logInfo("Game initialized successfully");
    }
    
    private void generateLevels() {
        levels.clear();
        
        // Level 1: Simple chamber with few essences
        levels.add(LevelGenerator.generateLevel(1, gameWidth, gameHeight, difficulty));
        
        // Level 2: Compound chambers introduced
        levels.add(LevelGenerator.generateLevel(2, gameWidth, gameHeight, difficulty));
        
        // Level 3: More complex structure with obstacles
        levels.add(LevelGenerator.generateLevel(3, gameWidth, gameHeight, difficulty));
        
        // Level 4: Advanced with moving obstacles
        levels.add(LevelGenerator.generateLevel(4, gameWidth, gameHeight, difficulty));
        
        // Level 5: Final challenge
        levels.add(LevelGenerator.generateLevel(5, gameWidth, gameHeight, difficulty));
        
        GameLogger.getInstance().logInfo("Generated " + levels.size() + " levels");
    }
    
    public void loadLevel(int levelIndex) {
        if (levelIndex < 0 || levelIndex >= levels.size()) {
            GameLogger.getInstance().logError("Invalid level index: " + levelIndex);
            return;
        }
        
        currentLevelIndex = levelIndex;
        currentLevel = levels.get(levelIndex);
        levelTime = 0;
        
        player.setX(currentLevel.getPlayerStartX());
        player.setY(currentLevel.getPlayerStartY());
        
        activeRecipes = currentLevel.getRecipes();
        
        GameEventManager.getInstance().fireEvent(
            new GameEvent(GameEvent.EventType.LEVEL_STARTED)
                .addData("levelNumber", levelIndex + 1)
                .addData("levelName", currentLevel.getName())
        );
        
        GameLogger.getInstance().logLevelStarted(levelIndex + 1, currentLevel.getName());
    }
    
    public void startGame() {
        if (currentState == GameState.MENU || currentState == GameState.GAME_OVER) {
            initializeGame();
        }
        
        changeState(GameState.PLAYING);
        running = true;
        
        GameEventManager.getInstance().fireEvent(GameEvent.EventType.GAME_STARTED);
    }
    
    public void pauseGame() {
        if (currentState == GameState.PLAYING) {
            changeState(GameState.PAUSED);
            GameEventManager.getInstance().fireEvent(GameEvent.EventType.GAME_PAUSED);
        }
    }
    
    public void resumeGame() {
        if (currentState == GameState.PAUSED) {
            changeState(GameState.PLAYING);
            GameEventManager.getInstance().fireEvent(GameEvent.EventType.GAME_RESUMED);
        }
    }
    
    public void gameOver() {
        changeState(GameState.GAME_OVER);
        running = false;
        
        GameEventManager.getInstance().fireEvent(
            new GameEvent(GameEvent.EventType.GAME_OVER)
                .addData("score", player.getScore())
                .addData("level", currentLevelIndex + 1)
                .addData("time", gameTime)
        );
    }
    
    public void victory() {
        changeState(GameState.VICTORY);
        running = false;
        
        GameEventManager.getInstance().fireEvent(
            new GameEvent(GameEvent.EventType.GAME_VICTORY)
                .addData("score", player.getScore())
                .addData("time", gameTime)
        );
    }
    
    private void changeState(GameState newState) {
        if (currentState != newState) {
            currentState.logTransitionTo(newState);
            currentState = newState;
        }
    }
    
    public void update(double deltaTime) {
        if (currentState != GameState.PLAYING) {
            return;
        }
        
        gameTime += deltaTime;
        levelTime += deltaTime;
        
        player.update(deltaTime);
        player.constrainToBounds(0, 0, gameWidth, gameHeight);
        
        if (currentLevel != null) {
            currentLevel.update(deltaTime);
        }
        
        processCollisions();

        checkRecipes();

        checkLevelCompletion();

        if (!player.isAlive()) {
            gameOver();
        }
    }

    private void processCollisions() {
        if (currentLevel == null) return;
        
        ChamberComponent rootChamber = currentLevel.getRootChamber();
        
        List<EssenceParticle> essences = rootChamber.getAllEssences();
        List<Obstacle> obstacles = rootChamber.getAllObstacles();
        List<PowerUp> powerUps = rootChamber.getAllPowerUps();
        
        double playerX = player.getX();
        double playerY = player.getY();
        double absorptionRange = player.getAbsorptionRange();
        double magnetStrength = player.getMagnetStrength();
        
        List<EssenceParticle> toAbsorb = new ArrayList<>();
        for (EssenceParticle essence : essences) {
            if (essence.isCollected()) continue;
            
            if (magnetStrength > 0) {
                double dx = playerX - essence.getX();
                double dy = playerY - essence.getY();
                double dist = Math.sqrt(dx * dx + dy * dy);
                if (dist < 150) { 
                    essence.moveToward(playerX, playerY, magnetStrength, 0.016);
                }
            }
            
            if (essence.isInRange(playerX, playerY, absorptionRange)) {
                if (player.canAbsorb(essence.getColor())) {
                    toAbsorb.add(essence);
                    if (!player.hasMultiAbsorb()) {
                        break; 
                    }
                }
            }
        }
        
        for (EssenceParticle essence : toAbsorb) {
            absorbEssence(essence);
        }
        
        if (!player.isPhasing()) {
            for (Obstacle obstacle : obstacles) {
                if (obstacle.collidesWithCircle(playerX, playerY, player.getRadius())) {
                    handleObstacleCollision(obstacle);
                }
            }
        }
        
        for (PowerUp powerUp : powerUps) {
            if (powerUp.isCollected()) continue;
            
            if (powerUp.isInRange(playerX, playerY, player.getRadius())) {
                collectPowerUp(powerUp);
            }
        }
    }
    
    private void absorbEssence(EssenceParticle essence) {
        essence.setCollected(true);
        player.incrementEssencesCollected();
        player.addScore(essence.getPointValue(), "Essence absorbed: " + essence.getColor());
        
        GameEventManager.getInstance().fireEvent(
            new GameEvent(GameEvent.EventType.ESSENCE_ABSORBED)
                .addData("color", essence.getColor())
                .addData("playerId", player.getId())
                .addData("points", essence.getPointValue())
        );
        
        GameLogger.getInstance().logEssenceAbsorbed(essence.getColor(), player.getId());
    }
    
    private void handleObstacleCollision(Obstacle obstacle) {
        player.takeDamage(obstacle.getDamage());
        
        GameEventManager.getInstance().fireEvent(
            new GameEvent(GameEvent.EventType.COLLISION_OBSTACLE)
                .addData("obstacleType", obstacle.getName())
                .addData("damage", obstacle.getDamage())
        );
        
        GameLogger.getInstance().logObstacleCollision(obstacle.getName(), obstacle.getDamage());
    }
    
    private void collectPowerUp(PowerUp powerUp) {
        powerUp.setCollected(true);
        
        // Apply the appropriate decorator based on power-up type
        double duration = powerUp.getDuration();
        switch (powerUp.getType()) {
            case SPEED_BOOST:
                player.applySpeedBoost(duration);
                break;
            case SHIELD:
                player.applyShieldDecorator(duration);
                break;
            case MAGNET:
                player.applyMagnet(duration);
                break;
            case MULTI_ABSORB:
                player.applyMultiAbsorb(duration);
                break;
            case SCORE_MULTIPLIER:
                player.applyScoreMultiplier(duration);
                break;
            case RANGE_BOOST:
                player.applyRangeBoost(duration);
                break;
        }
        
        GameEventManager.getInstance().fireEvent(
            new GameEvent(GameEvent.EventType.POWERUP_COLLECTED)
                .addData("powerUpType", powerUp.getDisplayName())
                .addData("duration", duration)
        );
        
        GameLogger.getInstance().logPowerUpCollision(powerUp.getDisplayName());
    }
    
    private void checkRecipes() {
        for (Recipe recipe : activeRecipes) {
            if (!recipe.isCompleted() && recipe.checkCompletion(player)) {
                recipe.setCompleted(true);
                recipesCompleted++;
                
                int bonus = recipe.getBonusPoints();
                player.addScore(bonus, "Recipe completed: " + recipe.getName());
                
                GameLogger.getInstance().logRecipeCompleted(recipe.getName(), bonus);
                
                GameEventManager.getInstance().fireEvent(
                    new GameEvent(GameEvent.EventType.RECIPE_COMPLETED)
                        .addData("recipeName", recipe.getName())
                        .addData("bonusPoints", bonus)
                );
            }
        }
    }
    
    private void checkLevelCompletion() {
        if (currentLevel != null && currentLevel.isCompleted()) {
            GameEventManager.getInstance().fireEvent(
                new GameEvent(GameEvent.EventType.LEVEL_COMPLETED)
                    .addData("levelNumber", currentLevelIndex + 1)
                    .addData("score", player.getScore())
                    .addData("time", levelTime)
            );
            
            GameLogger.getInstance().logLevelCompleted(
                currentLevelIndex + 1, player.getScore(), levelTime);
            
            if (currentLevelIndex < levels.size() - 1) {
                loadLevel(currentLevelIndex + 1);
            } else {
                victory();
            }
        }
    }
    
    @Override
    public void onGameEvent(GameEvent event) {
        switch (event.getType()) {
            case PLAYER_DIED:
                gameOver();
                break;
            case LEVEL_COMPLETED:
                break;
            default:
                break;
        }
    }
    
    // ==================== INPUT HANDLING ====================
    
    public void playerMoveUp(boolean pressed) {
        if (player != null) player.setMovingUp(pressed);
    }
    
    public void playerMoveDown(boolean pressed) {
        if (player != null) player.setMovingDown(pressed);
    }
    
    public void playerMoveLeft(boolean pressed) {
        if (player != null) player.setMovingLeft(pressed);
    }
    
    public void playerMoveRight(boolean pressed) {
        if (player != null) player.setMovingRight(pressed);
    }
    
    public void playerTransmuteToFire() {
        if (player != null) player.transmuteToFire();
    }
    
    public void playerTransmuteToWater() {
        if (player != null) player.transmuteToWater();
    }
    
    public void playerTransmuteToEarth() {
        if (player != null) player.transmuteToEarth();
    }
    
    public void playerTransmuteToAir() {
        if (player != null) player.transmuteToAir();
    }
    
    public void playerUseAbility() {
        if (player != null) player.useSpecialAbility();
    }
    
    // ==================== GETTERS ====================
    
    public GameState getCurrentState() { return currentState; }
    public boolean isRunning() { return running; }
    public Player getPlayer() { return player; }
    public Level getCurrentLevel() { return currentLevel; }
    public int getCurrentLevelIndex() { return currentLevelIndex; }
    public int getTotalLevels() { return levels.size(); }
    public double getGameTime() { return gameTime; }
    public double getLevelTime() { return levelTime; }
    public double getGameWidth() { return gameWidth; }
    public double getGameHeight() { return gameHeight; }
    public List<Recipe> getActiveRecipes() { return activeRecipes; }
    public int getRecipesCompleted() { return recipesCompleted; }
    
    public void setDifficulty(int difficulty) {
        this.difficulty = Math.max(1, Math.min(3, difficulty));
    }
    
    public int getDifficulty() { return difficulty; }
}
