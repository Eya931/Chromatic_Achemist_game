package com.chromatic.alchemist.controller;

import com.chromatic.alchemist.logging.GameLogger;
import com.chromatic.alchemist.model.GameModel;
import com.chromatic.alchemist.model.observer.GameEvent;
import com.chromatic.alchemist.model.observer.GameEventManager;
import com.chromatic.alchemist.model.observer.GameObserver;
import com.chromatic.alchemist.model.state.GameState;
import com.chromatic.alchemist.view.GameView;

import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;


public class GameController implements GameObserver {
    
    private final GameModel model;
    private GameView view;
    private AnimationTimer gameLoop;
    private long lastUpdateTime;
    private boolean initialized;
    
    private static final double TARGET_FPS = 60.0;
    private static final double FRAME_TIME = 1.0 / TARGET_FPS;
    private double accumulator;
    

    public GameController(double width, double height) {
        this.model = new GameModel(width, height);
        this.initialized = false;
        this.accumulator = 0;
        
        GameEventManager.getInstance().subscribe(
            new GameEvent.EventType[]{
                GameEvent.EventType.GAME_OVER,
                GameEvent.EventType.GAME_VICTORY,
                GameEvent.EventType.LEVEL_COMPLETED
            },
            this
        );
        
        GameLogger.getInstance().logInfo("GameController initialized");
    }
    
    
    public void setView(GameView view) {
        this.view = view;
        this.initialized = true;
    }
    
    
    public void startGameLoop() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
        
        lastUpdateTime = System.nanoTime();
        
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
            
                double deltaTime = (now - lastUpdateTime) / 1_000_000_000.0;
                lastUpdateTime = now;
                
                if (deltaTime > 0.25) {
                    deltaTime = 0.25;
                }
                
                accumulator += deltaTime;
                while (accumulator >= FRAME_TIME) {
                    update(FRAME_TIME);
                    accumulator -= FRAME_TIME;
                }
                
                render();
            }
        };
        
        gameLoop.start();
        GameLogger.getInstance().logInfo("Game loop started");
    }
    
    
    public void stopGameLoop() {
        if (gameLoop != null) {
            gameLoop.stop();
            GameLogger.getInstance().logInfo("Game loop stopped");
        }
    }
    
    
    private void update(double deltaTime) {
        model.update(deltaTime);
    }
    
    private void render() {
        if (view != null && initialized) {
            view.render(model);
        }
    }
    
    // ==================== GAME CONTROL ====================
    
   
    public void newGame() {
        GameLogger.getInstance().logInfo("Starting new game...");
        model.startGame();
        if (view != null) {
            view.showGameScene();
        }
    }
    
    public void pauseGame() {
        model.pauseGame();
        if (view != null) {
            view.showPauseOverlay();
        }
    }

    public void resumeGame() {
        model.resumeGame();
        if (view != null) {
            view.hidePauseOverlay();
        }
    }
    

    public void returnToMenu() {
        model.pauseGame();
        if (view != null) {
            view.showMenuScene();
        }
    }
    
    public void showOptions() {
        if (view != null) {
            view.showOptionsScene();
        }
    }
    
    public void exitGame() {
        stopGameLoop();
        GameLogger.getInstance().logInfo("Game exiting...");
        System.exit(0);
    }
    
    // ==================== INPUT HANDLING ====================
    

    public void handleKeyPressed(KeyCode code) {
        if (model.getCurrentState() == GameState.PLAYING) {
            switch (code) {
                case W:
                case UP:
                    model.playerMoveUp(true);
                    break;
                case S:
                case DOWN:
                    model.playerMoveDown(true);
                    break;
                case A:
                case LEFT:
                    model.playerMoveLeft(true);
                    break;
                case D:
                case RIGHT:
                    model.playerMoveRight(true);
                    break;
                    
                case DIGIT1:
                case NUMPAD1:
                    model.playerTransmuteToFire();
                    break;
                case DIGIT2:
                case NUMPAD2:
                    model.playerTransmuteToWater();
                    break;
                case DIGIT3:
                case NUMPAD3:
                    model.playerTransmuteToEarth();
                    break;
                case DIGIT4:
                case NUMPAD4:
                    model.playerTransmuteToAir();
                    break;
                    
                case SPACE:
                    model.playerUseAbility();
                    break;
                    
                case ESCAPE:
                case P:
                    pauseGame();
                    break;
                    
                default:
                    break;
            }
        } else if (model.getCurrentState() == GameState.PAUSED) {
            if (code == KeyCode.ESCAPE || code == KeyCode.P) {
                resumeGame();
            }
        }
    }
    
    public void handleKeyReleased(KeyCode code) {
        if (model.getCurrentState() == GameState.PLAYING) {
            switch (code) {
                case W:
                case UP:
                    model.playerMoveUp(false);
                    break;
                case S:
                case DOWN:
                    model.playerMoveDown(false);
                    break;
                case A:
                case LEFT:
                    model.playerMoveLeft(false);
                    break;
                case D:
                case RIGHT:
                    model.playerMoveRight(false);
                    break;
                default:
                    break;
            }
        }
    }
    
    // ==================== SETTINGS ====================
    
    public void setDifficulty(int difficulty) {
        model.setDifficulty(difficulty);
        GameLogger.getInstance().logInfo("Difficulty set to " + difficulty);
    }
    
    // ==================== OBSERVER ====================
    
    @Override
    public void onGameEvent(GameEvent event) {
        switch (event.getType()) {
            case GAME_OVER:
                if (view != null) {
                    view.showGameOverScene(
                        event.getInt("score", 0),
                        event.getInt("level", 1)
                    );
                }
                break;
            case GAME_VICTORY:
                if (view != null) {
                    view.showVictoryScene(event.getInt("score", 0));
                }
                break;
            case LEVEL_COMPLETED:
                break;
            default:
                break;
        }
    }
    
    // ==================== GETTERS ====================
    
    public GameModel getModel() {
        return model;
    }
    
    public GameState getCurrentState() {
        return model.getCurrentState();
    }
}
