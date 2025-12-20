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
    
    String getStateName();
    
    boolean canAbsorb(String essenceColor);
    
    String[] getCompatibleColors();
    
    String getStateColor();
    
    String getGlowColor();
    
    double getSpeedModifier();
    
    void onEnter(Player player);
    
    void onExit(Player player);
    
    void update(Player player, double deltaTime);
    
    void useSpecialAbility(Player player);
    
    String getSpecialAbilityDescription();
}
