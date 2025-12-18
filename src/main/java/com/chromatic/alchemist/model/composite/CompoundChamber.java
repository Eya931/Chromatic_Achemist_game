package com.chromatic.alchemist.model.composite;

import com.chromatic.alchemist.logging.GameLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * COMPOSITE PATTERN - Composite Node
 * 
 * Represents a compound chamber that can contain other chambers (both simple and compound).
 * Implements the tree structure for hierarchical level design.
 */

public class CompoundChamber implements ChamberComponent {
    
    private final String id;
    private final String name;
    private double x;
    private double y;
    private double width;
    private double height;
    private String backgroundColor;
    private String borderColor;
    
    private final List<ChamberComponent> children;
    
    private final List<EssenceParticle> directEssences;
    private final List<Obstacle> directObstacles;
    private final List<PowerUp> directPowerUps;
    

    public CompoundChamber(String name, double x, double y, double width, double height) {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.backgroundColor = "#0f0f1e";
        this.borderColor = "#6a6a9e";
        this.children = new ArrayList<>();
        this.directEssences = new ArrayList<>();
        this.directObstacles = new ArrayList<>();
        this.directPowerUps = new ArrayList<>();
        
        GameLogger.getInstance().logEntityCreated("CompoundChamber", id, x, y);
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

        for (EssenceParticle essence : directEssences) {
            if (!essence.isCollected()) {
                essence.update(deltaTime);
            }
        }
        
        for (Obstacle obstacle : directObstacles) {
            obstacle.update(deltaTime);
        }
        
        for (PowerUp powerUp : directPowerUps) {
            if (!powerUp.isCollected()) {
                powerUp.update(deltaTime);
            }
        }
        
        for (ChamberComponent child : children) {
            child.update(deltaTime);
        }
    }
    
    @Override
    public int getTotalEssenceCount() {
        int count = directEssences.size();
        for (ChamberComponent child : children) {
            count += child.getTotalEssenceCount();
        }
        return count;
    }
    
    @Override
    public int getRemainingEssenceCount() {
        int count = (int) directEssences.stream().filter(e -> !e.isCollected()).count();
        for (ChamberComponent child : children) {
            count += child.getRemainingEssenceCount();
        }
        return count;
    }
    
    @Override
    public boolean isCompleted() {
        if (directEssences.stream().anyMatch(e -> !e.isCollected())) {
            return false;
        }
        for (ChamberComponent child : children) {
            if (!child.isCompleted()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public double getCompletionPercentage() {
        int total = getTotalEssenceCount();
        if (total == 0) return 100.0;
        int remaining = getRemainingEssenceCount();
        return ((total - remaining) * 100.0) / total;
    }
    
    @Override
    public void addComponent(ChamberComponent component) {
        children.add(component);
        GameLogger.getInstance().logChamberEntered(component.getName(), this.name);
    }
    
    @Override
    public void removeComponent(ChamberComponent component) {
        children.remove(component);
    }
    
    @Override
    public List<ChamberComponent> getChildren() {
        return new ArrayList<>(children);
    }
    
    @Override
    public boolean isComposite() {
        return true;
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
        List<EssenceParticle> all = new ArrayList<>(directEssences);
        for (ChamberComponent child : children) {
            all.addAll(child.getAllEssences());
        }
        return all;
    }
    
    @Override
    public List<Obstacle> getAllObstacles() {
        List<Obstacle> all = new ArrayList<>(directObstacles);
        for (ChamberComponent child : children) {
            all.addAll(child.getAllObstacles());
        }
        return all;
    }
    
    @Override
    public List<PowerUp> getAllPowerUps() {
        List<PowerUp> all = new ArrayList<>(directPowerUps);
        for (ChamberComponent child : children) {
            all.addAll(child.getAllPowerUps());
        }
        return all;
    }
    
    @Override
    public void addEssence(EssenceParticle essence) {
        directEssences.add(essence);
        GameLogger.getInstance().logEntityCreated("Essence-" + essence.getColor(), 
            essence.getId(), essence.getX(), essence.getY());
    }
    
    @Override
    public void removeEssence(EssenceParticle essence) {
        directEssences.remove(essence);
        for (ChamberComponent child : children) {
            child.removeEssence(essence);
        }
    }
    
    @Override
    public void addObstacle(Obstacle obstacle) {
        directObstacles.add(obstacle);
        GameLogger.getInstance().logEntityCreated("Obstacle-" + obstacle.getType().name(), 
            obstacle.getId(), obstacle.getX(), obstacle.getY());
    }
    
    @Override
    public void addPowerUp(PowerUp powerUp) {
        directPowerUps.add(powerUp);
        GameLogger.getInstance().logEntityCreated("PowerUp-" + powerUp.getType().name(), 
            powerUp.getId(), powerUp.getX(), powerUp.getY());
    }
    
    @Override
    public void removePowerUp(PowerUp powerUp) {
        directPowerUps.remove(powerUp);
        for (ChamberComponent child : children) {
            child.removePowerUp(powerUp);
        }
    }
    
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public void setSize(double width, double height) {
        this.width = width;
        this.height = height;
    }
    
    public ChamberComponent findDeepestChamber(double px, double py) {
        if (!containsPoint(px, py)) {
            return null;
        }
        
        for (ChamberComponent child : children) {
            if (child.containsPoint(px, py)) {
                if (child.isComposite()) {
                    ChamberComponent deeper = ((CompoundChamber) child).findDeepestChamber(px, py);
                    if (deeper != null) {
                        return deeper;
                    }
                }
                return child;
            }
        }
        
        return this;
    }
}
