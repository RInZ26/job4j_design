package ru.job4j.lsp.parking;

import java.util.Collection;

/**
 * Т.к. по ISP интерфейсы всему голова, то сама обёртка для классов будет в
 * виде них (а могла быть и абстрактным классом сразу, но это не true)
 */
public interface Parking {
    /**
     * Добавление Car в коллекцию, дружащий с isCanBeParking
     *
     * @return добавление успешно?
     */
    boolean addCar(Car car);

    /**
     * Машина уехала - удалили из коллекции
     */
    boolean removeCar(Car car);

    /**
     * Метод - правая рука addCar, выполняющийся перед ним и
     * проверяющий - поместится ли Car
     */
    boolean isCanBeParking(Car car);

    /**
     * Геттеры, возвращающие количество свободных мест для маленьких машин и
     * больших машин
     */
    int getLightAmount();

    int getHighAmount();

    /**
     * Возвращает все машины на парковке
     */
    Collection<Car> getCars();
}
