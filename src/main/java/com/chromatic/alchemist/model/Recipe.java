package com.chromatic.alchemist.model;

import com.chromatic.alchemist.model.entity.Player;

import java.util.HashMap;
import java.util.Map;

/*
 * Recipe class representing an alchemical recipe objective.
 * Players complete recipes by collecting specific combinations of essences.
 */

public class Recipe {
    
    private final String name;
    private final String description;
    private final Map<String, Integer> requiredEssences; 
    private final int bonusPoints;
    private boolean completed;
    
    public Recipe(String name, String description, int bonusPoints) {
        this.name = name;
        this.description = description;
        this.requiredEssences = new HashMap<>();
        this.bonusPoints = bonusPoints;
        this.completed = false;
    }
    
    public Recipe addRequirement(String color, int count) {
        requiredEssences.put(color.toUpperCase(), count);
        return this;
    }
    
    public boolean checkCompletion(Player player) {

        int totalRequired = requiredEssences.values().stream()
            .mapToInt(Integer::intValue).sum();
        return player.getEssencesCollected() >= totalRequired;
    }
    
    
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Map<String, Integer> getRequiredEssences() { return new HashMap<>(requiredEssences); }
    public int getBonusPoints() { return bonusPoints; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}
