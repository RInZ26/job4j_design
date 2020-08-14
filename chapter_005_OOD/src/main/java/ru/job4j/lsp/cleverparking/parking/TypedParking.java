package ru.job4j.lsp.cleverparking.parking;

import ru.job4j.lsp.cleverparking.car.Car;
import ru.job4j.lsp.cleverparking.parking.map.Cell;
import ru.job4j.lsp.cleverparking.parking.map.CellType;
import ru.job4j.lsp.cleverparking.parking.map.Markup;
import ru.job4j.lsp.cleverparking.parking.wrapping.CarType;
import ru.job4j.lsp.cleverparking.parking.wrapping.CarType.Rule;
import ru.job4j.lsp.cleverparking.parking.wrapping.WrappedCar;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Типизированная парковка, которая поддерживает максимальную кастомизацию и
 * её поведение зависит только от настроек CarType.
 * Есть поддержка "примитивных парковок" с двумя типами машин -
 * грузовые/легковые. Для этого есть конструктор с вызовом setDefault();
 */
public class TypedParking extends AbstractParking {
    /**
     * Коллекция машин с массивом клеток, которые она занимает. Благодаря
     * этому легко и быстро работает remove
     */
    private Map<Car, Cell[]> carMap = new HashMap<>();
    /**
     * Разметка парковки
     */
    private Markup markup;

    /**
     * По имени возвращает тип машины, используется для враппинга, чтобы
     * правильно соотносить
     */
    private Map<String, CarType> mapCarTypes = new HashMap<>();

    /**
     * Диспатчер-фабрика
     * По имени типа, полученного из mapCarTypes возвращает определенную обёртку
     */
    private Map<CarType, Function<Car, WrappedCar>> dispatcherWrappedCars = new HashMap<>();

    /**
     * Зависимость типа ячеек от количества свободных ячеек
     */
    private Map<CellType, Integer> mapFreeCells = new HashMap<>();

    /**
     * Конструктор для совместимости логики простого паркинга.
     *
     * @param lightAmount количество легких машин
     * @param highAmount  количество тяжелых машин
     */
    public TypedParking(int lightAmount, int highAmount) {
        super(lightAmount, highAmount);
        setDefault(lightAmount, highAmount);
    }

