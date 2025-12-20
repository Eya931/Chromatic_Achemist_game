package com.chromatic.alchemist.model;

import com.chromatic.alchemist.logging.GameLogger;
import com.chromatic.alchemist.model.composite.*;

import java.util.Random;

/**
 * Level Generator
 * 
 * Generates game levels using the Composite pattern.
 * Creates hierarchical chamber structures with essences, obstacles, and power-ups.
 */

public class LevelGenerator {
    
    private static final Random random = new Random();
    
    public static Level generateLevel(int levelNumber, double width, double height, int difficulty) {
        GameLogger.getInstance().logInfo("Generating level " + levelNumber);
        
        switch (levelNumber) {
            case 1:
                return generateLevel1(width, height, difficulty);
            case 2:
                return generateLevel2(width, height, difficulty);
            case 3:
                return generateLevel3(width, height, difficulty);
            case 4:
                return generateLevel4(width, height, difficulty);
            case 5:
                return generateLevel5(width, height, difficulty);
            default:
                return generateLevel1(width, height, difficulty);
        }
    }
    
    /**
     * Level 1: Tutorial Chamber
     * Simple layout with basic essences, no obstacles.
     * Introduces Fire element essences.
     */

    private static Level generateLevel1(double width, double height, int difficulty) {
        // Create root chamber (simple)
        SimpleChamber root = new SimpleChamber("Initiation Chamber", 50, 50, width - 100, height - 100);
        root.setBackgroundColor("#1a1a2e");
        root.setBorderColor("#4a4aff");
        
        // Add essences - Fire colors (RED, ORANGE)
        int essenceCount = 8 + (difficulty * 2);
        addRandomEssences(root, essenceCount, new String[]{"RED", "ORANGE"}, 10);
        
        // Add one power-up
        root.addPowerUp(new PowerUp(PowerUp.PowerUpType.SPEED_BOOST, 
            root.getX() + root.getWidth() / 2,
            root.getY() + root.getHeight() / 2,
            10.0));
        
        Level level = new Level(1, "Initiation Chamber", root);
        level.setPlayerStartPosition(root.getX() + 100, root.getY() + root.getHeight() / 2);
        
        // Add recipe
        level.addRecipe(new Recipe("First Spark", "Collect 5 fire essences", 100)
            .addRequirement("RED", 3)
            .addRequirement("ORANGE", 2));
        
        return level;
    }
    
    /**
     * Level 2: Dual Chambers
     * Introduces compound chambers with Water essences.
     */
    
    private static Level generateLevel2(double width, double height, int difficulty) {
        // Create root compound chamber
        CompoundChamber root = new CompoundChamber("Elemental Duality", 30, 30, width - 60, height - 60);
        root.setBackgroundColor("#0f0f1e");
        root.setBorderColor("#6a6aff");
        
        // Create two sub-chambers
        double chamberWidth = (root.getWidth() - 60) / 2;
        double chamberHeight = root.getHeight() - 40;
        
        SimpleChamber fireChamber = new SimpleChamber("Fire Sanctum", 
            root.getX() + 20, root.getY() + 20, chamberWidth, chamberHeight);
        fireChamber.setBackgroundColor("#2a1a1a");
        fireChamber.setBorderColor("#ff4444");
        
        SimpleChamber waterChamber = new SimpleChamber("Water Sanctum",
            root.getX() + chamberWidth + 40, root.getY() + 20, chamberWidth, chamberHeight);
        waterChamber.setBackgroundColor("#1a1a2a");
        waterChamber.setBorderColor("#4444ff");
        
        // Add essences
        addRandomEssences(fireChamber, 6 + difficulty, new String[]{"RED", "ORANGE"}, 15);
        addRandomEssences(waterChamber, 6 + difficulty, new String[]{"BLUE", "CYAN"}, 15);
        
        // Add power-ups
        fireChamber.addPowerUp(new PowerUp(PowerUp.PowerUpType.SHIELD,
            fireChamber.getX() + fireChamber.getWidth() / 2,
            fireChamber.getY() + fireChamber.getHeight() / 2, 15.0));
        
        waterChamber.addPowerUp(new PowerUp(PowerUp.PowerUpType.MAGNET,
            waterChamber.getX() + waterChamber.getWidth() / 2,
            waterChamber.getY() + waterChamber.getHeight() / 2, 12.0));
        
        // Add sub-chambers to root
        root.addComponent(fireChamber);
        root.addComponent(waterChamber);
        
        Level level = new Level(2, "Elemental Duality", root);
        level.setPlayerStartPosition(root.getX() + root.getWidth() / 2, root.getY() + root.getHeight() / 2);
        
        // Add recipes
        level.addRecipe(new Recipe("Fire & Ice", "Master both fire and water", 200)
            .addRequirement("RED", 4)
            .addRequirement("BLUE", 4));
        
        return level;
    }
    
