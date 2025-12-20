package com.chromatic.alchemist;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.chromatic.alchemist.model.composite.SimpleChamber;

public class SimpleChamberTest {
    @Test
    void testChamberProperties() {
        SimpleChamber chamber = new SimpleChamber("Chamber1", 10, 20, 100, 200);
        assertEquals("Chamber1", chamber.getName());
        assertEquals(10, chamber.getX());
        assertEquals(20, chamber.getY());
        assertEquals(100, chamber.getWidth());
        assertEquals(200, chamber.getHeight());
    }

    @Test
    void testCompletionPercentage() {
        SimpleChamber chamber = new SimpleChamber("Chamber1", 0, 0, 100, 100);
        // By default, should be 0 or initial value
        assertEquals(100.0, chamber.getCompletionPercentage(), 0.0001);}
}
