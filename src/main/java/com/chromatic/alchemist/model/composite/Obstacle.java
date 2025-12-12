package com.chromatic.alchemist.model.composite;

import java.util.UUID;

/**
 * COMPOSITE PATTERN - Element in Chamber
 * 
 * Represents an obstacle that can damage the player.
 * Different obstacle types have different behaviors.
 */
public class Obstacle {
    
    /**
     * Types of obstacles.
     */
    public enum ObstacleType {
        STATIC("Static Barrier", "#444444", 10),
        MOVING("Moving Hazard", "#FF4444", 15),
        ROTATING("Rotating Spike", "#AA00AA", 20),
        PULSING("Pulsing Field", "#FFAA00", 12);
        
        private final String name;
        private final String color;
        private final int damage;
        
        ObstacleType(String name, String color, int damage) {
            this.name = name;
            this.color = color;
            this.damage = damage;
        }
        
        public String getName() { return name; }
        public String getColor() { return color; }
        public int getDamage() { return damage; }
    }
    
    private final String id;
    private final ObstacleType type;
    private double x;
    private double y;
    private double width;
    private double height;
    
    // Movement properties (for moving obstacles)
    private double startX, startY;
    private double endX, endY;
    private double moveSpeed;
    private boolean movingToEnd;
    
    // Rotation properties (for rotating obstacles)
    private double rotation;
    private double rotationSpeed;
    
    // Pulsing properties
    private double pulsePhase;
    private double pulseSize;
    
    /**
     * Creates a static obstacle.
     * 
     * @param x X position
     * @param y Y position
     * @param width Width
     * @param height Height
     */
    public Obstacle(double x, double y, double width, double height) {
        this(ObstacleType.STATIC, x, y, width, height);
    }
    
    /**
     * Creates an obstacle of a specific type.
     * 
     * @param type Obstacle type
     * @param x X position
     * @param y Y position
     * @param width Width
     * @param height Height
     */
    public Obstacle(ObstacleType type, double x, double y, double width, double height) {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.type = type;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        
        // Initialize movement
        this.startX = x;
        this.startY = y;
        this.endX = x;
        this.endY = y;
        this.moveSpeed = 100.0;
        this.movingToEnd = true;
        
        // Initialize rotation
        this.rotation = 0;
        this.rotationSpeed = 90.0; // degrees per second
        
        // Initialize pulsing
        this.pulsePhase = Math.random() * Math.PI * 2;
        this.pulseSize = 0.3;
    }
    
    /**
     * Sets up movement path for moving obstacles.
     * 
     * @param endX End X position
     * @param endY End Y position
     * @param speed Movement speed
     */
    public void setMovementPath(double endX, double endY, double speed) {
        this.startX = this.x;
        this.startY = this.y;
        this.endX = endX;
        this.endY = endY;
        this.moveSpeed = speed;
    }
    
    /**
     * Updates the obstacle animation and movement.
     * 
     * @param deltaTime Time since last update
     */
    public void update(double deltaTime) {
        switch (type) {
            case MOVING:
                updateMovement(deltaTime);
                break;
            case ROTATING:
                rotation += rotationSpeed * deltaTime;
                if (rotation >= 360) rotation -= 360;
                break;
            case PULSING:
                pulsePhase += deltaTime * 2.0;
                break;
            default:
                // Static obstacles don't update
                break;
        }
    }
    
    private void updateMovement(double deltaTime) {
        double targetX = movingToEnd ? endX : startX;
        double targetY = movingToEnd ? endY : startY;
        
        double dx = targetX - x;
        double dy = targetY - y;
        double dist = Math.sqrt(dx * dx + dy * dy);
        
        if (dist < moveSpeed * deltaTime) {
            x = targetX;
            y = targetY;
            movingToEnd = !movingToEnd;
        } else {
            x += (dx / dist) * moveSpeed * deltaTime;
            y += (dy / dist) * moveSpeed * deltaTime;
        }
    }
    
    /**
     * Gets the current visual width (with pulse effect for PULSING type).
     * 
     * @return Visual width
     */
    public double getVisualWidth() {
        if (type == ObstacleType.PULSING) {
            return width * (1 + Math.sin(pulsePhase) * pulseSize);
        }
        return width;
    }
    
    /**
     * Gets the current visual height (with pulse effect for PULSING type).
     * 
     * @return Visual height
     */
    public double getVisualHeight() {
        if (type == ObstacleType.PULSING) {
            return height * (1 + Math.sin(pulsePhase) * pulseSize);
        }
        return height;
    }
    
    /**
     * Checks if this obstacle collides with a circle.
     * 
     * @param cx Circle center X
     * @param cy Circle center Y
     * @param radius Circle radius
     * @return true if colliding
     */
    public boolean collidesWithCircle(double cx, double cy, double radius) {
        double closestX = Math.max(x, Math.min(cx, x + getVisualWidth()));
        double closestY = Math.max(y, Math.min(cy, y + getVisualHeight()));
        
        double dx = cx - closestX;
        double dy = cy - closestY;
        
        return (dx * dx + dy * dy) < (radius * radius);
    }
    
    // Getters
    
    public String getId() { return id; }
    public ObstacleType getType() { return type; }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public double getRotation() { return rotation; }
    public int getDamage() { return type.getDamage(); }
    public String getColor() { return type.getColor(); }
    public String getName() { return type.getName(); }
    
    public void setRotationSpeed(double speed) { this.rotationSpeed = speed; }
}
