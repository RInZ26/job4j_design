package ru.job4j.lsp.food;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ShopTest {

    @Test
    public void whenAddShopIsDeclinedByLowBound() {
        Food bestFood = new Food("C", LocalDateTime.now(), LocalDateTime.now()
                                                                        .plusDays(
                                                                                10),
                                 0, 0);
        Shop shop = new Shop();
        assertFalse(shop.addFood(bestFood));
        assertFalse(shop.getAllFood()
                        .contains(bestFood));

    }

    @Test
    public void whenAddShopIsDeclinedByCriticalBound() {
        Food oldFood = new Food("A", LocalDateTime.now()
                                                  .minusDays(5),
                                LocalDateTime.now()
                                             .minusDays(2), 0, 0);
        Shop shop = new Shop();
        assertFalse(shop.addFood(oldFood));
        assertFalse(shop.getAllFood()
                        .contains(oldFood));
    }

    @Test
    public void whenAddShopIsApproved() {
        Food normalFood = new Food("B", LocalDateTime.now()
                                                     .minusDays(90),
                                   LocalDateTime.now()
                                                .plusDays(10), 0, 0);
        Shop shop = new Shop();
        assertTrue(shop.addFood(normalFood));
        assertTrue(shop.getAllFood()
                        .contains(normalFood));
    }
}