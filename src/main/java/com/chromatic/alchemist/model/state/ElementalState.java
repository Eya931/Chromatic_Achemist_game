package com.chromatic.alchemist.model.state;

import com.chromatic.alchemist.model.entity.Player;

/**
 * STATE PATTERN - State Interface
 * 
 * Defines the interface for all player elemental states.
 * Each state determines:
 * - Which essence colors the player can absorb
 * - Visual appearance (color)
 * - Special abilities
 * - Movement characteristics
 * 
 * The player can transmute between states to absorb different essences.
 */
public interface ElementalState {
    
    /**
     * Gets the name of this elemental state.
     * 
     * @return The state name (e.g., "FIRE", "WATER", "EARTH", "AIR")
     */
    String getStateName();
    
    /**
     * Checks if this state can absorb the given essence color.
     * 
     * @param essenceColor The color of the essence particle
     * @return true if the essence can be absorbed, false otherwise
     */
    boolean canAbsorb(String essenceColor);
    
    /**
     * Gets the compatible essence colors for this state.
     * 
     * @return Array of compatible color names
     */
    String[] getCompatibleColors();
    
    /**
     * Gets the visual color representation of this state.
     * 
     * @return The color as a hex string (e.g., "#FF4500")
     */
    String getStateColor();
    
    /**
     * Gets the secondary/glow color for visual effects.
     * 
     * @return The glow color as a hex string
     */
    String getGlowColor();
    
    /**
     * Gets the movement speed modifier for this state.
     * 
     * @return Speed multiplier (1.0 = normal)
     */
    double getSpeedModifier();
    
    /**
     * Handles state entry - called when player enters this state.
     * 
     * @param player The player entering this state
     */
    void onEnter(Player player);
    
    /**
     * Handles state exit - called when player leaves this state.
     * 
     * @param player The player leaving this state
     */
    void onExit(Player player);
    
    /**
     * Updates the state - called every game tick.
     * 
     * @param player The player in this state
     * @param deltaTime Time since last update in seconds
     */
    void update(Player player, double deltaTime);
    
    /**
     * Performs the special ability of this state.
     * 
     * @param player The player using the ability
     */
    void useSpecialAbility(Player player);
    
    /**
     * Gets the description of this state's special ability.
     * 
     * @return Description text
     */
    String getSpecialAbilityDescription();
}
