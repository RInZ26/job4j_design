package ru.job4j.lsp.cleverparking.car;

public abstract class AbstractCar implements Car {
    private int size;

    public AbstractCar(int size) {
        this.size = size;
    }

    @Override
    public int getSize() {
        return size;
    }
}