    /**
     * Метод для совместимости, инициализирующий функционал для работы с
     * логикой по заданию.
     * Создаёт 2 типа мест парковки lightCell - для легковых, highCell - для
     * грузовых
     * Заполняет правила обработки каждого типа машин в зависимости от клеток.
     * То есть passCar может взаимодействовать только с клетками lightCell
     * А truckCar может взаимодействовать и с теми, и с другими
     * Далее создаются сами typeCars на основе правил и названий и заносятся
     * в мапы
     * В конце заполняется markup - структура парковки, исходя из дефолтной
     * логики: сначала подряд идут lightCell, потом hignCell
     */
    private void setDefault(int lightAmount, int highAmount) {
        var lightCell = new CellType("SMALL");
        var highCell = new CellType("BIG");
        mapFreeCells.put(lightCell, lightAmount);
        mapFreeCells.put(highCell, highAmount);
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
                }, (car, parking) -> parking.mapFreeCells.get(lightCell) > 0));
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
                            <= parking.mapFreeCells.get(lightCell)));
                put(highCell,
                    new CarType.Rule(CarType.Rule.Priority.FIRST, (holder) -> {
                        Cell cell = holder.getParking()
                                          .getMarkup()
                                          .getCell(holder.getX(),
                                                   holder.getY());
                        return cell.getType()
                                   .equals(highCell) ? new Cell[]{cell} : null;
                    }, //fixme потенциальное NPE, но для теста сойдёт
                                     (car, parking) ->
                                             parking.mapFreeCells.get(highCell)
                                                     > 0));
            }
        };
        var passCarType = new CarType("PASSENGER", 1, 1, passCarRules);
        var truckCarType = new CarType("TRUCK", 2, CarType.INFINITY_SIZE,
                                       truckCarRules);
        mapCarTypes.put(passCarType.getName(), passCarType);
        mapCarTypes.put(truckCarType.getName(), truckCarType);
        dispatcherWrappedCars.put(passCarType,
                                  (car -> new WrappedCar(car, passCarType)));
        dispatcherWrappedCars.put(truckCarType,
                                  (car -> new WrappedCar(car, truckCarType)));
        markup = Markup.createSquareMarkup(lightAmount, highAmount);
        //fill default map
        for (int row = 0; row < markup.getMap().length; row++) {
            for (int column = 0; column < markup.getMap()[row].length;
                 column++) {
                if (lightAmount-- > 0) {
                    markup.setCell(row, column, lightCell);
                } else if (highAmount-- > 0) {
                    markup.setCell(row, column, highCell);
                } else {
                    markup.setCell(row, column, CellType.UNPLACEABLE_CELL);
                }
            }
        }
    }

    /**
     * Возвращает тип машины, в которую парковка "обернёт"
     * исходную машину исходя из допустимых carTypes
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

    /**
     * Проверяет, может ли быть размещена машина
     */
    @Override
    public boolean isCanBeParking(Car car) {
        return !Objects.isNull(getSuitablePriorityCells(car));
    }

    /**
     * Проверяет, может ли быть размещена машина по указанным координатам
     */
    public boolean isCanBeParking(Car car, int x, int y) {
        return !Objects.isNull(getSuitableTargetCells(car, x, y));
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
     * Т.к. некоторые машины могут занимать несколько клеток, то логично, что
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
     * Размещает машину в первое доступное место (согласено приоритетам
     * правил добавления)
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

    /**
     * Добавление машины на парковку куда угодно по приоритету
     *
     * @return успех ?
     */
    @Override
    public boolean addCar(Car car) {
        return putToMap(car, getSuitablePriorityCells(car));
    }

    /**
     * Добавление машины на парковку в определенную ячейку(ки, если возможно)
     *
     * @return успех ?
     */
    public boolean addCar(Car car, int x, int y) {
        return putToMap(car, getSuitableTargetCells(car, x, y));
    }

    /**
     * DRY для addCar
     */
    private boolean putToMap(Car car, Cell[] cells) {
        boolean result = !Objects.isNull(cells);
        if (result) {
            updateCellsCount(cells, (a, b) -> a - b);
            updateMarkUp(cells, car);
            carMap.put(car, cells);
        }
        return result;
    }

    /**
     * фиксация нового количества клеток для всех типов.
     * Не факт, что у всех cells один тип, поэтому каждый подсчитывается
     * по-отдельности и заносится в мапу ячеек
     *
     * @param cells         влияющие на изменение ячейки
     * @param mergeFunction оператор сведения (для add и remove разные)
     */
    private void updateCellsCount(Cell[] cells,
                                  BinaryOperator<Integer> mergeFunction) {
        var groupedMap = Arrays.stream(cells)
                               .collect(Collectors.groupingBy(Cell::getType));
        for (Map.Entry<CellType, List<Cell>> entry : groupedMap.entrySet()) {
            mapFreeCells.merge(entry.getKey(), entry.getValue()
                                                    .size(), mergeFunction);
        }
    }

    /**
     * Машина "уезжает" с парковки с освобождением ячеек
     */
    @Override
    public boolean removeCar(Car car) {
        boolean result = carMap.containsKey(car);
        if (result) {
            var cells = carMap.get(car);
            updateCellsCount(cells, (a, b) -> a + b);
            updateMarkUp(carMap.get(car), null);
            carMap.remove(car);
        }
        return result;
    }

    /**
     * Убираем машину, которая занимает клетку (освободятся и другие клетки),
     * а также фиксируем новое количество клеток
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

    public Markup getMarkup() {
        return markup;
    }

    /**
     * Геттер для mapFreeCells
     */
    public Integer getFreeCellsCount(CellType cellType) {
        return mapFreeCells.get(cellType);
    }
}
