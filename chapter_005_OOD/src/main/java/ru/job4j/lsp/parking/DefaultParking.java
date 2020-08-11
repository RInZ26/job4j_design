package ru.job4j.lsp.parking;

import java.util.Map;

public abstract class DefaultParking implements Parking {
    /**
     * Сколько всего их было при создании
     */
    protected final int lightAmount;
    protected final int highAmount;
    /**
     * Актуальные данный момент, вместо size
     */
    protected int freeLightAmount;
    protected int freeHighAmount;

    protected DefaultParking(int lightAmount, int highAmount) {
        if (lightAmount < 0 || highAmount < 0) {
            throw new IllegalArgumentException("amounts can't be negative");
        }
        this.lightAmount = lightAmount;
        this.highAmount = highAmount;
        this.freeLightAmount = lightAmount;
        this.freeHighAmount = highAmount;
    }

    /**
     * Не совсем хорошая реализация, но, можно будет создать ещё один
     * абстрактный класс для ветки с использованием Map, а так я, получается,
     * использую методы наследников, что нехорошо
     */
    protected DefaultParking(int[][] mapStructure) {
        Map<Integer, Integer> map = MapParking.figureAmountsByMapStructure(
                mapStructure);
        this.lightAmount = map.getOrDefault(0, 0);
        this.highAmount = map.getOrDefault(1, 0);
        this.freeLightAmount = lightAmount;
        this.freeHighAmount = highAmount;
    }

    @Override
    public int getLightAmount() {
        return lightAmount;
    }

    @Override
    public int getHighAmount() {
        return highAmount;
    }

    public int getFreeLightAmount() {
        return freeLightAmount;
    }

    public int getFreeHighAmount() {
        return freeHighAmount;
    }
}
