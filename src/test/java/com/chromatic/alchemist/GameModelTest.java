package com.chromatic.alchemist;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.chromatic.alchemist.model.GameModel;
import com.chromatic.alchemist.model.state.GameState;

public class GameModelTest {
    @Test
    void testInitialState() {
        GameModel model = new GameModel(800, 600);
        assertEquals(GameState.MENU, model.getCurrentState(), "Initial state should be MENU");
    }

    @Test
    void testStartGame() {
        GameModel model = new GameModel(800, 600);
        model.startGame();
        assertEquals(GameState.PLAYING, model.getCurrentState(), "State should be PLAYING after startGame");
    }

    @Test
    void testPauseAndResume() {
        GameModel model = new GameModel(800, 600);
        model.startGame();
        model.pauseGame();
        assertEquals(GameState.PAUSED, model.getCurrentState(), "State should be PAUSED after pauseGame");
        model.resumeGame();
        assertEquals(GameState.PLAYING, model.getCurrentState(), "State should be PLAYING after resumeGame");
    }
}
