package com.chromatic.alchemist.model.decorator;

/**
 * DECORATOR PATTERN - Concrete Component
 * 
 * Base implementation of player abilities without any decorations.
 * This represents the default/base state of the player.
 */
public class BasePlayerAbility implements PlayerAbility {
    
    private static final double BASE_SPEED = 200.0; // pixels per second
    private static final double BASE_ABSORPTION_RANGE = 30.0; // pixels
    private static final double BASE_SHIELD = 0.0; // no shield
    private static final double BASE_SCORE_MULTIPLIER = 1.0; // normal
    private static final double BASE_MAGNET_STRENGTH = 0.0; // no magnet
    
    @Override
    public double getSpeed() {
        return BASE_SPEED;
    }
    
    @Override
    public double getAbsorptionRange() {
        return BASE_ABSORPTION_RANGE;
    }
    
    @Override
    public double getShieldValue() {
        return BASE_SHIELD;
    }
    
    @Override
    public double getScoreMultiplier() {
        return BASE_SCORE_MULTIPLIER;
    }
    
    @Override
    public String getAbilityDescription() {
        return "Base Abilities";
    }
    
    @Override
    public boolean hasMultiAbsorb() {
        return false;
    }
    
    @Override
    public double getMagnetStrength() {
        return BASE_MAGNET_STRENGTH;
    }
}
