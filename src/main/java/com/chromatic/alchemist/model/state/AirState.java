package com.chromatic.alchemist.model.state;

import com.chromatic.alchemist.logging.GameLogger;
import com.chromatic.alchemist.model.entity.Player;

/**
 * STATE PATTERN - Concrete State: Air Element
 * 
 * Air state characteristics:
 * - Can absorb WHITE and YELLOW essence particles
 * - Fastest movement speed (1.4x modifier)
 * - Special ability: Dash - quick directional dash
 * - Visual: White-yellow appearance with wind glow
 */

public class AirState implements ElementalState {
    
    private static final String STATE_NAME = "AIR";
    private static final String[] COMPATIBLE_COLORS = {"WHITE", "YELLOW"};
    private static final String STATE_COLOR = "#F0F8FF"; // AliceBlue
    private static final String GLOW_COLOR = "#FFD700"; // Gold
    private static final double SPEED_MODIFIER = 1.4;
    
    // Ability cooldown tracking
    private double abilityCooldown = 0;
    private static final double ABILITY_COOLDOWN_TIME = 3.0; // seconds
    
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
        GameLogger.getInstance().logGameEvent("Air State", 
            "Player now absorbs WHITE and YELLOW essences");
        abilityCooldown = 0;
    }
    
    @Override
    public void onExit(Player player) {
        GameLogger.getInstance().logGameEvent("Air State", "Exiting air state");
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
            // Dash: Quick directional dash
            GameLogger.getInstance().logGameEvent("Air Ability", 
                "DASH activated - Quick dash in movement direction");
            player.performDash(200.0); // 200 pixel dash
            abilityCooldown = ABILITY_COOLDOWN_TIME;
        } else {
            GameLogger.getInstance().logGameEvent("Air Ability", 
                String.format("DASH on cooldown (%.1fs remaining)", abilityCooldown));
        }
    }
    
    @Override
    public String getSpecialAbilityDescription() {
        return "DASH: Quick dash in movement direction (3s cooldown)";
    }
    
    public double getAbilityCooldown() {
        return Math.max(0, abilityCooldown);
    }
}
