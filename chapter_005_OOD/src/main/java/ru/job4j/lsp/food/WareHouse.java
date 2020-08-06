package ru.job4j.lsp.food;

import java.util.LinkedList;

public class WareHouse extends FoodSupplier {
    /**
     * Граница, выше которой нельзя
     */
    private double qualityHighBound = 0.25d;

    /**
     * Коллекцтя продуктов в виде LinkedList - та же логика, что и в Shop
     */
    public WareHouse() {
        super(new LinkedList<>());
    }

    @Override
    public boolean isSuitableFood(Food food) {
        return ControlQuality.consumeQuality(food) <= qualityHighBound;
    }
}
