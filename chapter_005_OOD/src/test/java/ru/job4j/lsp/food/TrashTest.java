package ru.job4j.lsp.food;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TrashTest {

    @Test
    public void whenIsSuitableIsFalse() {
        Food bestFood = new Food("C", LocalDateTime.now(), LocalDateTime.now()
                                                                        .plusDays(
                                                                                10),
                                 0, 0);
        assertFalse(new Trash().isSuitableFood(bestFood));
    }

    @Test
    public void whenIsSuitableIsTrue() {
        Food oldFood = new Food("A", LocalDateTime.now()
                                                  .minusDays(5),
                                LocalDateTime.now()
                                             .minusDays(2), 0, 0);
        assertTrue(new Trash().isSuitableFood(oldFood));
    }

    @Test
    public void whenFoodWasNotAdded() {
        Food bestFood = new Food("C", LocalDateTime.now(), LocalDateTime.now()
                                                                        .plusDays(
                                                                                10),
                                 0, 0);
        FoodSupplier trash = new Trash();
        trash.addFood(bestFood);
        assertFalse(trash.getAllFood().contains(bestFood));
    }

    @Test
    public void whenFoodWasAdded() {
        Food oldFood = new Food("A", LocalDateTime.now()
                                                  .minusDays(5),
                                LocalDateTime.now()
                                             .minusDays(2), 0, 0);
        FoodSupplier trash = new Trash();
        trash.addFood(oldFood);
        assertTrue(trash.getAllFood().contains(oldFood));
    }
}