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
    
    public GameEvent(EventType type) {
        this.type = type;
        this.data = new HashMap<>();
        this.timestamp = System.currentTimeMillis();
    }
    
    public GameEvent(EventType type, String key, Object value) {
        this(type);
        this.data.put(key, value);
    }
    
    public GameEvent addData(String key, Object value) {
        data.put(key, value);
        return this;
    }
    
    public Object getData(String key) {
        return data.get(key);
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getData(String key, Class<T> type) {
        Object value = data.get(key);
        if (type.isInstance(value)) {
            return (T) value;
        }
        return null;
    }
    
    public String getString(String key) {
        Object value = data.get(key);
        return value != null ? value.toString() : "";
    }
    
    public int getInt(String key, int defaultValue) {
        Object value = data.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return defaultValue;
    }
    
    public double getDouble(String key, double defaultValue) {
        Object value = data.get(key);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return defaultValue;
    }
    
    public boolean getBoolean(String key, boolean defaultValue) {
        Object value = data.get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return defaultValue;
    }
    
    public EventType getType() {
        return type;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
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
