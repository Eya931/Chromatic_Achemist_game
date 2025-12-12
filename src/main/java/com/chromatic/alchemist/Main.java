package com.chromatic.alchemist;

import com.chromatic.alchemist.controller.GameController;
import com.chromatic.alchemist.logging.GameLogger;
import com.chromatic.alchemist.view.GameApplication;
import javafx.application.Application;

/**
 * Main entry point for Chromatic Alchemist game.
 * 
 * This game implements four design patterns:
 * - State Pattern: For player elemental states (Fire, Water, Earth, Air)
 * - Decorator Pattern: For power-up mutations that enhance player abilities
 * - Composite Pattern: For hierarchical level structure (Chambers containing sub-chambers)
 * - Observer Pattern: For event system notifying UI, logging, and game systems
 * 
 * @author Chromatic Alchemist Team
 * @version 1.0.0
 */
public class Main {
    
    /**
     * Application entry point.
     * Initializes the logging system and launches the JavaFX application.
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // Initialize the logging system
        GameLogger.getInstance().logInfo("===========================================");
        GameLogger.getInstance().logInfo("Chromatic Alchemist Game Starting...");
        GameLogger.getInstance().logInfo("Version: 1.0.0");
        GameLogger.getInstance().logInfo("===========================================");
        
        // Launch JavaFX application
        Application.launch(GameApplication.class, args);
    }
}
