package com.chromatic.alchemist.model.decorator;

import com.chromatic.alchemist.logging.GameLogger;

/**
 * DECORATOR PATTERN - Concrete Decorator: Speed Boost
 * 
 * Increases player movement speed by 50%.
 * Stacks with other speed modifiers.
 */
public class SpeedBoostDecorator extends AbilityDecorator {
    
    private static final double SPEED_MULTIPLIER = 1.5;
    private static final String NAME = "SpeedBoost";
    private static final double DEFAULT_DURATION = 10.0; // seconds
    
    /**
     * Creates a speed boost decorator with default duration.
     * 
     * @param wrappedAbility The ability to wrap
     */
    public SpeedBoostDecorator(PlayerAbility wrappedAbility) {
        this(wrappedAbility, DEFAULT_DURATION);
    }
    
    /**
     * Creates a speed boost decorator with custom duration.
     * 
     * @param wrappedAbility The ability to wrap
     * @param duration Duration in seconds
     */
    public SpeedBoostDecorator(PlayerAbility wrappedAbility, double duration) {
        super(wrappedAbility, NAME, duration);
        GameLogger.getInstance().logDecoratorApplied(NAME, "Player");
    }
    
    @Override
    public double getSpeed() {
        return wrappedAbility.getSpeed() * SPEED_MULTIPLIER;
    }
    
    @Override
    public String getAbilityDescription() {
        return wrappedAbility.getAbilityDescription() + " + Speed+50%";
    }
}
