package com.chromatic.alchemist.model.composite;

import java.util.UUID;

/**
 * COMPOSITE PATTERN - Element in Chamber
 * 
 * Represents a power-up that grants the player a decorator ability.
 */
public class PowerUp {
    
    /**
     * Types of power-ups corresponding to decorators.
     */
    public enum PowerUpType {
        SPEED_BOOST("Speed Boost", "#00FF00", "SpeedBoost"),
        SHIELD("Shield", "#0088FF", "Shield"),
        MAGNET("Magnet", "#FF00FF", "Magnet"),
        MULTI_ABSORB("Multi-Absorb", "#FFFF00", "MultiAbsorb"),
        SCORE_MULTIPLIER("Score x2", "#FF8800", "ScoreMultiplier"),
        RANGE_BOOST("Range Boost", "#00FFFF", "RangeBoost");
        
        private final String displayName;
        private final String color;
        private final String decoratorName;
        
        PowerUpType(String displayName, String color, String decoratorName) {
            this.displayName = displayName;
            this.color = color;
            this.decoratorName = decoratorName;
        }
        
        public String getDisplayName() { return displayName; }
        public String getColor() { return color; }
        public String getDecoratorName() { return decoratorName; }
    }
    
    private final String id;
    private final PowerUpType type;
    private double x;
    private double y;
    private final double radius;
    private boolean collected;
    private final double duration; // Duration of the effect in seconds
    
    // Animation properties
    private double rotationAngle;
    private double pulsePhase;
    private double floatPhase;
    
    /**
     * Creates a new power-up.
     * 
     * @param type The type of power-up
     * @param x X position
     * @param y Y position
     * @param duration Duration of the effect in seconds
     */
    public PowerUp(PowerUpType type, double x, double y, double duration) {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.type = type;
        this.x = x;
        this.y = y;
        this.radius = 18.0;
        this.collected = false;
        this.duration = duration;
        this.rotationAngle = 0;
        this.pulsePhase = Math.random() * Math.PI * 2;
        this.floatPhase = Math.random() * Math.PI * 2;
    }
    
    /**
     * Updates the power-up animation.
     * 
     * @param deltaTime Time since last update
     */
    public void update(double deltaTime) {
        rotationAngle += deltaTime * 60.0; // 60 degrees per second
        if (rotationAngle >= 360) rotationAngle -= 360;
        
        pulsePhase += deltaTime * 4.0;
        floatPhase += deltaTime * 2.0;
    }
    
    /**
     * Gets the current visual radius with pulse effect.
     * 
     * @return Visual radius
     */
    public double getVisualRadius() {
        return radius + Math.sin(pulsePhase) * 4.0;
    }
    
    /**
     * Gets the Y offset for floating animation.
     * 
     * @return Float Y offset
     */
    public double getFloatY() {
        return y + Math.sin(floatPhase) * 8.0;
    }
    
    /**
     * Checks if a point is within collection range.
     * 
     * @param px Point X
     * @param py Point Y
     * @param range Collection range
     * @return true if within range
     */
    public boolean isInRange(double px, double py, double range) {
        double dx = x - px;
        double dy = y - py;
        return Math.sqrt(dx * dx + dy * dy) <= range + radius;
    }
    
    // Getters and setters
    
    public String getId() { return id; }
    public PowerUpType getType() { return type; }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getRadius() { return radius; }
    public boolean isCollected() { return collected; }
    public void setCollected(boolean collected) { this.collected = collected; }
    public double getDuration() { return duration; }
    public double getRotationAngle() { return rotationAngle; }
    public String getColor() { return type.getColor(); }
    public String getDisplayName() { return type.getDisplayName(); }
    public String getDecoratorName() { return type.getDecoratorName(); }
}