    /**
     * Level 3: Four Elements
     * All four element types with static obstacles.
     */

    private static Level generateLevel3(double width, double height, int difficulty) {
        CompoundChamber root = new CompoundChamber("Quadrant of Elements", 20, 20, width - 40, height - 40);
        root.setBackgroundColor("#0a0a15");
        root.setBorderColor("#8888ff");
        
        double quadWidth = (root.getWidth() - 30) / 2;
        double quadHeight = (root.getHeight() - 30) / 2;
        double padding = 10;
        
        // Fire quadrant (top-left)
        SimpleChamber fireQuad = new SimpleChamber("Fire Quadrant",
            root.getX() + padding, root.getY() + padding, quadWidth, quadHeight);
        fireQuad.setBackgroundColor("#2a1515");
        fireQuad.setBorderColor("#ff6600");
        addRandomEssences(fireQuad, 5 + difficulty, new String[]{"RED", "ORANGE"}, 20);
        addStaticObstacles(fireQuad, 2);
        
        // Water quadrant (top-right)
        SimpleChamber waterQuad = new SimpleChamber("Water Quadrant",
            root.getX() + quadWidth + padding * 2, root.getY() + padding, quadWidth, quadHeight);
        waterQuad.setBackgroundColor("#151525");
        waterQuad.setBorderColor("#0066ff");
        addRandomEssences(waterQuad, 5 + difficulty, new String[]{"BLUE", "CYAN"}, 20);
        addStaticObstacles(waterQuad, 2);
        
        // Earth quadrant (bottom-left)
        SimpleChamber earthQuad = new SimpleChamber("Earth Quadrant",
            root.getX() + padding, root.getY() + quadHeight + padding * 2, quadWidth, quadHeight);
        earthQuad.setBackgroundColor("#1a2a15");
        earthQuad.setBorderColor("#00aa00");
        addRandomEssences(earthQuad, 5 + difficulty, new String[]{"GREEN", "BROWN"}, 20);
        addStaticObstacles(earthQuad, 2);
        
        // Air quadrant (bottom-right)
        SimpleChamber airQuad = new SimpleChamber("Air Quadrant",
            root.getX() + quadWidth + padding * 2, root.getY() + quadHeight + padding * 2, quadWidth, quadHeight);
        airQuad.setBackgroundColor("#252525");
        airQuad.setBorderColor("#ffff00");
        addRandomEssences(airQuad, 5 + difficulty, new String[]{"WHITE", "YELLOW"}, 20);
        addStaticObstacles(airQuad, 2);
        
        // Add power-ups
        fireQuad.addPowerUp(new PowerUp(PowerUp.PowerUpType.SCORE_MULTIPLIER,
            fireQuad.getX() + fireQuad.getWidth() / 2, fireQuad.getY() + 50, 15.0));
        waterQuad.addPowerUp(new PowerUp(PowerUp.PowerUpType.RANGE_BOOST,
            waterQuad.getX() + waterQuad.getWidth() / 2, waterQuad.getY() + 50, 12.0));
        earthQuad.addPowerUp(new PowerUp(PowerUp.PowerUpType.SHIELD,
            earthQuad.getX() + earthQuad.getWidth() / 2, earthQuad.getY() + 50, 15.0));
        airQuad.addPowerUp(new PowerUp(PowerUp.PowerUpType.SPEED_BOOST,
            airQuad.getX() + airQuad.getWidth() / 2, airQuad.getY() + 50, 10.0));
        
        root.addComponent(fireQuad);
        root.addComponent(waterQuad);
        root.addComponent(earthQuad);
        root.addComponent(airQuad);
        
        Level level = new Level(3, "Quadrant of Elements", root);
        level.setPlayerStartPosition(root.getX() + root.getWidth() / 2, root.getY() + root.getHeight() / 2);
        
        level.addRecipe(new Recipe("Elemental Mastery", "Collect from all elements", 300)
            .addRequirement("RED", 3)
            .addRequirement("BLUE", 3)
            .addRequirement("GREEN", 3)
            .addRequirement("WHITE", 3));
        
        return level;
    }
    
