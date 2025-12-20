package com.chromatic.alchemist.model;

import com.chromatic.alchemist.model.composite.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Level class representing a game level.
 * Uses the Composite pattern with ChamberComponent as the structure.
 */

public class Level {
    
    private final int levelNumber;
    private final String name;
    private final ChamberComponent rootChamber;
    private final List<Recipe> recipes;
    private double playerStartX;
    private double playerStartY;
    private boolean completed;
    
    public Level(int levelNumber, String name, ChamberComponent rootChamber) {
        this.levelNumber = levelNumber;
        this.name = name;
        this.rootChamber = rootChamber;
        this.recipes = new ArrayList<>();
        this.playerStartX = rootChamber.getX() + rootChamber.getWidth() / 2;
        this.playerStartY = rootChamber.getY() + rootChamber.getHeight() / 2;
        this.completed = false;
    }
    
    public void update(double deltaTime) {
        rootChamber.update(deltaTime);
    }
    
    public boolean isCompleted() {
        return rootChamber.isCompleted();
    }
    
    public double getCompletionPercentage() {
        return rootChamber.getCompletionPercentage();
    }
    
    public void addRecipe(Recipe recipe) {
        recipes.add(recipe);
    }
    
    
    public int getLevelNumber() { return levelNumber; }
    public String getName() { return name; }
    public ChamberComponent getRootChamber() { return rootChamber; }
    public List<Recipe> getRecipes() { return new ArrayList<>(recipes); }
    public double getPlayerStartX() { return playerStartX; }
    public double getPlayerStartY() { return playerStartY; }
    
    public void setPlayerStartPosition(double x, double y) {
        this.playerStartX = x;
        this.playerStartY = y;
    }
    
    public int getTotalEssences() { return rootChamber.getTotalEssenceCount(); }
    public int getRemainingEssences() { return rootChamber.getRemainingEssenceCount(); }
}
