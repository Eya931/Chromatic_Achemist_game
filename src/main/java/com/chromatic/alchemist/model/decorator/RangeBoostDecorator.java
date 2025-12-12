package com.chromatic.alchemist.model.decorator;

import com.chromatic.alchemist.logging.GameLogger;

/**
 * DECORATOR PATTERN - Concrete Decorator: Range Boost
 * 
 * Increases the absorption range, allowing the player
 * to collect essences from further away.
 */
public class RangeBoostDecorator extends AbilityDecorator {
    
    private static final double RANGE_MULTIPLIER = 2.0;
    private static final String NAME = "RangeBoost";
    private static final double DEFAULT_DURATION = 12.0; // seconds
    
    /**
     * Creates a range boost decorator with default duration.
     * 
     * @param wrappedAbility The ability to wrap
     */
    public RangeBoostDecorator(PlayerAbility wrappedAbility) {
        this(wrappedAbility, DEFAULT_DURATION);
    }
    
    /**
     * Creates a range boost decorator with custom duration.
     * 
     * @param wrappedAbility The ability to wrap
     * @param duration Duration in seconds
     */
    public RangeBoostDecorator(PlayerAbility wrappedAbility, double duration) {
        super(wrappedAbility, NAME, duration);
        GameLogger.getInstance().logDecoratorApplied(NAME, "Player");
    }
    
    @Override
    public double getAbsorptionRange() {
        return wrappedAbility.getAbsorptionRange() * RANGE_MULTIPLIER;
    }
    
    @Override
    public String getAbilityDescription() {
        return wrappedAbility.getAbilityDescription() + " + Range x2";
    }
}