    /**
     * Level 4: Nested Chambers
     * Introduces nested compound chambers with moving obstacles.
     */

    private static Level generateLevel4(double width, double height, int difficulty) {
        CompoundChamber root = new CompoundChamber("Alchemical Depths", 15, 15, width - 30, height - 30);
        root.setBackgroundColor("#050510");
        root.setBorderColor("#aa88ff");
        
        CompoundChamber innerCompound = new CompoundChamber("Inner Sanctum",
            root.getX() + 50, root.getY() + 50, root.getWidth() - 100, root.getHeight() - 100);
        innerCompound.setBackgroundColor("#0a0a1a");
        innerCompound.setBorderColor("#6666aa");
        
        double innerWidth = (innerCompound.getWidth() - 40) / 3;
        double innerHeight = innerCompound.getHeight() - 40;
        
        SimpleChamber chamber1 = new SimpleChamber("Transmutation Lab",
            innerCompound.getX() + 10, innerCompound.getY() + 20, innerWidth, innerHeight);
        chamber1.setBackgroundColor("#1a1020");
        chamber1.setBorderColor("#ff00ff");
        addRandomEssences(chamber1, 6 + difficulty, new String[]{"RED", "BLUE"}, 25);
        addMovingObstacles(chamber1, 1 + difficulty);
        
        SimpleChamber chamber2 = new SimpleChamber("Fusion Core",
            innerCompound.getX() + innerWidth + 20, innerCompound.getY() + 20, innerWidth, innerHeight);
        chamber2.setBackgroundColor("#102010");
        chamber2.setBorderColor("#00ff00");
        addRandomEssences(chamber2, 6 + difficulty, new String[]{"GREEN", "ORANGE"}, 25);
        addMovingObstacles(chamber2, 1 + difficulty);
        
        SimpleChamber chamber3 = new SimpleChamber("Ethereal Void",
            innerCompound.getX() + innerWidth * 2 + 30, innerCompound.getY() + 20, innerWidth, innerHeight);
        chamber3.setBackgroundColor("#202020");
        chamber3.setBorderColor("#ffffff");
        addRandomEssences(chamber3, 6 + difficulty, new String[]{"WHITE", "CYAN"}, 25);
        addMovingObstacles(chamber3, 1 + difficulty);
        
        chamber1.addPowerUp(new PowerUp(PowerUp.PowerUpType.MULTI_ABSORB,
            chamber1.getX() + chamber1.getWidth() / 2, chamber1.getY() + chamber1.getHeight() / 2, 8.0));
        chamber2.addPowerUp(new PowerUp(PowerUp.PowerUpType.MAGNET,
            chamber2.getX() + chamber2.getWidth() / 2, chamber2.getY() + chamber2.getHeight() / 2, 12.0));
        chamber3.addPowerUp(new PowerUp(PowerUp.PowerUpType.SHIELD,
            chamber3.getX() + chamber3.getWidth() / 2, chamber3.getY() + chamber3.getHeight() / 2, 15.0));
        
        innerCompound.addComponent(chamber1);
        innerCompound.addComponent(chamber2);
        innerCompound.addComponent(chamber3);
        root.addComponent(innerCompound);
        
        addRandomEssences(root, 4, new String[]{"YELLOW", "BROWN"}, 30);
        
        Level level = new Level(4, "Alchemical Depths", root);
        level.setPlayerStartPosition(root.getX() + 100, root.getY() + root.getHeight() / 2);
        
        level.addRecipe(new Recipe("Deep Transmutation", "Complete the alchemical sequence", 400)
            .addRequirement("RED", 4)
            .addRequirement("GREEN", 4)
            .addRequirement("WHITE", 4));
        
        return level;
    }
    
