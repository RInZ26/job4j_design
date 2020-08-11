package ru.job4j.lsp.parking;

public abstract class DefaultCar implements Car {
    /**
     * Размер машины в "клетках" паркинга
     */
    private int size;

    protected DefaultCar(int size) {
        if (size < 1) {
            throw new IllegalArgumentException("Size can't be negative or "
                                                       + "zero");
        }
        this.size = size;
    }

    @Override
    public int getSize() {
        return size;
    }
}
