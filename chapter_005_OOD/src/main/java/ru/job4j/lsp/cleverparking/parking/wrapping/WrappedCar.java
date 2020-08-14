package ru.job4j.lsp.cleverparking.parking.wrapping;

import ru.job4j.lsp.cleverparking.car.Car;
import ru.job4j.lsp.cleverparking.parking.map.Cell;
import ru.job4j.lsp.cleverparking.parking.map.CellType;
import ru.job4j.lsp.cleverparking.parking.wrapping.CarType.Holder;

import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Класс-обертка-структурка для Car, чтобы совмещать Car и CarType
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
     * Проверка + возвращение результатов, может ли машина такого типа встать
     * на клетку в текущих реалиях парковки с учетом приоритетов. Так, если
     * первый приоритет всё ещё доступен для постановки - другие приоритеты
     * рассматриваться не будут
     */
    public Cell[] getPriorityCells(CellType cellType, Holder holder) {
        var firstPriorityRule = type.getMapRules()
                                    .values()
                                    .stream()
                                    .filter(rule -> rule.getPriority()
                                                        .equals(CarType.Rule.Priority.FIRST)
                                            && rule.getIsExecutable()
                                                   .test(car,
                                                         holder.getParking()))
                                    .findAny();
        if (firstPriorityRule.isPresent()) {
            var currentRule = type.getMapRules()
                                  .get(cellType);
            if (!Objects.isNull(currentRule) && currentRule.getPriority()
                                                           .equals(CarType.Rule.Priority.FIRST)) {
                return currentRule.apply(holder);
            } else {
                return null;
            }
        } else {
            var otherPriorityRules = type.getMapRules()
                                         .values()
                                         .stream()
                                         .filter(rule -> !rule.getPriority()
                                                              .equals(CarType.Rule.Priority.FIRST)
                                                 && rule.getIsExecutable()
                                                        .test(car,
                                                              holder.getParking()))
                                         .sorted(Comparator.comparingInt(
                                                 a -> a.getPriority()
                                                       .getNumberPriority()))
                                         .collect(Collectors.toList());
            for (CarType.Rule rule : otherPriorityRules) {
                var cells = rule.apply(holder);
                if (!Objects.isNull(cells)) {
                    return cells;
                }
            }
        }
        return null;
    }

    /**
     * Проверка + возвращение результатов, может ли машина такого типа встать
     * на клетку без учета приоритетов
     */
    public Cell[] getAnyCells(CellType cellType, Holder holder) {
        var rule = type.getMapRules()
                       .get(cellType);
        if (!Objects.isNull(rule)) {
            return rule.apply(holder);
        }
        return null;
    }
}
