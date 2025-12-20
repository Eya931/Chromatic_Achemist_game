package com.chromatic.alchemist;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.chromatic.alchemist.model.Recipe;
import com.chromatic.alchemist.model.entity.Player;

public class RecipeTest {
    @Test
    void testRecipeRequirements() {
        Recipe recipe = new Recipe("Test Recipe", "Desc", 100);
        recipe.addRequirement("RED", 2).addRequirement("BLUE", 1);
        assertEquals(2, recipe.getRequiredEssences().get("RED"));
        assertEquals(1, recipe.getRequiredEssences().get("BLUE"));
    }

    @Test
    void testRecipeCompletion() {
        Recipe recipe = new Recipe("Test Recipe", "Desc", 100);
        recipe.addRequirement("RED", 1);
        Player player = new Player(0, 0);
        // Simulate player collecting essences (method may need to be adapted)
        // For now, just check the method returns false by default
        assertFalse(recipe.checkCompletion(player));
    }
}
