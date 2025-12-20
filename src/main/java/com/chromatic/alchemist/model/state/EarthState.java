package com.chromatic.alchemist.model.state;

import com.chromatic.alchemist.logging.GameLogger;
import com.chromatic.alchemist.model.entity.Player;

/**
 * STATE PATTERN - Concrete State: Earth Element
 * 
 * Earth state characteristics:
 * - Can absorb GREEN and BROWN essence particles
 * - Slower movement speed (0.8x modifier)
 * - Special ability: Shield - temporary damage immunity
 * - Visual: Green-brown appearance with rock glow
 */

public class EarthState implements ElementalState {
    
    private static final String STATE_NAME = "EARTH";
    private static final String[] COMPATIBLE_COLORS = {"GREEN", "BROWN"};
    private static final String STATE_COLOR = "#228B22"; // ForestGreen
    private static final String GLOW_COLOR = "#8B4513"; // SaddleBrown
    private static final double SPEED_MODIFIER = 0.8;
    
    // Ability cooldown tracking
    private double abilityCooldown = 0;
    private static final double ABILITY_COOLDOWN_TIME = 10.0; // seconds
    
    @Override
    public String getStateName() {
        return STATE_NAME;
    }
    
    @Override
    public boolean canAbsorb(String essenceColor) {
        for (String color : COMPATIBLE_COLORS) {
            if (color.equalsIgnoreCase(essenceColor)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String[] getCompatibleColors() {
        return COMPATIBLE_COLORS.clone();
    }
    
    @Override
    public String getStateColor() {
        return STATE_COLOR;
    }
    
    @Override
    public String getGlowColor() {
        return GLOW_COLOR;
    }
    
    @Override
    public double getSpeedModifier() {
        return SPEED_MODIFIER;
    }
    
    @Override
    public void onEnter(Player player) {
        GameLogger.getInstance().logPlayerTransmutation(
            player.getPreviousStateName(), STATE_NAME);
        GameLogger.getInstance().logGameEvent("Earth State", 
            "Player now absorbs GREEN and BROWN essences");
        abilityCooldown = 0;
    }
    
    @Override
    public void onExit(Player player) {
        GameLogger.getInstance().logGameEvent("Earth State", "Exiting earth state");
    }
    
    @Override
    public void update(Player player, double deltaTime) {
        if (abilityCooldown > 0) {
            abilityCooldown -= deltaTime;
        }
    }
    
    @Override
    public void useSpecialAbility(Player player) {
        if (abilityCooldown <= 0) {
            // Shield: Damage immunity
            GameLogger.getInstance().logGameEvent("Earth Ability", 
                "SHIELD activated - Damage immunity for 4 seconds");
            player.activateShield(4.0);
            abilityCooldown = ABILITY_COOLDOWN_TIME;
        } else {
            GameLogger.getInstance().logGameEvent("Earth Ability", 
                String.format("SHIELD on cooldown (%.1fs remaining)", abilityCooldown));
        }
    }
    
    @Override
    public String getSpecialAbilityDescription() {
        return "SHIELD: Damage immunity for 4 seconds (10s cooldown)";
    }
    
    public double getAbilityCooldown() {
        return Math.max(0, abilityCooldown);
    }
}
