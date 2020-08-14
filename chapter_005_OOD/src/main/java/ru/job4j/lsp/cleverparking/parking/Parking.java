package ru.job4j.lsp.cleverparking.parking;


import ru.job4j.lsp.cleverparking.car.Car;

import java.util.Collection;

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
     * Возвращает все машины на парковке
     */
    Collection<Car> getCars();
}
