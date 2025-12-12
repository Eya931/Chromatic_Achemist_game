package com.chromatic.alchemist.model.decorator;

import com.chromatic.alchemist.logging.GameLogger;

/**
 * DECORATOR PATTERN - Concrete Decorator: Multi-Absorb
 * 
 * Allows the player to absorb multiple essences at once,
 * even if they are different colors (within current state compatibility).
 */
public class MultiAbsorbDecorator extends AbilityDecorator {
    
    private static final String NAME = "MultiAbsorb";
    private static final double DEFAULT_DURATION = 8.0; // seconds
    
    /**
     * Creates a multi-absorb decorator with default duration.
     * 
     * @param wrappedAbility The ability to wrap
     */
    public MultiAbsorbDecorator(PlayerAbility wrappedAbility) {
        this(wrappedAbility, DEFAULT_DURATION);
    }
    
    /**
     * Creates a multi-absorb decorator with custom duration.
     * 
     * @param wrappedAbility The ability to wrap
     * @param duration Duration in seconds
     */
    public MultiAbsorbDecorator(PlayerAbility wrappedAbility, double duration) {
        super(wrappedAbility, NAME, duration);
        GameLogger.getInstance().logDecoratorApplied(NAME, "Player");
    }
    
    @Override
    public boolean hasMultiAbsorb() {
        return true;
    }
    
    @Override
    public String getAbilityDescription() {
        return wrappedAbility.getAbilityDescription() + " + MultiAbsorb";
    }
}
