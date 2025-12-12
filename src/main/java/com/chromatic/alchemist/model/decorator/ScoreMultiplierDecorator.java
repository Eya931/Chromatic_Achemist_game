package com.chromatic.alchemist.model.decorator;

import com.chromatic.alchemist.logging.GameLogger;

/**
 * DECORATOR PATTERN - Concrete Decorator: Score Multiplier
 * 
 * Doubles the score earned from absorbing essences.
 */
public class ScoreMultiplierDecorator extends AbilityDecorator {
    
    private static final double SCORE_MULTIPLIER = 2.0;
    private static final String NAME = "ScoreMultiplier";
    private static final double DEFAULT_DURATION = 15.0; // seconds
    
    /**
     * Creates a score multiplier decorator with default duration.
     * 
     * @param wrappedAbility The ability to wrap
     */
    public ScoreMultiplierDecorator(PlayerAbility wrappedAbility) {
        this(wrappedAbility, DEFAULT_DURATION);
    }
    
    /**
     * Creates a score multiplier decorator with custom duration.
     * 
     * @param wrappedAbility The ability to wrap
     * @param duration Duration in seconds
     */
    public ScoreMultiplierDecorator(PlayerAbility wrappedAbility, double duration) {
        super(wrappedAbility, NAME, duration);
        GameLogger.getInstance().logDecoratorApplied(NAME, "Player");
    }
    
    @Override
    public double getScoreMultiplier() {
        return wrappedAbility.getScoreMultiplier() * SCORE_MULTIPLIER;
    }
    
    @Override
    public String getAbilityDescription() {
        return wrappedAbility.getAbilityDescription() + " + Score x2";
    }
}
