package ru.job4j.lsp.cleverparking.parking;

import ru.job4j.lsp.cleverparking.car.Car;
import ru.job4j.lsp.cleverparking.parking.map.Cell;
import ru.job4j.lsp.cleverparking.parking.map.CellType;
import ru.job4j.lsp.cleverparking.parking.map.Markup;
import ru.job4j.lsp.cleverparking.parking.wrapping.CarType;
import ru.job4j.lsp.cleverparking.parking.wrapping.CarType.Rule;
import ru.job4j.lsp.cleverparking.parking.wrapping.WrappedCar;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

//fixme у правил должны быть ещё приоритеты
public class TypedParking extends AbstractParking {
    private Map<Car, Cell[]> carMap = new HashMap<>();

    private Markup markup;
    /**
     * По имени возвращает тип
     */
    private Map<String, CarType> mapCarTypes = new HashMap<>();
    /**
     * По имени типа возвращает опреденный враппер
     */
    private Map<CarType, Function<Car, WrappedCar>> dispatcherWrappedCars = new HashMap<>();

    public TypedParking(int lightAmount, int highAmount) {
        super(lightAmount, highAmount);
        setDefault(lightAmount, highAmount);
    }

    private void setDefault(int lightAmount, int highAmount) {
        var lightCell = new CellType("SMALL");
        var highCell = new CellType("BIG");
        Map<CellType, Rule> passCarRules = new HashMap<>() {
            {
                put(lightCell, new Rule(Rule.Priority.FIRST, (holder) -> {
                    Cell checkedCell = holder.getParking()
                                             .getMarkup()
                                             .getCell(holder.getX(),
                                                      holder.getY());
                    return checkedCell.getType()
                                      .equals(lightCell) ? new Cell[]{
                            checkedCell} : null;
                }, (car, parking) -> parking.getFreeLightAmount() > 0));
            }
        };
        Map<CellType, Rule> truckCarRules = new HashMap<>() {
            {
                put(lightCell,
                    new CarType.Rule(CarType.Rule.Priority.SECOND, (holder) -> {
                        Cell checkedCell = holder.getParking()
                                                 .getMarkup()
                                                 .getCell(holder.getX(),
                                                          holder.getY());
                        return checkedCell.getType()
                                          .equals(lightCell)
                                && markup.checkDiapasonInLine(holder.getX(),
                                                              holder.getY(),
                                                              holder.getSize(),
                                                              (cell) -> cell.getType()
                                                                            .equals(lightCell))
                                ? markup.getRange(holder.getX(), holder.getY(),
                                                  holder.getSize()) : null;
                    }, (car, parking) -> car.getSize()
                            <= parking.getFreeLightAmount()));
                put(highCell,
                    new CarType.Rule(CarType.Rule.Priority.FIRST, (holder) -> {
                        Cell cell = holder.getParking()
                                          .getMarkup()
                                          .getCell(holder.getX(),
                                                   holder.getY());
                        return cell.getType()
                                   .equals(highCell) ? new Cell[]{cell} : null;
                    }, (car, parking) -> parking.getFreeHighAmount() > 0));
            }
        };
        var passCar = new CarType("PASSENGER", 1, 1, passCarRules);
        var truckCar = new CarType("TRUCK", 2, CarType.INFINITY_SIZE,
                                   truckCarRules);
        mapCarTypes.put(passCar.getName(), passCar);
        mapCarTypes.put(truckCar.getName(), truckCar);
        dispatcherWrappedCars.put(passCar,
                                  (car -> new WrappedCar(car, passCar)));
        dispatcherWrappedCars.put(truckCar,
                                  (car -> new WrappedCar(car, truckCar)));
        markup = Markup.createSquareMarkup(lightAmount, highAmount);
        //fill default map
        for (int row = 0; row < markup.getMap().length; row++) {
            for (int column = 0; column < markup.getMap()[row].length;
                 column++) {
                if (lightAmount-- > 0) {
                    setCell(row, column, lightCell);
                } else if (highAmount-- > 0) {
                    setCell(row, column, highCell);
                } else {
                    setCell(row, column, CellType.UNPLACEABLE_CELL);
                }
            }
        }
    }

