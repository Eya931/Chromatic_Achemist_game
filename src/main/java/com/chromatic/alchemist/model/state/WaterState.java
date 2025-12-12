package com.chromatic.alchemist.model.state;

import com.chromatic.alchemist.logging.GameLogger;
import com.chromatic.alchemist.model.entity.Player;

/**
 * STATE PATTERN - Concrete State: Water Element
 * 
 * Water state characteristics:
 * - Can absorb BLUE and CYAN essence particles
 * - Normal movement speed (1.0x modifier)
 * - Special ability: Flow - phase through obstacles briefly
 * - Visual: Blue appearance with wave glow
 */
public class WaterState implements ElementalState {
    
    private static final String STATE_NAME = "WATER";
    private static final String[] COMPATIBLE_COLORS = {"BLUE", "CYAN"};
    private static final String STATE_COLOR = "#1E90FF"; // DodgerBlue
    private static final String GLOW_COLOR = "#00CED1"; // DarkTurquoise
    private static final double SPEED_MODIFIER = 1.0;
    
    // Ability cooldown tracking
    private double abilityCooldown = 0;
    private static final double ABILITY_COOLDOWN_TIME = 8.0; // seconds
    
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
        GameLogger.getInstance().logGameEvent("Water State", 
            "Player now absorbs BLUE and CYAN essences");
        abilityCooldown = 0;
    }
    
    @Override
    public void onExit(Player player) {
        GameLogger.getInstance().logGameEvent("Water State", "Exiting water state");
    }
    
    @Override
    public void update(Player player, double deltaTime) {
        // Update ability cooldown
        if (abilityCooldown > 0) {
            abilityCooldown -= deltaTime;
        }
    }
    
    @Override
    public void useSpecialAbility(Player player) {
        if (abilityCooldown <= 0) {
            // Flow: Phase through obstacles
            GameLogger.getInstance().logGameEvent("Water Ability", 
                "FLOW activated - Phasing through obstacles for 3 seconds");
            player.activatePhasing(3.0);
            abilityCooldown = ABILITY_COOLDOWN_TIME;
        } else {
            GameLogger.getInstance().logGameEvent("Water Ability", 
                String.format("FLOW on cooldown (%.1fs remaining)", abilityCooldown));
        }
    }
    
    @Override
    public String getSpecialAbilityDescription() {
        return "FLOW: Phase through obstacles for 3 seconds (8s cooldown)";
    }
    
    /**
     * Gets the remaining cooldown time for the special ability.
     * 
     * @return Cooldown time in seconds
     */
    public double getAbilityCooldown() {
        return Math.max(0, abilityCooldown);
    }
}
