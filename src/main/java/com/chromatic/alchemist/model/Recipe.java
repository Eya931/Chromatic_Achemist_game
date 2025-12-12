package com.chromatic.alchemist.model;

import com.chromatic.alchemist.model.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Recipe class representing an alchemical recipe objective.
 * Players complete recipes by collecting specific combinations of essences.
 */
public class Recipe {
    
    private final String name;
    private final String description;
    private final Map<String, Integer> requiredEssences; // Color -> Count
    private final int bonusPoints;
    private boolean completed;
    
    /**
     * Creates a new recipe.
     * 
     * @param name Recipe name
     * @param description Recipe description
     * @param bonusPoints Bonus points awarded on completion
     */
    public Recipe(String name, String description, int bonusPoints) {
        this.name = name;
        this.description = description;
        this.requiredEssences = new HashMap<>();
        this.bonusPoints = bonusPoints;
        this.completed = false;
    }
    
    /**
     * Adds a required essence to the recipe.
     * 
     * @param color Essence color
     * @param count Required count
     * @return This recipe (for chaining)
     */
    public Recipe addRequirement(String color, int count) {
        requiredEssences.put(color.toUpperCase(), count);
        return this;
    }
    
    /**
     * Checks if the recipe is completed based on player's collected essences.
     * For simplicity, this checks if the player has collected enough total essences.
     * In a full implementation, you would track individual essence colors collected.
     * 
     * @param player The player
     * @return true if recipe requirements are met
     */
    public boolean checkCompletion(Player player) {
        // Simplified: check if player has collected enough essences
        int totalRequired = requiredEssences.values().stream()
            .mapToInt(Integer::intValue).sum();
        return player.getEssencesCollected() >= totalRequired;
    }
    
    // Getters and setters
    
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Map<String, Integer> getRequiredEssences() { return new HashMap<>(requiredEssences); }
    public int getBonusPoints() { return bonusPoints; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}
