package ru.job4j.lsp.parking;

import org.junit.Test;

public class CarTest {
    @Test(expected = IllegalArgumentException.class)
    public void whenInitParkingWithNegativeSizeWhenIllegalArgumentException() {
        Car car = new Truck((byte) -1);
    }
}
