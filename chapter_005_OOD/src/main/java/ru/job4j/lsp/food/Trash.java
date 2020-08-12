package ru.job4j.lsp.food;

import java.util.ArrayList;

public class Trash extends FoodSupplier {
    /**
     * Граница, ниже которой нельзя
     */
    private double qualityLowBound = 1d;

    /**
     * Коллекцтя продуктов в виде ArrayList, потому что мы вряд ли будем
     * что-то отсюда удалить из центра
     */
    public Trash() {
        super(new ArrayList<>());
    }

    @Override
    public boolean isSuitableFood(Food food) {
        return new ControlQuality().consumeQuality(food) >= qualityLowBound;
    }
}
