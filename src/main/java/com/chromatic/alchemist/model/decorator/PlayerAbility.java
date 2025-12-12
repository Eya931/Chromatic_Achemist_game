package com.chromatic.alchemist.model.decorator;

/**
 * DECORATOR PATTERN - Component Interface
 * 
 * Defines the interface for player abilities that can be decorated.
 * This interface represents the base component that decorators will wrap.
 * 
 * Abilities include:
 * - Speed: Movement speed
 * - Absorption Range: Range at which essences can be absorbed
 * - Shield: Damage reduction
 * - Score Multiplier: Points earned multiplier
 */
public interface PlayerAbility {
    
    /**
     * Gets the base movement speed.
     * 
     * @return Speed value (pixels per second)
     */
    double getSpeed();
    
    /**
     * Gets the absorption range for essences.
     * 
     * @return Range in pixels
     */
    double getAbsorptionRange();
    
    /**
     * Gets the shield/damage reduction value.
     * 
     * @return Shield value (0-100, percentage reduction)
     */
    double getShieldValue();
    
    /**
     * Gets the score multiplier.
     * 
     * @return Multiplier (1.0 = normal)
     */
    double getScoreMultiplier();
    
    /**
     * Gets a description of all active abilities.
     * 
     * @return Description string
     */
    String getAbilityDescription();
    
    /**
     * Checks if this ability grants multi-absorb capability.
     * 
     * @return true if can absorb multiple essences at once
     */
    boolean hasMultiAbsorb();
    
    /**
     * Gets the magnet strength for attracting essences.
     * 
     * @return Magnet strength (0 = none)
     */
    double getMagnetStrength();
}
