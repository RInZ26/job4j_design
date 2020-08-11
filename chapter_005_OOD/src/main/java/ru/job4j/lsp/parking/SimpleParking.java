package ru.job4j.lsp.parking;

import java.util.Collection;

public class SimpleParking extends DefaultParking {
    public SimpleParking(int lightAmount, int highAmount) {
        super(lightAmount, highAmount);
    }

    @Override
    public boolean isCanBeParking(Car car) {
        return false;
    }

    @Override
    public boolean addCar(Car car) {
        return false;
    }

    @Override
    public boolean removeCar(Car car) {
        return false;
    }

    @Override
    public Collection<Car> getCars() {
        return null;
    }
}
