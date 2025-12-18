package com.chromatic.alchemist.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Centralized logging utility for the Chromatic Alchemist game.
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
    
    private static GameLogger instance;
    
    private final Logger gameStateLogger;
    private final Logger playerStateLogger;
    private final Logger decoratorLogger;
    private final Logger entityLogger;
    private final Logger collisionLogger;
    private final Logger levelLogger;
    private final Logger eventLogger;
    private final Logger generalLogger;
    
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
    
    public static synchronized GameLogger getInstance() {
        if (instance == null) {
            instance = new GameLogger();
        }
        return instance;
    }
    
    // ==================== GENERAL LOGGING ====================
    
    public void logInfo(String message) {
        generalLogger.info(message);
    }

    public void logWarning(String message) {
        generalLogger.warn(message);
    }
    
    public void logError(String message) {
        generalLogger.error(message);
    }
    

    public void logError(String message, Throwable throwable) {
        generalLogger.error(message, throwable);
    }
    
    // ==================== GAME STATE LOGGING ====================
      
    public void logGameStateChange(String oldState, String newState) {
        gameStateLogger.info("[STATE] Game: {} -> {}", oldState, newState);
    }
    
    public void logGameStarted() {
        gameStateLogger.info("Game started");
    }
    
    public void logGamePaused() {
        gameStateLogger.info("Game paused");
    }
    
    public void logGameResumed() {
        gameStateLogger.info("Game resumed");
    }
    
    public void logGameOver(int finalScore) {
        gameStateLogger.info("Game Over - Final Score: {}", finalScore);
    }
    
    public void logVictory(int finalScore) {
        gameStateLogger.info("Victory! Final Score: {}", finalScore);
    }
    
    // ==================== PLAYER STATE LOGGING ====================
    
    public void logPlayerStateChange(String oldState, String newState) {
        playerStateLogger.info("[STATE] Player: {} -> {}", oldState, newState);
    }
    
    public void logPlayerTransmutation(String oldElement, String newElement) {
        playerStateLogger.info("[STATE] Player Transmutation: {} -> {}", oldElement, newElement);
    }
    
    public void logPlayerMovementChange(String oldMovement, String newMovement) {
        playerStateLogger.info("[STATE] Player Movement: {} -> {}", oldMovement, newMovement);
    }
    
    // ==================== DECORATOR LOGGING ====================
    
    public void logDecoratorApplied(String decoratorName, String targetName) {
        decoratorLogger.info("[DECORATOR] {} applied to {}", decoratorName, targetName);
    }
    
    public void logDecoratorRemoved(String decoratorName, String targetName) {
        decoratorLogger.info("[DECORATOR] {} removed from {}", decoratorName, targetName);
    }
    
    public void logDecoratorStack(String[] decoratorNames, String targetName) {
        String decorators = String.join(", ", decoratorNames);
        decoratorLogger.info("[DECORATOR] Active decorators on {}: [{}]", targetName, decorators);
    }
    
    // ==================== ENTITY LOGGING ====================
    
    public void logEntityCreated(String entityType, String entityId, double x, double y) {
        entityLogger.info("[ENTITY] Created: {} (ID: {}) at ({}, {})", entityType, entityId, x, y);
    }
    
    public void logEntityDestroyed(String entityType, String entityId, String reason) {
        entityLogger.info("[ENTITY] Destroyed: {} (ID: {}) - Reason: {}", entityType, entityId, reason);
    }
    
    public void logEssenceAbsorbed(String essenceColor, String playerId) {
        entityLogger.info("[ENTITY] Essence {} absorbed by {}", essenceColor, playerId);
    }
    
    // ==================== COLLISION LOGGING ====================
    
    public void logCollision(String entity1, String entity2, String result) {
        collisionLogger.info("[COLLISION] {} <-> {} : {}", entity1, entity2, result);
    }
    
    public void logObstacleCollision(String obstacleType, int damage) {
        collisionLogger.info("[COLLISION] Player hit {} - Damage: {}", obstacleType, damage);
    }
    
    public void logPowerUpCollision(String powerUpType) {
        collisionLogger.info("[COLLISION] Player collected power-up: {}", powerUpType);
    }
    
    // ==================== LEVEL LOGGING ====================
    
    public void logLevelStarted(int levelNumber, String levelName) {
        levelLogger.info("[LEVEL] Started Level {}: {}", levelNumber, levelName);
    }
    
    public void logLevelCompleted(int levelNumber, int score, double timeSeconds) {
        levelLogger.info("[LEVEL] Completed Level {} - Score: {} - Time: {}s", levelNumber, score, timeSeconds);
    }
    
    public void logChamberEntered(String chamberName, String parentChamber) {
        if (parentChamber != null) {
            levelLogger.info("[LEVEL] Entered chamber: {} (inside {})", chamberName, parentChamber);
        } else {
            levelLogger.info("[LEVEL] Entered root chamber: {}", chamberName);
        }
    }
    
    public void logRecipeCompleted(String recipeName, int bonusPoints) {
        levelLogger.info("[LEVEL] Recipe completed: {} (+{} points)", recipeName, bonusPoints);
    }
    
    // ==================== EVENT LOGGING ====================
    
    public void logScoreUpdate(int oldScore, int newScore, String reason) {
        eventLogger.info("[EVENT] Score: {} -> {} ({})", oldScore, newScore, reason);
    }
    
    public void logHealthUpdate(int oldHealth, int newHealth) {
        eventLogger.info("[EVENT] Health: {} -> {}", oldHealth, newHealth);
    }
    
    public void logAchievement(String achievementName) {
        eventLogger.info("[EVENT] Achievement Unlocked: {}", achievementName);
    }
    
    public void logGameEvent(String eventName, String details) {
        eventLogger.info("[EVENT] {}: {}", eventName, details);
    }
    
    public void logInput(String inputType, String action) {
        eventLogger.debug("[INPUT] {}: {}", inputType, action);
    }
}
