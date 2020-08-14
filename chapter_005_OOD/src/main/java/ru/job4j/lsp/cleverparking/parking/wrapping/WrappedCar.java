package ru.job4j.lsp.cleverparking.parking.wrapping;

import ru.job4j.lsp.cleverparking.car.Car;
import ru.job4j.lsp.cleverparking.parking.map.Cell;
import ru.job4j.lsp.cleverparking.parking.map.CellType;

/**
 * Класс-обертка, чтобы совмещать Car и её тип
 */
public class WrappedCar {
    private Car car;
    private CarType type;

    public WrappedCar(Car car, CarType type) {
        this.car = car;
        this.type = type;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public CarType getType() {
        return type;
    }

    public void setType(CarType type) {
        this.type = type;
    }

    /**
     * Просто обертка, чтобы делать более простые вызовы
     */
    public Cell[] getPossibleCells(CellType cellType, CarType.Holder holder) {
        return type.getPossibleCells(cellType, holder);
    }
}
