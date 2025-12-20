package com.chromatic.alchemist;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.chromatic.alchemist.model.Level;
import com.chromatic.alchemist.model.composite.SimpleChamber;

public class LevelTest {
    @Test
    void testLevelInitialization() {
        SimpleChamber root = new SimpleChamber("Test Chamber", 0, 0, 100, 100);
        Level level = new Level(1, "Test Level", root);
        assertEquals(1, level.getLevelNumber());
        assertEquals("Test Level", level.getName());
        assertEquals(root, level.getRootChamber());
    }

    @Test
    void testLevelCompletion() {
        SimpleChamber root = new SimpleChamber("Test Chamber", 0, 0, 100, 100);
        Level level = new Level(1, "Test Level", root);
        // Par défaut, le niveau est complété (isCompleted() == true)
        assertTrue(level.isCompleted());
    }
}
