package ru.job4j.lsp.food;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
        var expected = new ControlQuality().consumeQuality(
                new Food(null, foodCreated, foodExpired, 0, 0));
        assertThat(expected, is(0.90d));
    }

    @Test
    public void whenDifferenceIs94days() {
        LocalDateTime foodCreated = LocalDateTime.now()
                                                 .minusDays(36);
        LocalDateTime foodExpired = LocalDateTime.now()
                                                 .plusDays(59);
        var expected = new ControlQuality().consumeQuality(
                new Food(null, foodCreated, foodExpired, 0, 0));
        assertThat(expected, is(0.38d));
    }

    @Test //expectedBeforeRoundIsLine 0.357 -> rounded 0.36
    public void whenResultShouldBeRounded() {
        LocalDateTime foodCreated = LocalDateTime.now()
                                                 .minusDays(5);
        LocalDateTime foodExpired = LocalDateTime.now()
                                                 .plusDays(9);
        var expected = new ControlQuality().consumeQuality(
                new Food(null, foodCreated, foodExpired, 0, 0));
        assertThat(expected, is(0.36d));
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
        assertFalse(new ControlQuality(Arrays.asList(shop)).splitFoodSoft(
                foodList));
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
        assertTrue(new ControlQuality(Arrays.asList(shop)).splitFoodSoft(
                foodList));
    }

    @Test
    public void resortFood() {
        FoodSupplier shop = new Shop();
        FoodSupplier wareHouse = new WareHouse();
        FoodSupplier trash = new Trash();
        Food oldFood = new Food("A", LocalDateTime.now()
                                                  .minusDays(5),
                                LocalDateTime.now()
                                             .minusDays(2), 0, 0);
        Food normalFood = new Food("B", LocalDateTime.now()
                                                     .minusDays(90),
                                   LocalDateTime.now()
                                                .plusDays(10), 0, 0);

        Food bestFood = new Food("C", LocalDateTime.now(), LocalDateTime.now()
                                                                        .plusDays(
                                                                                10),
                                 0, 0);
        List<Food> foodList = new ArrayList<>() {
            {
                add(oldFood);
                add(normalFood);
                add(bestFood);
            }
        };
        var cQ = new ControlQuality(Arrays.asList(shop, wareHouse, trash));
        cQ.splitFoodSoft(foodList);
        oldFood.setExpiredDate(LocalDateTime.now()
                                            .plusDays(1000)); //move to
        // warehouse
        normalFood.setExpiredDate(LocalDateTime.now()
                                               .plusDays(1000)); //same
        cQ.resort();
        assertThat(wareHouse.getAllFood()
                            .size(), is(3));
        assertThat(trash.getAllFood(), is(Collections.emptyList()));
        assertThat(shop.getAllFood(), is(Collections.emptyList()));

    }
}