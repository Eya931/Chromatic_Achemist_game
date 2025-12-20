package com.chromatic.alchemist.view;

import com.chromatic.alchemist.controller.GameController;
import com.chromatic.alchemist.logging.GameLogger;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Main JavaFX Application
 * 
 * Entry point for the JavaFX application.
 * Initializes the game window and manages scenes.
 */

public class GameApplication extends Application {
    
    // Game dimensions
    public static final double GAME_WIDTH = 1024;
    public static final double GAME_HEIGHT = 768;
    
    private Stage primaryStage;
    private GameController controller;
    private GameView gameView;
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        
        GameLogger.getInstance().logInfo("JavaFX Application starting...");
        
        // Initialize controller
        controller = new GameController(GAME_WIDTH, GAME_HEIGHT);
        
        // Initialize view
        gameView = new GameView(primaryStage, controller, GAME_WIDTH, GAME_HEIGHT);
        controller.setView(gameView);
        
        // Configure stage
        primaryStage.setTitle("Chromatic Alchemist");
        primaryStage.setResizable(false);
        
        gameView.showMenuScene();
        
        // Size the stage to fit the scene content
        primaryStage.sizeToScene();
        
        // Center the window on screen
        primaryStage.centerOnScreen();
        
        controller.startGameLoop();
        
        // Handle window close
        primaryStage.setOnCloseRequest(event -> {
            GameLogger.getInstance().logInfo("Window close requested");
            controller.stopGameLoop();
        });
        
        primaryStage.show();
        
        GameLogger.getInstance().logInfo("JavaFX Application started successfully");
    }
    
    @Override
    public void stop() {
        GameLogger.getInstance().logInfo("JavaFX Application stopping...");
        if (controller != null) {
            controller.stopGameLoop();
        }
    }
    
    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
