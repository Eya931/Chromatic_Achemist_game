package com.chromatic.alchemist.model.decorator;

import com.chromatic.alchemist.logging.GameLogger;

/**
 * DECORATOR PATTERN - Concrete Decorator: Shield
 * 
 * Adds damage reduction to the player.
 * Shield value of 50 = 50% damage reduction.
 */
public class ShieldDecorator extends AbilityDecorator {
    
    private static final double SHIELD_VALUE = 50.0; // 50% damage reduction
    private static final String NAME = "Shield";
    private static final double DEFAULT_DURATION = 15.0; // seconds
    
    /**
     * Creates a shield decorator with default duration.
     * 
     * @param wrappedAbility The ability to wrap
     */
    public ShieldDecorator(PlayerAbility wrappedAbility) {
        this(wrappedAbility, DEFAULT_DURATION);
    }
    
    /**
     * Creates a shield decorator with custom duration.
     * 
     * @param wrappedAbility The ability to wrap
     * @param duration Duration in seconds
     */
    public ShieldDecorator(PlayerAbility wrappedAbility, double duration) {
        super(wrappedAbility, NAME, duration);
        GameLogger.getInstance().logDecoratorApplied(NAME, "Player");
    }
    
    @Override
    public double getShieldValue() {
        // Stack shield values (max 90%)
        return Math.min(90.0, wrappedAbility.getShieldValue() + SHIELD_VALUE);
    }
    
    @Override
    public String getAbilityDescription() {
        return wrappedAbility.getAbilityDescription() + " + Shield+50%";
    }
}