    /**
     * Здесь мы победим private у матрицы Markup и будем сетить сюда //fixme
     *
     * @param x
     * @param y
     * @param type
     */
    private void setCell(int x, int y, CellType type) {
        markup.setCell(x, y, type);
    }

    /**
     * Возвращает тип машины, согласно условиям парковки
     *
     * @param car
     *
     * @return
     */
    public String getInnerType(Car car) {
        int size = car.getSize();
        for (CarType carType : mapCarTypes.values()) {
            if (carType.isThisType(size)) {
                return carType.getName();
            }
        }
        return "";
    }

    @Override
    public boolean isCanBeParking(Car car) {
        return !Objects.isNull(getSuitablePriorityCells(car));
    }

    /**
     * Возвращает обертку машины
     */
    private WrappedCar wrapCar(Car car) {
        String type = getInnerType(car);
        if (type.equals("")) {
            return null;
        }
        return dispatcherWrappedCars.get(mapCarTypes.get(type))
                                    .apply(car);
    }

    /**
     * Случай с таргетной подстановкой
     *
     * @param car
     * @param x
     * @param y
     *
     * @return
     */
    public boolean isCanBeParking(Car car, int x, int y) {
        return !Objects.isNull(getSuitableTargetCells(car, x, y));
    }

    /**
     * Т.к. некоторые машины могут занимать могут клеток, то логично, что
     * желание встать на клетку подразумевает возможность занять несколько
     * клеток после => возврвщается такой же массив клеток
     */
    private Cell[] getSuitableTargetCells(Car car, int x, int y) {
        if (!markup.isFreeCell(x, y)) {
            return null;
        }
        Cell cell = markup.getCell(x, y);
        var wrappedCar = wrapCar(car);
        return wrappedCar.getAnyCells(cell.getType(),
                                           new CarType.Holder(cell.getX(),
                                                              cell.getY(),
                                                              wrappedCar.getCar()
                                                                        .getSize(),
                                                              this));
    }

    /**
     * Проверка, можно ла разместить куда угодно
     *
     * @param car
     *
     * @return возвращает клетку в которую можно разместить
     */
    private Cell[] getSuitablePriorityCells(Car car) {
        var wrappedCar = wrapCar(car);
        for (int x = 0; x < markup.getMap().length; x++) {
            for (int y = 0; y < markup.getMap()[x].length; y++) {
                if (!markup.getMap()[x][y].isFree()) {
                    continue;
                }
                var result = wrappedCar.getPriorityCells(
                        markup.getMap()[x][y].getType(),
                        new CarType.Holder(x, y, wrappedCar.getCar()
                                                           .getSize(), this));
                if (!Objects.isNull(result)) {
                    return result;
                }
            }
        }
        return null;
    }

    public Markup getMarkup() {
        return markup;
    }

    @Override
    public boolean addCar(Car car) {
        return putToMap(car, getSuitablePriorityCells(car));
    }

    public boolean addCar(Car car, int x, int y) {
        return putToMap(car, getSuitableTargetCells(car, x, y));
    }

    /**
     * DRY
     *
     * @param car
     * @param cells
     */
    private boolean putToMap(Car car, Cell[] cells) {
        boolean result = !Objects.isNull(cells);
        if (result) {
            updateMarkUp(cells, car);
            carMap.put(car, cells);

        }
        return result;
    }

    @Override
    public boolean removeCar(Car car) {
        boolean result = carMap.containsKey(car);
        if (result) {
            updateMarkUp(carMap.get(car), null);
            carMap.remove(car);
        }
        return result;
    }

    /**
     * Убирает машину по клетке
     *
     * @param x
     * @param y
     *
     * @return
     */
    public boolean removeFromCells(int x, int y) {
        return removeCar(markup.getCell(x, y)
                               .getOwner());
    }

    @Override
    public Collection<Car> getCars() {
        return carMap.keySet();
    }

    /**
     * Ставит в соответствие "владельца" клеток на данный момент, owner =null
     * - клетка свободна
     *
     * @param cells изменяемые клетки
     * @param owner владелец
     */
    private void updateMarkUp(Cell[] cells, Car owner) {
        for (Cell cell : cells) {
            cell.setOwner(owner);
        }
    }
}
