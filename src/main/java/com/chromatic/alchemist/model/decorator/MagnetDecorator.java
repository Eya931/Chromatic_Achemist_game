package com.chromatic.alchemist.model.decorator;

import com.chromatic.alchemist.logging.GameLogger;

/**
 * DECORATOR PATTERN - Concrete Decorator: Magnet
 * 
 * Adds essence attraction capability.
 * Essences within magnet range are pulled toward the player.
 */
public class MagnetDecorator extends AbilityDecorator {
    
    private static final double MAGNET_STRENGTH = 150.0; // attraction force
    private static final String NAME = "Magnet";
    private static final double DEFAULT_DURATION = 12.0; // seconds
    
    /**
     * Creates a magnet decorator with default duration.
     * 
     * @param wrappedAbility The ability to wrap
     */
    public MagnetDecorator(PlayerAbility wrappedAbility) {
        this(wrappedAbility, DEFAULT_DURATION);
    }
    
    /**
     * Creates a magnet decorator with custom duration.
     * 
     * @param wrappedAbility The ability to wrap
     * @param duration Duration in seconds
     */
    public MagnetDecorator(PlayerAbility wrappedAbility, double duration) {
        super(wrappedAbility, NAME, duration);
        GameLogger.getInstance().logDecoratorApplied(NAME, "Player");
    }
    
    @Override
    public double getMagnetStrength() {
        // Stack magnet strengths
        return wrappedAbility.getMagnetStrength() + MAGNET_STRENGTH;
    }
    
    @Override
    public double getAbsorptionRange() {
        // Also increase absorption range with magnet
        return wrappedAbility.getAbsorptionRange() * 1.5;
    }
    
    @Override
    public String getAbilityDescription() {
        return wrappedAbility.getAbilityDescription() + " + Magnet";
    }
}
