package com.chromatic.alchemist.model.observer;

import com.chromatic.alchemist.logging.GameLogger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * OBSERVER PATTERN - Subject/Manager
 * 
 * Manages game event subscriptions and notifications.
 * Observers can subscribe to specific event types or all events.
 * Singleton pattern is used to ensure a single event manager instance.
 */

public class GameEventManager {
    
    // Singleton instance
    private static GameEventManager instance;
    
    // Observers subscribed to specific event types
    private final Map<GameEvent.EventType, List<GameObserver>> typeObservers;
    
    // Observers subscribed to all events
    private final List<GameObserver> globalObservers;
    
    // Event history for debugging
    private final List<GameEvent> eventHistory;
    private static final int MAX_HISTORY_SIZE = 100;
    

    private GameEventManager() {
        typeObservers = new ConcurrentHashMap<>();
        globalObservers = Collections.synchronizedList(new ArrayList<>());
        eventHistory = Collections.synchronizedList(new ArrayList<>());
        
        // Initialize lists for all event types
        for (GameEvent.EventType type : GameEvent.EventType.values()) {
            typeObservers.put(type, Collections.synchronizedList(new ArrayList<>()));
        }
    }
    
    public static synchronized GameEventManager getInstance() {
        if (instance == null) {
            instance = new GameEventManager();
        }
        return instance;
    }
    
    public void subscribe(GameEvent.EventType type, GameObserver observer) {
        typeObservers.get(type).add(observer);
        GameLogger.getInstance().logGameEvent("Observer Subscribed", 
            observer.getClass().getSimpleName() + " -> " + type.name());
    }
    
    public void subscribe(GameEvent.EventType[] types, GameObserver observer) {
        for (GameEvent.EventType type : types) {
            subscribe(type, observer);
        }
    }
    
    public void subscribeToAll(GameObserver observer) {
        globalObservers.add(observer);
        GameLogger.getInstance().logGameEvent("Observer Subscribed", 
            observer.getClass().getSimpleName() + " -> ALL EVENTS");
    }
    
    public void unsubscribe(GameEvent.EventType type, GameObserver observer) {
        typeObservers.get(type).remove(observer);
    }
    
    public void unsubscribeFromAll(GameObserver observer) {
        globalObservers.remove(observer);
        for (List<GameObserver> observers : typeObservers.values()) {
            observers.remove(observer);
        }
    }
    
    public void fireEvent(GameEvent event) {
        // Add to history
        eventHistory.add(event);
        if (eventHistory.size() > MAX_HISTORY_SIZE) {
            eventHistory.remove(0);
        }
        
        // Log the event
        logEvent(event);
        
        // Notify type-specific observers
        List<GameObserver> observers = typeObservers.get(event.getType());
        for (GameObserver observer : new ArrayList<>(observers)) {
            try {
                observer.onGameEvent(event);
            } catch (Exception e) {
                GameLogger.getInstance().logError("Error notifying observer: " + e.getMessage(), e);
            }
        }
        
        // Notify global observers
        for (GameObserver observer : new ArrayList<>(globalObservers)) {
            try {
                observer.onGameEvent(event);
            } catch (Exception e) {
                GameLogger.getInstance().logError("Error notifying observer: " + e.getMessage(), e);
            }
        }
    }
    
    public void fireEvent(GameEvent.EventType type) {
        fireEvent(new GameEvent(type));
    }
    
    public void fireEvent(GameEvent.EventType type, String key, Object value) {
        fireEvent(new GameEvent(type, key, value));
    }
    
    private void logEvent(GameEvent event) {
        GameLogger logger = GameLogger.getInstance();
        
        switch (event.getType()) {
            case GAME_STARTED:
                logger.logGameStarted();
                break;
            case GAME_PAUSED:
                logger.logGamePaused();
                break;
            case GAME_RESUMED:
                logger.logGameResumed();
                break;
            case GAME_OVER:
                logger.logGameOver(event.getInt("score", 0));
                break;
            case GAME_VICTORY:
                logger.logVictory(event.getInt("score", 0));
                break;
            case PLAYER_TRANSMUTED:
                logger.logPlayerTransmutation(
                    event.getString("oldElement"),
                    event.getString("newElement"));
                break;
            case SCORE_CHANGED:
                logger.logScoreUpdate(
                    event.getInt("oldScore", 0),
                    event.getInt("newScore", 0),
                    event.getString("reason"));
                break;
            case ESSENCE_ABSORBED:
                logger.logEssenceAbsorbed(
                    event.getString("color"),
                    event.getString("playerId"));
                break;
            case DECORATOR_APPLIED:
                logger.logDecoratorApplied(
                    event.getString("decorator"),
                    event.getString("target"));
                break;
            case DECORATOR_REMOVED:
                logger.logDecoratorRemoved(
                    event.getString("decorator"),
                    event.getString("target"));
                break;
            case LEVEL_STARTED:
                logger.logLevelStarted(
                    event.getInt("levelNumber", 0),
                    event.getString("levelName"));
                break;
            case LEVEL_COMPLETED:
                logger.logLevelCompleted(
                    event.getInt("levelNumber", 0),
                    event.getInt("score", 0),
                    event.getDouble("time", 0));
                break;
            case COLLISION_OBSTACLE:
                logger.logObstacleCollision(
                    event.getString("obstacleType"),
                    event.getInt("damage", 0));
                break;
            case COLLISION_POWERUP:
                logger.logPowerUpCollision(event.getString("powerUpType"));
                break;
            default:
                logger.logGameEvent(event.getType().name(), event.toString());
        }
    }
    
    public List<GameEvent> getEventHistory() {
        return new ArrayList<>(eventHistory);
    }
    
    public void clearHistory() {
        eventHistory.clear();
    }
    
    public int getObserverCount(GameEvent.EventType type) {
        return typeObservers.get(type).size() + globalObservers.size();
    }
}
