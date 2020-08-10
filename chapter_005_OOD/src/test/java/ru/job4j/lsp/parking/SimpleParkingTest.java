package ru.job4j.lsp.parking;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SimpleParkingTest {
    @Test
    public void whenAddIsPossible() {
        Parking parking = new SimpleParking(1, 0);
        Car car = new PassengerCar();
        assertTrue(parking.isCanBeParking(car));
    }

    @Test
    public void whenAddIsImpossible() {
        Parking parking = new SimpleParking(0, 1);
        Car car = new PassengerCar();
        assertFalse(parking.isCanBeParking(car));
    }

    /**
     * Ситуация, когда свободные места рядом
     * До удаления 1110
     * После удаления 1100
     * В таком случае, машина на 2 места помещается
     */
    @Test
    public void whenAddHighCarToUnsplittedLightPlaceIsSuccess() {
        Parking parking = new SimpleParking(4, 0);
        Car car1 = new PassengerCar();
        Car car2 = new PassengerCar();
        Car car3 = new PassengerCar();
        parking.addCar(car1);
        parking.addCar(car2);
        parking.addCar(car3);
        parking.removeCar(car3);
        Car truck = new Truck(2);
        assertTrue(parking.isCanBeParking(truck));
    }

    /**
     * Ситуация, когда свободные места рядом
     * До удаления 1110
     * После удаления 1010
     * В таком случае, машина на 2 места НЕ помещается
     */
    @Test
    public void whenAddHighCarToSplittedLightPlaceIsFail() {
        Parking parking = new SimpleParking(4, 0);
        Car car1 = new PassengerCar();
        Car car2 = new PassengerCar();
        Car car3 = new PassengerCar();
        parking.addCar(car1);
        parking.addCar(car2);
        parking.addCar(car3);
        parking.removeCar(car2);
        Car truck = new Truck((byte) 2);
        assertFalse(parking.isCanBeParking(truck));
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenInitParkingWithNegativeSizeWhenIllegalArgumentException() {
        Parking parking = new SimpleParking(-1, -1);
    }
}