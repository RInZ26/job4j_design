package ru.job4j.lsp.food;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class ControlQualityTest {
    @Test
    public void whenDifferenceIs10Days() {
        LocalDateTime foodCreated = LocalDateTime.now()
                                                 .minusDays(90);
        LocalDateTime foodExpired = LocalDateTime.now()
                                                 .plusDays(10);
        var expected = ControlQuality.consumeQuality(
                new Food(null, foodCreated, foodExpired, 0, 0));
        assertThat(expected, is(0.90d));
    }

    @Test
    public void whenDifferenceIs94days() {
        LocalDateTime foodCreated = LocalDateTime.now()
                                                 .minusDays(36);
        LocalDateTime foodExpired = LocalDateTime.now()
                                                 .plusDays(59);
        var expected = ControlQuality.consumeQuality(
                new Food(null, foodCreated, foodExpired, 0, 0));
        assertThat(expected, is(0.38d));
    }

    @Test //expectedBeforeRoundIsLine 0.357 -> rounded 0.36
    public void whenResultShouldBeRounded() {
        LocalDateTime foodCreated = LocalDateTime.now()
                                                 .minusDays(5);
        LocalDateTime foodExpired = LocalDateTime.now()
                                                 .plusDays(9);
        var expected = ControlQuality.consumeQuality(
                new Food(null, foodCreated, foodExpired, 0, 0));
        assertThat(expected, is(0.36d));
    }

    @Test
    public void whenSplitWithRemoveIsFalse() {
        FoodSupplier shop = new Shop();
        Food bestFood = new Food("C", LocalDateTime.now(), LocalDateTime.now()
                                                                        .plusDays(
                                                                                10),
                                 0, 0);
        Food oldFood = new Food("A", LocalDateTime.now()
                                                  .minusDays(5),
                                LocalDateTime.now()
                                             .minusDays(2), 0, 0);
        Food normalFood = new Food("B", LocalDateTime.now()
                                                     .minusDays(90),
                                   LocalDateTime.now()
                                                .plusDays(10), 0, 0);
        List<Food> foodList = new ArrayList<>() {
            {
                add(oldFood);
                add(normalFood);
                add(bestFood);
            }
        };
        assertFalse(ControlQuality.splitFoodRemove(foodList, shop));
        assertTrue(foodList.contains(oldFood));
        assertTrue(foodList.contains(bestFood));
        assertFalse(foodList.contains(normalFood));
    }

    @Test
    public void whenSplitFoodSoftIsFalse() {
        FoodSupplier shop = new Shop();
        Food oldFood = new Food("A", LocalDateTime.now()
                                                  .minusDays(5),
                                LocalDateTime.now()
                                             .minusDays(2), 0, 0);
        Food normalFood = new Food("B", LocalDateTime.now()
                                                     .minusDays(90),
                                   LocalDateTime.now()
                                                .plusDays(10), 0, 0);
        List<Food> foodList = new ArrayList<>() {
            {
                add(oldFood);
                add(normalFood);
            }
        };
        assertFalse(ControlQuality.splitFoodSoft(foodList, shop));
    }

    @Test
    public void whenSplitFoodSoftIsTrue() {
        FoodSupplier shop = new Shop();
        Food normalFood = new Food("B", LocalDateTime.now()
                                                     .minusDays(90),
                                   LocalDateTime.now()
                                                .plusDays(10), 0, 0);
        List<Food> foodList = new ArrayList<>() {
            {
                add(normalFood);
                add(normalFood);
            }
        };
        assertTrue(ControlQuality.splitFoodSoft(foodList, shop));
    }

    @Test
    public void whenSplitFoodRemoveWithFewFoodSuppliersIsTrue() {
        Food bestFood = new Food("C", LocalDateTime.now(), LocalDateTime.now()
                                                                        .plusDays(
                                                                                10),
                                 0, 0);
        Food oldFood = new Food("A", LocalDateTime.now()
                                                  .minusDays(5),
                                LocalDateTime.now()
                                             .minusDays(2), 0, 0);
        Food normalFood = new Food("B", LocalDateTime.now()
                                                     .minusDays(90),
                                   LocalDateTime.now()
                                                .plusDays(10), 0, 0);
        List<Food> foodList = new ArrayList<>() {
            {
                add(oldFood);
                add(bestFood);
                add(normalFood);
            }
        };
        FoodSupplier shop = new Shop();
        FoodSupplier trash = new Trash();
        FoodSupplier warHouse = new WareHouse();
        assertTrue(ControlQuality.splitFoodRemove(foodList, shop, trash,
                                                  warHouse));
        assertThat(foodList.size(), is(0));
        assertThat(shop.getAllFood()
                       .size(), is(1));
        assertThat(trash.getAllFood()
                        .size(), is(1));
        assertThat(warHouse.getAllFood()
                           .size(), is(1));
    }

    @Test
    public void whenSplitFoodRemoveWithFewFoodSuppliersIsFalse() {
        Food bestFood = new Food("C", LocalDateTime.now(), LocalDateTime.now()
                                                                        .plusDays(
                                                                                10),
                                 0, 0);
        Food oldFood = new Food("A", LocalDateTime.now()
                                                  .minusDays(5),
                                LocalDateTime.now()
                                             .minusDays(2), 0, 0);
        Food normalFood = new Food("B", LocalDateTime.now()
                                                     .minusDays(90),
                                   LocalDateTime.now()
                                                .plusDays(10), 0, 0);
        List<Food> foodList = new ArrayList<>() {
            {
                add(oldFood);
                add(bestFood);
                add(normalFood);
            }
        };
        FoodSupplier shop = new Shop();
        FoodSupplier trash = new Trash();
        assertFalse(ControlQuality.splitFoodRemove(foodList, shop, trash));
        assertThat(foodList.size(), is(1));
        assertThat(shop.getAllFood()
                       .size(), is(1));
        assertThat(trash.getAllFood()
                        .size(), is(1));
    }

    @Test
    public void whenCollectionDoesNotSupportRemoveOperation() {
        Food normalFood = new Food("B", LocalDateTime.now()
                                                     .minusDays(90),
                                   LocalDateTime.now()
                                                .plusDays(10), 0, 0);
        FoodSupplier shop = new Shop();
        List<Food> immutableList = Arrays.asList(normalFood);
        ControlQuality.splitFoodRemove(immutableList, shop);
        assertThat(immutableList.size(), is(1));
        assertThat(shop.getAllFood().size(), is(1));
    }
}