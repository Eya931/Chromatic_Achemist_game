package com.chromatic.alchemist.model.composite;

import com.chromatic.alchemist.logging.GameLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * COMPOSITE PATTERN - Leaf Node
 * 
 * Represents a simple chamber that cannot contain other chambers.
 * Contains only game elements (essences, obstacles, power-ups).
 */

public class SimpleChamber implements ChamberComponent {
    
    private final String id;
    private final String name;
    private double x;
    private double y;
    private double width;
    private double height;
    private String backgroundColor;
    private String borderColor;
    
    private final List<EssenceParticle> essences;
    private final List<Obstacle> obstacles;
    private final List<PowerUp> powerUps;
    
    public SimpleChamber(String name, double x, double y, double width, double height) {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.backgroundColor = "#1a1a2e";
        this.borderColor = "#4a4a6e";
        this.essences = new ArrayList<>();
        this.obstacles = new ArrayList<>();
        this.powerUps = new ArrayList<>();
        
        GameLogger.getInstance().logEntityCreated("SimpleChamber", id, x, y);
    }
    
    @Override
    public String getId() {
        return id;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public double getX() {
        return x;
    }
    
    @Override
    public double getY() {
        return y;
    }
    
    @Override
    public double getWidth() {
        return width;
    }
    
    @Override
    public double getHeight() {
        return height;
    }
    
    @Override
    public boolean containsPoint(double px, double py) {
        return px >= x && px <= x + width && py >= y && py <= y + height;
    }
    
    @Override
    public void update(double deltaTime) {

        for (EssenceParticle essence : essences) {
            if (!essence.isCollected()) {
                essence.update(deltaTime);
            }
        }
        
        for (Obstacle obstacle : obstacles) {
            obstacle.update(deltaTime);
        }
        
        for (PowerUp powerUp : powerUps) {
            if (!powerUp.isCollected()) {
                powerUp.update(deltaTime);
            }
        }
    }
    
    @Override
    public int getTotalEssenceCount() {
        return essences.size();
    }
    
    @Override
    public int getRemainingEssenceCount() {
        return (int) essences.stream().filter(e -> !e.isCollected()).count();
    }
    
    @Override
    public boolean isCompleted() {
        return getRemainingEssenceCount() == 0;
    }
    
    @Override
    public double getCompletionPercentage() {
        if (essences.isEmpty()) return 100.0;
        int collected = getTotalEssenceCount() - getRemainingEssenceCount();
        return (collected * 100.0) / getTotalEssenceCount();
    }
    
    @Override
    public void addComponent(ChamberComponent component) {
        throw new UnsupportedOperationException("SimpleChamber cannot contain other chambers");
    }
    
    @Override
    public void removeComponent(ChamberComponent component) {
        throw new UnsupportedOperationException("SimpleChamber cannot contain other chambers");
    }
    
    @Override
    public List<ChamberComponent> getChildren() {
        return new ArrayList<>();
    }
    
    @Override
    public boolean isComposite() {
        return false;
    }
    
    @Override
    public String getBackgroundColor() {
        return backgroundColor;
    }
    
    @Override
    public String getBorderColor() {
        return borderColor;
    }
    
    public void setBackgroundColor(String color) {
        this.backgroundColor = color;
    }
    
    public void setBorderColor(String color) {
        this.borderColor = color;
    }
    
    @Override
    public List<EssenceParticle> getAllEssences() {
        return new ArrayList<>(essences);
    }
    
    @Override
    public List<Obstacle> getAllObstacles() {
        return new ArrayList<>(obstacles);
    }
    
    @Override
    public List<PowerUp> getAllPowerUps() {
        return new ArrayList<>(powerUps);
    }
    
    @Override
    public void addEssence(EssenceParticle essence) {
        essences.add(essence);
        GameLogger.getInstance().logEntityCreated("Essence-" + essence.getColor(), 
            essence.getId(), essence.getX(), essence.getY());
    }
    
    @Override
    public void removeEssence(EssenceParticle essence) {
        essences.remove(essence);
    }
    
    @Override
    public void addObstacle(Obstacle obstacle) {
        obstacles.add(obstacle);
        GameLogger.getInstance().logEntityCreated("Obstacle-" + obstacle.getType().name(), 
            obstacle.getId(), obstacle.getX(), obstacle.getY());
    }
    
    @Override
    public void addPowerUp(PowerUp powerUp) {
        powerUps.add(powerUp);
        GameLogger.getInstance().logEntityCreated("PowerUp-" + powerUp.getType().name(), 
            powerUp.getId(), powerUp.getX(), powerUp.getY());
    }
    
    @Override
    public void removePowerUp(PowerUp powerUp) {
        powerUps.remove(powerUp);
    }
    
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public void setSize(double width, double height) {
        this.width = width;
        this.height = height;
    }
}