    /**
     * Level 5: Final Challenge
     * Complex nested structure with all obstacle types.
     */

    private static Level generateLevel5(double width, double height, int difficulty) {
        CompoundChamber root = new CompoundChamber("Philosopher's Nexus", 10, 10, width - 20, height - 20);
        root.setBackgroundColor("#000005");
        root.setBorderColor("#ffcc00");
        
        CompoundChamber central = new CompoundChamber("Grand Athanor",
            root.getX() + root.getWidth() / 4, root.getY() + root.getHeight() / 4,
            root.getWidth() / 2, root.getHeight() / 2);
        central.setBackgroundColor("#100510");
        central.setBorderColor("#ff8800");
        
        SimpleChamber sanctum = new SimpleChamber("Philosopher's Stone",
            central.getX() + central.getWidth() / 4, central.getY() + central.getHeight() / 4,
            central.getWidth() / 2, central.getHeight() / 2);
        sanctum.setBackgroundColor("#201010");
        sanctum.setBorderColor("#ffff00");
        
        addRandomEssences(sanctum, 5 + difficulty * 2, new String[]{"YELLOW", "ORANGE"}, 50);
        addPulsingObstacles(sanctum, difficulty);
        
        sanctum.addPowerUp(new PowerUp(PowerUp.PowerUpType.SCORE_MULTIPLIER,
            sanctum.getX() + sanctum.getWidth() / 2, sanctum.getY() + sanctum.getHeight() / 2, 20.0));
        
        central.addComponent(sanctum);
        
        addRandomEssences(central, 8 + difficulty, new String[]{"RED", "BLUE", "GREEN", "WHITE"}, 30);
        addRotatingObstacles(central, difficulty);
        
        root.addComponent(central);
        
        double cornerSize = root.getWidth() / 5;
        
        SimpleChamber tlCorner = new SimpleChamber("Fire Alcove",
            root.getX() + 20, root.getY() + 20, cornerSize, cornerSize);
        tlCorner.setBackgroundColor("#200505");
        tlCorner.setBorderColor("#ff0000");
        addRandomEssences(tlCorner, 4, new String[]{"RED"}, 25);
        addStaticObstacles(tlCorner, 1);
        
        SimpleChamber trCorner = new SimpleChamber("Water Alcove",
            root.getX() + root.getWidth() - cornerSize - 20, root.getY() + 20, cornerSize, cornerSize);
        trCorner.setBackgroundColor("#050520");
        trCorner.setBorderColor("#0000ff");
        addRandomEssences(trCorner, 4, new String[]{"BLUE"}, 25);
        addStaticObstacles(trCorner, 1);
        
        SimpleChamber blCorner = new SimpleChamber("Earth Alcove",
            root.getX() + 20, root.getY() + root.getHeight() - cornerSize - 20, cornerSize, cornerSize);
        blCorner.setBackgroundColor("#052005");
        blCorner.setBorderColor("#00ff00");
        addRandomEssences(blCorner, 4, new String[]{"GREEN"}, 25);
        addStaticObstacles(blCorner, 1);
        
        SimpleChamber brCorner = new SimpleChamber("Air Alcove",
            root.getX() + root.getWidth() - cornerSize - 20, 
            root.getY() + root.getHeight() - cornerSize - 20, cornerSize, cornerSize);
        brCorner.setBackgroundColor("#202020");
        brCorner.setBorderColor("#ffffff");
        addRandomEssences(brCorner, 4, new String[]{"WHITE"}, 25);
        addStaticObstacles(brCorner, 1);
        
        tlCorner.addPowerUp(new PowerUp(PowerUp.PowerUpType.SPEED_BOOST,
            tlCorner.getX() + cornerSize / 2, tlCorner.getY() + cornerSize / 2, 10.0));
        trCorner.addPowerUp(new PowerUp(PowerUp.PowerUpType.MAGNET,
            trCorner.getX() + cornerSize / 2, trCorner.getY() + cornerSize / 2, 12.0));
        blCorner.addPowerUp(new PowerUp(PowerUp.PowerUpType.SHIELD,
            blCorner.getX() + cornerSize / 2, blCorner.getY() + cornerSize / 2, 15.0));
        brCorner.addPowerUp(new PowerUp(PowerUp.PowerUpType.MULTI_ABSORB,
            brCorner.getX() + cornerSize / 2, brCorner.getY() + cornerSize / 2, 8.0));
        
        root.addComponent(tlCorner);
        root.addComponent(trCorner);
        root.addComponent(blCorner);
        root.addComponent(brCorner);
        
        addRandomEssences(root, 6, new String[]{"CYAN", "BROWN", "YELLOW"}, 35);
        
        Level level = new Level(5, "Philosopher's Nexus", root);
        level.setPlayerStartPosition(root.getX() + 80, root.getY() + root.getHeight() / 2);
        
        level.addRecipe(new Recipe("Philosopher's Stone", "Complete the ultimate transmutation", 1000)
            .addRequirement("RED", 5)
            .addRequirement("BLUE", 5)
            .addRequirement("GREEN", 5)
            .addRequirement("WHITE", 5)
            .addRequirement("YELLOW", 5));
        
        return level;
    }
    
