package com.chromatic.alchemist.model.observer;

import java.util.HashMap;
import java.util.Map;

/**
 * OBSERVER PATTERN - Event Object
 * 
 * Represents a game event that is broadcast to observers.
 * Contains event type and associated data.
 */
public class GameEvent {
    
    /**
     * Types of game events.
     */
    public enum EventType {
        // Game state events
        GAME_STARTED,
        GAME_PAUSED,
        GAME_RESUMED,
        GAME_OVER,
        GAME_VICTORY,
        
        // Player events
        PLAYER_STATE_CHANGED,
        PLAYER_TRANSMUTED,
        PLAYER_DAMAGED,
        PLAYER_HEALED,
        PLAYER_DIED,
        
        // Score events
        SCORE_CHANGED,
        HIGH_SCORE_ACHIEVED,
        
        // Essence events
        ESSENCE_ABSORBED,
        ESSENCE_SPAWNED,
        
        // Decorator/Power-up events
        POWERUP_COLLECTED,
        DECORATOR_APPLIED,
        DECORATOR_REMOVED,
        
        // Level events
        LEVEL_STARTED,
        LEVEL_COMPLETED,
        CHAMBER_ENTERED,
        RECIPE_COMPLETED,
        
        // Collision events
        COLLISION_OBSTACLE,
        COLLISION_ESSENCE,
        COLLISION_POWERUP,
        
        // UI events
        UI_UPDATE_REQUIRED,
        SHOW_MESSAGE
    }
    
    private final EventType type;
    private final Map<String, Object> data;
    private final long timestamp;
    
    /**
     * Creates a new game event.
     * 
     * @param type The event type
     */
    public GameEvent(EventType type) {
        this.type = type;
        this.data = new HashMap<>();
        this.timestamp = System.currentTimeMillis();
    }
    
    /**
     * Creates a new game event with initial data.
     * 
     * @param type The event type
     * @param key Initial data key
     * @param value Initial data value
     */
    public GameEvent(EventType type, String key, Object value) {
        this(type);
        this.data.put(key, value);
    }
    
    /**
     * Adds data to the event.
     * 
     * @param key Data key
     * @param value Data value
     * @return This event (for chaining)
     */
    public GameEvent addData(String key, Object value) {
        data.put(key, value);
        return this;
    }
    
    /**
     * Gets data from the event.
     * 
     * @param key Data key
     * @return The data value, or null if not found
     */
    public Object getData(String key) {
        return data.get(key);
    }
    
    /**
     * Gets typed data from the event.
     * 
     * @param key Data key
     * @param type Expected type
     * @param <T> Type parameter
     * @return The data value cast to the expected type, or null
     */
    @SuppressWarnings("unchecked")
    public <T> T getData(String key, Class<T> type) {
        Object value = data.get(key);
        if (type.isInstance(value)) {
            return (T) value;
        }
        return null;
    }
    
    /**
     * Gets string data from the event.
     * 
     * @param key Data key
     * @return The string value, or empty string if not found
     */
    public String getString(String key) {
        Object value = data.get(key);
        return value != null ? value.toString() : "";
    }
    
    /**
     * Gets integer data from the event.
     * 
     * @param key Data key
     * @param defaultValue Default value if not found
     * @return The integer value
     */
    public int getInt(String key, int defaultValue) {
        Object value = data.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return defaultValue;
    }
    
    /**
     * Gets double data from the event.
     * 
     * @param key Data key
     * @param defaultValue Default value if not found
     * @return The double value
     */
    public double getDouble(String key, double defaultValue) {
        Object value = data.get(key);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return defaultValue;
    }
    
    /**
     * Gets boolean data from the event.
     * 
     * @param key Data key
     * @param defaultValue Default value if not found
     * @return The boolean value
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        Object value = data.get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return defaultValue;
    }
    
    /**
     * Gets the event type.
     * 
     * @return Event type
     */
    public EventType getType() {
        return type;
    }
    
    /**
     * Gets the timestamp when this event was created.
     * 
     * @return Timestamp in milliseconds
     */
    public long getTimestamp() {
        return timestamp;
    }
    
    /**
     * Checks if the event has data for a specific key.
     * 
     * @param key Data key
     * @return true if data exists
     */
    public boolean hasData(String key) {
        return data.containsKey(key);
    }
    
    @Override
    public String toString() {
        return "GameEvent{" +
                "type=" + type +
                ", data=" + data +
                ", timestamp=" + timestamp +
                '}';
    }
}
