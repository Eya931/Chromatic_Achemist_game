package com.chromatic.alchemist.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Centralized logging utility for the Chromatic Alchemist game.
 * Provides specialized logging methods for different game events.
 * 
 * Log Format: [YYYY-MM-DD HH:MM:SS] [LEVEL] Message
 * 
 * Categories:
 * - INFO: General game information
 * - STATE: State transitions (game and player)
 * - DECORATOR: Power-up applications and removals
 * - ENTITY: Entity creation and destruction
 * - COLLISION: Collision events
 * - LEVEL: Level changes and completions
 * - EVENT: Game events (score, achievements, etc.)
 */
public class GameLogger {
    
    // Singleton instance
    private static GameLogger instance;
    
    // Specialized loggers for different categories
    private final Logger gameStateLogger;
    private final Logger playerStateLogger;
    private final Logger decoratorLogger;
    private final Logger entityLogger;
    private final Logger collisionLogger;
    private final Logger levelLogger;
    private final Logger eventLogger;
    private final Logger generalLogger;
    
    /**
     * Private constructor for singleton pattern.
     * Initializes all specialized loggers.
     */
    private GameLogger() {
        gameStateLogger = LogManager.getLogger("GameState");
        playerStateLogger = LogManager.getLogger("PlayerState");
        decoratorLogger = LogManager.getLogger("Decorator");
        entityLogger = LogManager.getLogger("Entity");
        collisionLogger = LogManager.getLogger("Collision");
        levelLogger = LogManager.getLogger("Level");
        eventLogger = LogManager.getLogger("Event");
        generalLogger = LogManager.getLogger("General");
    }
    
    /**
     * Gets the singleton instance of GameLogger.
     * Thread-safe lazy initialization.
     * 
     * @return The GameLogger singleton instance
     */
    public static synchronized GameLogger getInstance() {
        if (instance == null) {
            instance = new GameLogger();
        }
        return instance;
    }
    
    // ==================== GENERAL LOGGING ====================
    
    /**
     * Logs general information messages.
     * 
     * @param message The message to log
     */
    public void logInfo(String message) {
        generalLogger.info(message);
    }
    
    /**
     * Logs warning messages.
     * 
     * @param message The warning message
     */
    public void logWarning(String message) {
        generalLogger.warn(message);
    }
    
    /**
     * Logs error messages.
     * 
     * @param message The error message
     */
    public void logError(String message) {
        generalLogger.error(message);
    }
    
    /**
     * Logs error messages with exception.
     * 
     * @param message The error message
     * @param throwable The exception
     */
    public void logError(String message, Throwable throwable) {
        generalLogger.error(message, throwable);
    }
    
    // ==================== GAME STATE LOGGING ====================
    
    /**
     * Logs game state transitions.
     * Format: [STATE] Game: OLD_STATE -> NEW_STATE
     * 
     * @param oldState The previous game state
     * @param newState The new game state
     */
    public void logGameStateChange(String oldState, String newState) {
        gameStateLogger.info("[STATE] Game: {} -> {}", oldState, newState);
    }
    
    /**
     * Logs game started event.
     */
    public void logGameStarted() {
        gameStateLogger.info("Game started");
    }
    
    /**
     * Logs game paused event.
     */
    public void logGamePaused() {
        gameStateLogger.info("Game paused");
    }
    
    /**
     * Logs game resumed event.
     */
    public void logGameResumed() {
        gameStateLogger.info("Game resumed");
    }
    
    /**
     * Logs game over event.
     * 
     * @param finalScore The final score achieved
     */
    public void logGameOver(int finalScore) {
        gameStateLogger.info("Game Over - Final Score: {}", finalScore);
    }
    
    /**
     * Logs victory event.
     * 
     * @param finalScore The final score achieved
     */
    public void logVictory(int finalScore) {
        gameStateLogger.info("Victory! Final Score: {}", finalScore);
    }
    
    // ==================== PLAYER STATE LOGGING ====================
    
    /**
     * Logs player state transitions.
     * Format: [STATE] Player: OLD_STATE -> NEW_STATE
     * 
     * @param oldState The previous player state
     * @param newState The new player state
     */
    public void logPlayerStateChange(String oldState, String newState) {
        playerStateLogger.info("[STATE] Player: {} -> {}", oldState, newState);
    }
    
    /**
     * Logs player elemental transmutation.
     * 
     * @param oldElement The previous element
     * @param newElement The new element
     */
    public void logPlayerTransmutation(String oldElement, String newElement) {
        playerStateLogger.info("[STATE] Player Transmutation: {} -> {}", oldElement, newElement);
    }
    
    /**
     * Logs player movement state change.
     * 
     * @param oldMovement The previous movement state
     * @param newMovement The new movement state
     */
    public void logPlayerMovementChange(String oldMovement, String newMovement) {
        playerStateLogger.info("[STATE] Player Movement: {} -> {}", oldMovement, newMovement);
    }
    
    // ==================== DECORATOR LOGGING ====================
    
    /**
     * Logs decorator (power-up/mutation) application.
     * Format: [DECORATOR] DecoratorName applied to Target
     * 
     * @param decoratorName The name of the decorator
     * @param targetName The target entity
     */
    public void logDecoratorApplied(String decoratorName, String targetName) {
        decoratorLogger.info("[DECORATOR] {} applied to {}", decoratorName, targetName);
    }
    
    /**
     * Logs decorator removal/expiration.
     * 
     * @param decoratorName The name of the decorator
     * @param targetName The target entity
     */
    public void logDecoratorRemoved(String decoratorName, String targetName) {
        decoratorLogger.info("[DECORATOR] {} removed from {}", decoratorName, targetName);
    }
    