    // ==================== HELPER METHODS ====================
    
    private static void addRandomEssences(ChamberComponent chamber, int count, String[] colors, int pointValue) {
        double margin = 40;
        for (int i = 0; i < count; i++) {
            String color = colors[random.nextInt(colors.length)];
            double x = chamber.getX() + margin + random.nextDouble() * (chamber.getWidth() - margin * 2);
            double y = chamber.getY() + margin + random.nextDouble() * (chamber.getHeight() - margin * 2);
            chamber.addEssence(new EssenceParticle(color, x, y, pointValue));
        }
    }
    
    private static void addStaticObstacles(ChamberComponent chamber, int count) {
        double margin = 60;
        for (int i = 0; i < count; i++) {
            double x = chamber.getX() + margin + random.nextDouble() * (chamber.getWidth() - margin * 2 - 60);
            double y = chamber.getY() + margin + random.nextDouble() * (chamber.getHeight() - margin * 2 - 30);
            chamber.addObstacle(new Obstacle(Obstacle.ObstacleType.STATIC, x, y, 60, 30));
        }
    }
    
    private static void addMovingObstacles(ChamberComponent chamber, int count) {
        double margin = 80;
        for (int i = 0; i < count; i++) {
            double x = chamber.getX() + margin;
            double y = chamber.getY() + margin + random.nextDouble() * (chamber.getHeight() - margin * 2);
            Obstacle obstacle = new Obstacle(Obstacle.ObstacleType.MOVING, x, y, 40, 40);
            obstacle.setMovementPath(
                chamber.getX() + chamber.getWidth() - margin - 40,
                y,
                80 + random.nextDouble() * 60
            );
            chamber.addObstacle(obstacle);
        }
    }
    
    private static void addRotatingObstacles(ChamberComponent chamber, int count) {
        double margin = 80;
        for (int i = 0; i < count; i++) {
            double x = chamber.getX() + margin + random.nextDouble() * (chamber.getWidth() - margin * 2 - 50);
            double y = chamber.getY() + margin + random.nextDouble() * (chamber.getHeight() - margin * 2 - 50);
            Obstacle obstacle = new Obstacle(Obstacle.ObstacleType.ROTATING, x, y, 50, 15);
            obstacle.setRotationSpeed(60 + random.nextDouble() * 60);
            chamber.addObstacle(obstacle);
        }
    }
    
    private static void addPulsingObstacles(ChamberComponent chamber, int count) {
        double margin = 60;
        for (int i = 0; i < count; i++) {
            double x = chamber.getX() + margin + random.nextDouble() * (chamber.getWidth() - margin * 2 - 40);
            double y = chamber.getY() + margin + random.nextDouble() * (chamber.getHeight() - margin * 2 - 40);
            chamber.addObstacle(new Obstacle(Obstacle.ObstacleType.PULSING, x, y, 40, 40));
        }
    }
}
