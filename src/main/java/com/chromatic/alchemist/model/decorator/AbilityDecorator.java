package com.chromatic.alchemist.model.decorator;

/**
 * DECORATOR PATTERN - Abstract Decorator
 * 
 * Base class for all ability decorators.
 * Wraps a PlayerAbility and delegates to it by default.
 * Concrete decorators override specific methods to add functionality.
 */
public abstract class AbilityDecorator implements PlayerAbility {
    
    /** The wrapped ability component */
    protected final PlayerAbility wrappedAbility;
    
    /** Duration remaining for this decorator (in seconds, -1 for permanent) */
    protected double duration;
    
    /** Name of this decorator for logging */
    protected final String decoratorName;
    
    /**
     * Creates a new ability decorator.
     * 
     * @param wrappedAbility The ability to wrap
     * @param decoratorName The name of this decorator
     * @param duration Duration in seconds (-1 for permanent)
     */
    public AbilityDecorator(PlayerAbility wrappedAbility, String decoratorName, double duration) {
        this.wrappedAbility = wrappedAbility;
        this.decoratorName = decoratorName;
        this.duration = duration;
    }
    
    @Override
    public double getSpeed() {
        return wrappedAbility.getSpeed();
    }
    
    @Override
    public double getAbsorptionRange() {
        return wrappedAbility.getAbsorptionRange();
    }
    
    @Override
    public double getShieldValue() {
        return wrappedAbility.getShieldValue();
    }
    
    @Override
    public double getScoreMultiplier() {
        return wrappedAbility.getScoreMultiplier();
    }
    
    @Override
    public String getAbilityDescription() {
        return wrappedAbility.getAbilityDescription() + " + " + decoratorName;
    }
    
    @Override
    public boolean hasMultiAbsorb() {
        return wrappedAbility.hasMultiAbsorb();
    }
    
    @Override
    public double getMagnetStrength() {
        return wrappedAbility.getMagnetStrength();
    }
    
    /**
     * Gets the name of this decorator.
     * 
     * @return Decorator name
     */
    public String getDecoratorName() {
        return decoratorName;
    }
    
    /**
     * Gets the remaining duration.
     * 
     * @return Duration in seconds (-1 if permanent)
     */
    public double getDuration() {
        return duration;
    }
    
    /**
     * Updates the duration.
     * 
     * @param deltaTime Time passed in seconds
     * @return true if decorator is still active, false if expired
     */
    public boolean updateDuration(double deltaTime) {
        if (duration < 0) {
            return true; // Permanent
        }
        duration -= deltaTime;
        return duration > 0;
    }
    
    /**
     * Checks if this decorator has expired.
     * 
     * @return true if expired
     */
    public boolean isExpired() {
        return duration >= 0 && duration <= 0;
    }
    
    /**
     * Gets the wrapped ability.
     * 
     * @return The wrapped ability
     */
    public PlayerAbility getWrappedAbility() {
        return wrappedAbility;
    }
}
