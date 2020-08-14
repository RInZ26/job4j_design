package ru.job4j.lsp.cleverparking.parking;

public abstract class AbstractParking implements Parking {
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

    protected AbstractParking(int lightAmount, int highAmount) {
        if (lightAmount < 0 || highAmount < 0) {
            throw new IllegalArgumentException("amounts can't be negative");
        }
        this.lightAmount = lightAmount;
        this.highAmount = highAmount;
        this.freeLightAmount = lightAmount;
        this.freeHighAmount = highAmount;
    }

    public int getFreeLightAmount() {
        return freeLightAmount;
    }

    public int getFreeHighAmount() {
        return freeHighAmount;
    }
}
