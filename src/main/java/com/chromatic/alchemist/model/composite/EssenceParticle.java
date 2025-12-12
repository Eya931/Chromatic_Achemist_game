package com.chromatic.alchemist.model.composite;

import java.util.UUID;

/**
 * COMPOSITE PATTERN - Element in Chamber
 * 
 * Represents an essence particle that players must collect.
 * Each essence has a color that determines which elemental state can absorb it.
 */
public class EssenceParticle {
    
    private final String id;
    private final String color;
    private double x;
    private double y;
    private final double radius;
    private boolean collected;
    private final int pointValue;
    
    // Animation properties
    private double pulsePhase;
    private double floatOffset;
    
    /**
     * Creates a new essence particle.
     * 
     * @param color The color name (RED, ORANGE, BLUE, CYAN, GREEN, BROWN, WHITE, YELLOW)
     * @param x X position
     * @param y Y position
     * @param pointValue Points awarded when collected
     */
    public EssenceParticle(String color, double x, double y, int pointValue) {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.color = color.toUpperCase();
        this.x = x;
        this.y = y;
        this.radius = 12.0;
        this.collected = false;
        this.pointValue = pointValue;
        this.pulsePhase = Math.random() * Math.PI * 2;
        this.floatOffset = Math.random() * Math.PI * 2;
    }
    
    /**
     * Updates the essence animation.
     * 
     * @param deltaTime Time since last update
     */
    public void update(double deltaTime) {
        pulsePhase += deltaTime * 3.0;
        floatOffset += deltaTime * 2.0;
    }
    
    /**
     * Gets the current visual radius (with pulse effect).
     * 
     * @return Visual radius
     */
    public double getVisualRadius() {
        return radius + Math.sin(pulsePhase) * 3.0;
    }
    
    /**
     * Gets the current Y offset for floating animation.
     * 
     * @return Y offset
     */
    public double getFloatY() {
        return y + Math.sin(floatOffset) * 5.0;
    }
    
    // Getters and setters
    
    public String getId() {
        return id;
    }
    
    public String getColor() {
        return color;
    }
    
    public double getX() {
        return x;
    }
    
    public void setX(double x) {
        this.x = x;
    }
    
    public double getY() {
        return y;
    }
    
    public void setY(double y) {
        this.y = y;
    }
    
    public double getRadius() {
        return radius;
    }
    
    public boolean isCollected() {
        return collected;
    }
    
    public void setCollected(boolean collected) {
        this.collected = collected;
    }
    
    public int getPointValue() {
        return pointValue;
    }
    
    /**
     * Gets the JavaFX color representation for this essence.
     * 
     * @return Color string for JavaFX
     */
    public String getColorHex() {
        switch (color) {
            case "RED": return "#FF0000";
            case "ORANGE": return "#FF8C00";
            case "BLUE": return "#0066FF";
            case "CYAN": return "#00FFFF";
            case "GREEN": return "#00FF00";
            case "BROWN": return "#8B4513";
            case "WHITE": return "#FFFFFF";
            case "YELLOW": return "#FFD700";
            default: return "#FFFFFF";
        }
    }
    
    /**
     * Gets the glow color for visual effects.
     * 
     * @return Glow color hex
     */
    public String getGlowColor() {
        switch (color) {
            case "RED": return "#FF6666";
            case "ORANGE": return "#FFB366";
            case "BLUE": return "#6699FF";
            case "CYAN": return "#66FFFF";
            case "GREEN": return "#66FF66";
            case "BROWN": return "#CD853F";
            case "WHITE": return "#FFFFCC";
            case "YELLOW": return "#FFEB66";
            default: return "#FFFFFF";
        }
    }
    
    /**
     * Checks if a point is within collection range of this essence.
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
    
    /**
     * Moves the essence toward a point (for magnet effect).
     * 
     * @param targetX Target X
     * @param targetY Target Y
     * @param strength Pull strength
     * @param deltaTime Time delta
     */
    public void moveToward(double targetX, double targetY, double strength, double deltaTime) {
        double dx = targetX - x;
        double dy = targetY - y;
        double dist = Math.sqrt(dx * dx + dy * dy);
        if (dist > 0) {
            x += (dx / dist) * strength * deltaTime;
            y += (dy / dist) * strength * deltaTime;
        }
    }
}
