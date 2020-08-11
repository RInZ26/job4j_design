package ru.job4j.lsp.parking;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class MapParkingTest {
    @Test
    public void whenAddIsPossible() {
        Parking parking = new MapParking(1, 0);
        Car car = new PassengerCar();
        assertTrue(parking.isCanBeParking(car));
    }

    @Test
    public void whenAddIsImpossible() {
        Parking parking = new MapParking(0, 1);
        Car car = new PassengerCar();
        assertFalse(parking.isCanBeParking(car));
    }

    /**
     * Ситуация, когда свободные места рядом
     * До удаления
     * 11
     * 11
     * После удаления
     * 11
     * 00
     * В таком случае, машина на 2 места помещается
     */
    @Test
    public void whenAddHighCarToUnsplittedLightPlaceIsSuccess() {
        MapParking parking = new MapParking(4, 0);
        Car car1 = new PassengerCar();
        Car car2 = new PassengerCar();
        Car truck = new Truck(2);
        parking.addCar(car1);
        parking.addCar(car2);
        assertTrue(parking.isCanBeParking(truck));
        parking.addCar(truck);
        int[][] expected = new int[][]{{1, 1}, {1, 1}};
        assertThat(parking.getMapActual(), is(expected));
    }

    /**
     * Ситуация, когда свободные места рядом
     * До удаления
     * 11
     * 10
     * После удаления
     * 10
     * 10
     * В таком случае, машина на 2 места НЕ помещается
     */
    @Test
    public void whenAddHighCarToSplittedLightPlaceIsFail() {
        Parking parking = new MapParking(4, 0);
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
        Parking parking = new MapParking(-1, -1);
    }

    @Test
    public void whenAddThenStructureMapIs() {
        MapParking mapParking = new MapParking(4, 3);
        int[][] expected = new int[][]{{0, 0, 0}, {0, 1, 1}, {1, -1, -1}};
        assertThat(mapParking.getMapStructure(), is(expected));
    }

    @Test
    public void whenAddThenActualMapIs() {
        MapParking parking = new MapParking(4, 3);
        Car car1 = new PassengerCar();
        Car car2 = new PassengerCar();
        Car car3 = new PassengerCar();
        Car car4 = new Truck(2);
        parking.addCar(car1);
        parking.addCar(car2);
        parking.addCar(car3);
        parking.addCar(car4);
        int[][] expected = new int[][]{{1, 1, 1}, {0, 1, 0}, {0, -1, -1}};
        assertThat(parking.getMapActual(), is(expected));
    }

    @Test
    public void whenAddThenRemove() {
        MapParking parking = new MapParking(3, 0);
        Car car1 = new PassengerCar();
        parking.addCar(car1);
        parking.removeCar(car1);
        int[][] expected = new int[][]{{0, 0}, {0, -1}};
        assertFalse(parking.getCars()
                           .contains(car1));
        assertThat(parking.getMapActual(), is(expected));
    }

    @Test
    public void whenMultiAddAndMultiRemove() {
        MapParking parking = new MapParking(5, 1);
        Car car1 = new PassengerCar();
        Car car2 = new PassengerCar();
        Car car3 = new PassengerCar();
        Car car4 = new PassengerCar();
        Car car5 = new PassengerCar();
        Car truck1 = new Truck(3);
        Car truck2 = new Truck(2);
        parking.addCar(car1);
        parking.addCar(car2);
        parking.addCar(car3);
        parking.addCar(car4);
        parking.addCar(car5);
        parking.addCar(truck1);
        parking.removeCar(car2);
        parking.removeCar(car3);
        int[][] expected = new int[][]{{1, 0, 0}, {1, 1, 1}, {-1, -1, -1}};
        assertThat(parking.getMapActual(), is(expected));
        parking.addCar(truck2);
        expected = new int[][]{{1, 1, 1}, {1, 1, 1}, {-1, -1, -1}};
        assertThat(parking.getMapActual(), is(expected));
        parking.removeCar(truck2);
        expected = new int[][]{{1, 0, 0}, {1, 1, 1}, {-1, -1, -1}};
        assertThat(parking.getMapActual(), is(expected));
        parking.addCar(car2);
        expected = new int[][]{{1, 1, 0}, {1, 1, 1}, {-1, -1, -1}};
        assertThat(parking.getMapActual(), is(expected));
    }

    @Test
    public void whenCustomMapStructure() {
        MapParking parking = new MapParking(new int[][]{{-1, -1, 0, 1},
                {1, 1, -1, 0}, {0, -1, 0, -1}});
        assertThat(parking.getHighAmount(), is(3));
        assertThat(parking.getLightAmount(), is(4));
    }
    //todo написать тесты на проверку логики кастомного mapStructure

}