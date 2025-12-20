package com.chromatic.alchemist.model.composite;

import java.util.List;

/**
 * COMPOSITE PATTERN - Component Interface
 * 
 * Defines the interface for all chamber components in the game.
 * A chamber can be either a simple chamber (leaf) or a compound chamber
 * containing other chambers (composite).
 * 
 * This allows for hierarchical level design:
 * - Level (root composite)
 *   - Main Chamber (composite)
 *     - Sub-Chamber A (leaf)
 *     - Sub-Chamber B (composite)
 *       - Inner Chamber 1 (leaf)
 *       - Inner Chamber 2 (leaf)
 */

public interface ChamberComponent {
    
    String getId();
    
    String getName();
    
    double getX();
    
    double getY();
    
    double getWidth();
    
    double getHeight();
    
    boolean containsPoint(double px, double py);
    
    void update(double deltaTime);
    
    int getTotalEssenceCount();
    
    int getRemainingEssenceCount();
    
    boolean isCompleted();
    
    double getCompletionPercentage();
    
    void addComponent(ChamberComponent component);
    
    void removeComponent(ChamberComponent component);
    
    List<ChamberComponent> getChildren();
    
    boolean isComposite();
    
    String getBackgroundColor();
    
    String getBorderColor();
    
    List<EssenceParticle> getAllEssences();
    
    List<Obstacle> getAllObstacles();
    
    List<PowerUp> getAllPowerUps();
    
    void addEssence(EssenceParticle essence);
    
    void removeEssence(EssenceParticle essence);
    
    void addObstacle(Obstacle obstacle);
    
    void addPowerUp(PowerUp powerUp);
    
    void removePowerUp(PowerUp powerUp);
}