    /**
     * Logs decorator stack (multiple decorators).
     * 
     * @param decoratorNames Array of decorator names
     * @param targetName The target entity
     */
    public void logDecoratorStack(String[] decoratorNames, String targetName) {
        String decorators = String.join(", ", decoratorNames);
        decoratorLogger.info("[DECORATOR] Active decorators on {}: [{}]", targetName, decorators);
    }
    
    // ==================== ENTITY LOGGING ====================
    
    /**
     * Logs entity creation/spawn.
     * 
     * @param entityType The type of entity
     * @param entityId The entity identifier
     * @param x X position
     * @param y Y position
     */
    public void logEntityCreated(String entityType, String entityId, double x, double y) {
        entityLogger.info("[ENTITY] Created: {} (ID: {}) at ({}, {})", entityType, entityId, x, y);
    }
    
    /**
     * Logs entity destruction/despawn.
     * 
     * @param entityType The type of entity
     * @param entityId The entity identifier
     * @param reason The reason for destruction
     */
    public void logEntityDestroyed(String entityType, String entityId, String reason) {
        entityLogger.info("[ENTITY] Destroyed: {} (ID: {}) - Reason: {}", entityType, entityId, reason);
    }
    
    /**
     * Logs essence particle absorbed.
     * 
     * @param essenceColor The color of the essence
     * @param playerId The player who absorbed it
     */
    public void logEssenceAbsorbed(String essenceColor, String playerId) {
        entityLogger.info("[ENTITY] Essence {} absorbed by {}", essenceColor, playerId);
    }
    
    // ==================== COLLISION LOGGING ====================
    
    /**
     * Logs collision event between two entities.
     * 
     * @param entity1 First entity involved
     * @param entity2 Second entity involved
     * @param result The result of the collision
     */
    public void logCollision(String entity1, String entity2, String result) {
        collisionLogger.info("[COLLISION] {} <-> {} : {}", entity1, entity2, result);
    }
    
    /**
     * Logs player collision with obstacle.
     * 
     * @param obstacleType The type of obstacle
     * @param damage The damage taken (if any)
     */
    public void logObstacleCollision(String obstacleType, int damage) {
        collisionLogger.info("[COLLISION] Player hit {} - Damage: {}", obstacleType, damage);
    }
    
    /**
     * Logs player collision with power-up.
     * 
     * @param powerUpType The type of power-up
     */
    public void logPowerUpCollision(String powerUpType) {
        collisionLogger.info("[COLLISION] Player collected power-up: {}", powerUpType);
    }
    
    // ==================== LEVEL LOGGING ====================
    
    /**
     * Logs level start.
     * 
     * @param levelNumber The level number
     * @param levelName The level name
     */
    public void logLevelStarted(int levelNumber, String levelName) {
        levelLogger.info("[LEVEL] Started Level {}: {}", levelNumber, levelName);
    }
    
    /**
     * Logs level completion.
     * 
     * @param levelNumber The level number
     * @param score The score achieved
     * @param timeSeconds The time taken in seconds
     */
    public void logLevelCompleted(int levelNumber, int score, double timeSeconds) {
        levelLogger.info("[LEVEL] Completed Level {} - Score: {} - Time: {}s", levelNumber, score, timeSeconds);
    }
    
    /**
     * Logs chamber entered (composite pattern).
     * 
     * @param chamberName The chamber name
     * @param parentChamber The parent chamber (null if root)
     */
    public void logChamberEntered(String chamberName, String parentChamber) {
        if (parentChamber != null) {
            levelLogger.info("[LEVEL] Entered chamber: {} (inside {})", chamberName, parentChamber);
        } else {
            levelLogger.info("[LEVEL] Entered root chamber: {}", chamberName);
        }
    }
    
    /**
     * Logs recipe completion.
     * 
     * @param recipeName The recipe completed
     * @param bonusPoints The bonus points awarded
     */
    public void logRecipeCompleted(String recipeName, int bonusPoints) {
        levelLogger.info("[LEVEL] Recipe completed: {} (+{} points)", recipeName, bonusPoints);
    }
    
    // ==================== EVENT LOGGING ====================
    
    /**
     * Logs score update.
     * 
     * @param oldScore The previous score
     * @param newScore The new score
     * @param reason The reason for score change
     */
    public void logScoreUpdate(int oldScore, int newScore, String reason) {
        eventLogger.info("[EVENT] Score: {} -> {} ({})", oldScore, newScore, reason);
    }
    
    /**
     * Logs health update.
     * 
     * @param oldHealth The previous health
     * @param newHealth The new health
     */
    public void logHealthUpdate(int oldHealth, int newHealth) {
        eventLogger.info("[EVENT] Health: {} -> {}", oldHealth, newHealth);
    }
    
    /**
     * Logs achievement unlocked.
     * 
     * @param achievementName The achievement name
     */
    public void logAchievement(String achievementName) {
        eventLogger.info("[EVENT] Achievement Unlocked: {}", achievementName);
    }
    
    /**
     * Logs custom game event.
     * 
     * @param eventName The event name
     * @param details Additional details
     */
    public void logGameEvent(String eventName, String details) {
        eventLogger.info("[EVENT] {}: {}", eventName, details);
    }
    
    /**
     * Logs input event.
     * 
     * @param inputType The type of input
     * @param action The action performed
     */
    public void logInput(String inputType, String action) {
        eventLogger.debug("[INPUT] {}: {}", inputType, action);
    }
}
