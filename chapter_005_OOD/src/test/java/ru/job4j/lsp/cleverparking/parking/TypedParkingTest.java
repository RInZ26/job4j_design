package ru.job4j.lsp.cleverparking.parking;

import org.junit.Ignore;
import org.junit.Test;
import ru.job4j.lsp.cleverparking.car.Car;
import ru.job4j.lsp.cleverparking.car.HighCar;
import ru.job4j.lsp.cleverparking.car.LightCar;
import ru.job4j.lsp.cleverparking.parking.map.CellType;
import ru.job4j.lsp.cleverparking.parking.map.Markup;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class TypedParkingTest {
    CellType lightCell = new CellType("SMALL");
    CellType highCell = new CellType("BIG");

    @Test
    public void whenAddSmallCarThenTransformToLight() {
        Car car = new LightCar(1);
        TypedParking parking = new TypedParking(0, 0);
        assertThat(parking.getInnerType(car), is("PASSENGER"));
    }

    @Test
    public void whenAddSmallCarThenTransformToHigh() {
        Car car = new HighCar(4);
        TypedParking parking = new TypedParking(0, 0);
        assertThat(parking.getInnerType(car), is("TRUCK"));
    }

    @Test
    public void whenDefaultOptionMarkupIs() {
        Markup expected = Markup.createSquareMarkup(2, 1);
        expected.setCell(0, 0, lightCell);
        expected.setCell(0, 1, lightCell);
        expected.setCell(1, 0, highCell);
        expected.setCell(1, 1, CellType.UNPLACEABLE_CELL);
        TypedParking parking = new TypedParking(2, 1);
        assertThat(parking.getMarkup()
                          .getMap(), is(expected.getMap()));
    }

    @Test
    public void whenAddLightCarIsPossible() {
        Car car = new LightCar(1);
        TypedParking parking = new TypedParking(1, 0);
        assertThat(parking.isCanBeParking(car), is(true));
    }

    @Test
    public void whenAddLightCarIsImpossible() {
        Car car = new LightCar(1);
        TypedParking parking = new TypedParking(0, 2);
        assertThat(parking.isCanBeParking(car), is(false));
    }

    @Test
    public void whenAddHighCarIsPossible() {
        Car car = new HighCar(2);
        TypedParking parking1 = new TypedParking(0, 1);
        TypedParking parking2 = new TypedParking(2, 0);

        assertThat(parking1.isCanBeParking(car), is(true));
        assertThat(parking2.isCanBeParking(car), is(true));
    }

    @Test
    public void whenAddHighCarByRulesIsImpossible() {
        Car car1 = new HighCar(2);
        TypedParking parking1 = new TypedParking(1, 0);
        Car car2 = new HighCar(3);
        TypedParking parking2 = new TypedParking(4, 0);
        assertThat(parking1.isCanBeParking(car1), is(false));
        assertThat(parking2.isCanBeParking(car2), is(false));
    }

    @Test
    public void whenTargetAddLightCarIsPossible() {
        Car car = new LightCar(1);
        TypedParking parking = new TypedParking(1, 2);
        assertThat(parking.isCanBeParking(car, 0, 0), is(true));
    }

    @Test
    public void whenTargetAddLightCarIsImpossible() {
        Car car = new LightCar(1);
        TypedParking parking = new TypedParking(3, 2);
        assertThat(parking.isCanBeParking(car, 1, 0), is(false));
    }

    @Test
    public void whenTargetAddHighCarIsPossible() {
        Car car = new HighCar(2);
        TypedParking parking = new TypedParking(3, 2);
        assertThat(parking.isCanBeParking(car, 0, 0), is(true));
        assertThat(parking.isCanBeParking(car, 0, 1), is(true));
        assertThat(parking.isCanBeParking(car, 1, 0), is(true));
        assertThat(parking.isCanBeParking(car, 1, 1), is(true));
    }

    @Test
    public void whenTargetAddHighCarIsImpossible() {
        Car car = new HighCar(2);
        TypedParking parking = new TypedParking(3, 2);
        assertThat(parking.isCanBeParking(car, 0, 2), is(false));
        assertThat(parking.isCanBeParking(car, 1, 2), is(false));
        assertThat(parking.isCanBeParking(car, 2, 0), is(false));
        assertThat(parking.isCanBeParking(car, 2, 1), is(false));
        assertThat(parking.isCanBeParking(car, 2, 2), is(false));
    }

    @Ignore //fixme проблема с приоритетами
    @Test
    public void whenAddLightCarOnFirstPossiblePlaceWithSameType() {
        Car car = new HighCar(2);
        TypedParking parking = new TypedParking(2, 1);

        parking.addCar(car);
        assertTrue(parking.getCars()
                          .contains(car));
        Markup expected = Markup.createSquareMarkup(2, 1);
        expected.setCell(0, 0, lightCell);
        expected.setCell(0, 1, lightCell);
        expected.setCell(1, 0, highCell);
        expected.setCell(1, 1, CellType.UNPLACEABLE_CELL);
        parking.getMarkup()
               .getCell(1, 0)
               .setOwner(car);
        assertThat(parking.getMarkup()
                          .getMap(), is(expected.getMap()));
    }

    @Test
    public void whenAddLightCarOnFirstPossiblePlaceWithAnotherType() {
        Car car = new HighCar(2);
        TypedParking parking = new TypedParking(3, 0);
        parking.addCar(car);
        assertTrue(parking.getCars()
                          .contains(car));
        Markup expected = Markup.createSquareMarkup(3, 0);
        expected.setCell(0, 0, lightCell);
        expected.setCell(0, 1, lightCell);
        expected.setCell(1, 0, lightCell);
        expected.setCell(1, 1, CellType.UNPLACEABLE_CELL);
        expected.getCell(0, 0)
                .setOwner(car);
        expected.getCell(0, 1)
                .setOwner(car);
        assertThat(parking.getMarkup()
                          .getMap(), is(expected.getMap()));
    }

    @Test
    public void whenAddCarOnTargetPlace() {
        Car car = new LightCar(1);
        TypedParking parking = new TypedParking(2, 1);
        parking.addCar(car, 0, 1);
        assertTrue(parking.getCars()
                          .contains(car));
        Markup expected = Markup.createSquareMarkup(2, 1);
        expected.setCell(0, 0, lightCell);
        expected.setCell(0, 1, lightCell);
        expected.setCell(1, 0, highCell);
        expected.setCell(1, 1, CellType.UNPLACEABLE_CELL);
        expected.getCell(0, 1)
                .setOwner(car);
        assertThat(parking.getMarkup()
                          .getMap(), is(expected.getMap()));
    }

    @Test
    public void whenRemoveCarByCarThenCellsAreFree() {
        Car car = new HighCar(2);
        TypedParking parking = new TypedParking(3, 0);
        parking.addCar(car);
        parking.removeCar(car);
        Markup expected = Markup.createSquareMarkup(2, 1);
        expected.setCell(0, 0, lightCell);
        expected.setCell(0, 1, lightCell);
        expected.setCell(1, 0, lightCell);
        expected.setCell(1, 1, CellType.UNPLACEABLE_CELL);
        assertThat(parking.getMarkup()
                          .getMap(), is(expected.getMap()));
    }

    @Test
    public void whenRemoveCarByCellsThenCellsAreFree() {
        Car car = new HighCar(2);
        TypedParking parking = new TypedParking(3, 0);
        parking.addCar(car);
        parking.removeFromCells(0, 1);
        Markup expected = Markup.createSquareMarkup(2, 1);
        expected.setCell(0, 0, lightCell);
        expected.setCell(0, 1, lightCell);
        expected.setCell(1, 0, lightCell);
        expected.setCell(1, 1, CellType.UNPLACEABLE_CELL);
        assertThat(parking.getMarkup()
                          .getMap(), is(expected.getMap()));
    }
}