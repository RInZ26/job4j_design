package ru.job4j.lsp.food;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WareHouseTest {

    @Test
    public void whenIsSuitableIsTrue() {
        Food bestFood = new Food("C", LocalDateTime.now(), LocalDateTime.now()
                                                                        .plusDays(
                                                                                10),
                                 0, 0);
        assertTrue(new WareHouse().isSuitableFood(bestFood));
    }

    @Test
    public void whenIsSuitableIsFalse() {
        Food oldFood = new Food("A", LocalDateTime.now()
                                                  .minusDays(5),
                                LocalDateTime.now()
                                             .minusDays(2), 0, 0);
        assertFalse(new WareHouse().isSuitableFood(oldFood));
    }

    @Test
    public void whenFoodWasAdded() {
        Food bestFood = new Food("C", LocalDateTime.now(), LocalDateTime.now()
                                                                        .plusDays(
                                                                                10),
                                 0, 0);
        FoodSupplier wareHouse = new WareHouse();
        wareHouse.addFood(bestFood);
        assertTrue(wareHouse.getAllFood().contains(bestFood));
    }

    @Test
    public void whenFoodWasNotAdded() {
        Food oldFood = new Food("A", LocalDateTime.now()
                                                  .minusDays(5),
                                LocalDateTime.now()
                                             .minusDays(2), 0, 0);
        FoodSupplier wareHouse = new WareHouse();
        wareHouse.addFood(oldFood);
        assertFalse(wareHouse.getAllFood().contains(oldFood));
    }
}