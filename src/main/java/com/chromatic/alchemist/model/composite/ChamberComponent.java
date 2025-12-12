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
    
    /**
     * Gets the unique identifier for this chamber.
     * 
     * @return Chamber ID
     */
    String getId();
    
    /**
     * Gets the display name of this chamber.
     * 
     * @return Chamber name
     */
    String getName();
    
    /**
     * Gets the X position of this chamber in the world.
     * 
     * @return X coordinate
     */
    double getX();
    
    /**
     * Gets the Y position of this chamber in the world.
     * 
     * @return Y coordinate
     */
    double getY();
    
    /**
     * Gets the width of this chamber.
     * 
     * @return Width in pixels
     */
    double getWidth();
    
    /**
     * Gets the height of this chamber.
     * 
     * @return Height in pixels
     */
    double getHeight();
    
    /**
     * Checks if this chamber contains the given point.
     * 
     * @param px X coordinate
     * @param py Y coordinate
     * @return true if point is inside this chamber
     */
    boolean containsPoint(double px, double py);
    
    /**
     * Updates this chamber and all its contents.
     * 
     * @param deltaTime Time since last update in seconds
     */
    void update(double deltaTime);
    
    /**
     * Gets the total number of essence particles in this chamber
     * (including all sub-chambers).
     * 
     * @return Total essence count
     */
    int getTotalEssenceCount();
    
    /**
     * Gets the number of remaining (not collected) essences.
     * 
     * @return Remaining essence count
     */
    int getRemainingEssenceCount();
    
    /**
     * Checks if this chamber is completed (all essences collected).
     * 
     * @return true if completed
     */
    boolean isCompleted();
    
    /**
     * Gets the completion percentage of this chamber.
     * 
     * @return Percentage (0-100)
     */
    double getCompletionPercentage();
    
    /**
     * Adds a child component (for composites).
     * 
     * @param component The component to add
     * @throws UnsupportedOperationException if this is a leaf
     */
    void addComponent(ChamberComponent component);
    
    /**
     * Removes a child component (for composites).
     * 
     * @param component The component to remove
     * @throws UnsupportedOperationException if this is a leaf
     */
    void removeComponent(ChamberComponent component);
    
    /**
     * Gets all child components (for composites).
     * 
     * @return List of children, empty for leaves
     */
    List<ChamberComponent> getChildren();
    
    /**
     * Checks if this is a composite (has children).
     * 
     * @return true if composite
     */
    boolean isComposite();
    
    /**
     * Gets the background color for this chamber.
     * 
     * @return Color as hex string
     */
    String getBackgroundColor();
    
    /**
     * Gets the border color for this chamber.
     * 
     * @return Color as hex string
     */
    String getBorderColor();
    
    /**
     * Gets all essence particles in this chamber (and sub-chambers).
     * 
     * @return List of essence particles
     */
    List<EssenceParticle> getAllEssences();
    
    /**
     * Gets all obstacles in this chamber (and sub-chambers).
     * 
     * @return List of obstacles
     */
    List<Obstacle> getAllObstacles();
    
    /**
     * Gets all power-ups in this chamber (and sub-chambers).
     * 
     * @return List of power-ups
     */
    List<PowerUp> getAllPowerUps();
    
    /**
     * Adds an essence particle to this chamber.
     * 
     * @param essence The essence to add
     */
    void addEssence(EssenceParticle essence);
    
    /**
     * Removes an essence particle from this chamber.
     * 
     * @param essence The essence to remove
     */
    void removeEssence(EssenceParticle essence);
    
    /**
     * Adds an obstacle to this chamber.
     * 
     * @param obstacle The obstacle to add
     */
    void addObstacle(Obstacle obstacle);
    
    /**
     * Adds a power-up to this chamber.
     * 
     * @param powerUp The power-up to add
     */
    void addPowerUp(PowerUp powerUp);
    
    /**
     * Removes a power-up from this chamber.
     * 
     * @param powerUp The power-up to remove
     */
    void removePowerUp(PowerUp powerUp);
}
