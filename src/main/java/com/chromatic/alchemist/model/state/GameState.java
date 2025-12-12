package com.chromatic.alchemist.model.state;

import com.chromatic.alchemist.logging.GameLogger;

/**
 * STATE PATTERN - Game State Interface
 * 
 * Defines the interface for all game states.
 * The game can be in one of several states:
 * - MENU: Main menu displayed
 * - PLAYING: Active gameplay
 * - PAUSED: Game paused
 * - GAME_OVER: Player lost
 * - VICTORY: Player won
 * - OPTIONS: Options menu displayed
 */
public enum GameState {
    MENU("Main Menu"),
    PLAYING("Playing"),
    PAUSED("Paused"),
    GAME_OVER("Game Over"),
    VICTORY("Victory"),
    OPTIONS("Options");
    
    private final String displayName;
    
    GameState(String displayName) {
        this.displayName = displayName;
    }
    
    /**
     * Gets the display name for this state.
     * 
     * @return Human-readable state name
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Logs a transition from this state to another.
     * 
     * @param newState The state being transitioned to
     */
    public void logTransitionTo(GameState newState) {
        GameLogger.getInstance().logGameStateChange(this.name(), newState.name());
    }
}
