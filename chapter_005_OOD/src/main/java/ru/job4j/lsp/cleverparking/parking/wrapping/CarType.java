package ru.job4j.lsp.cleverparking.parking.wrapping;

import ru.job4j.lsp.cleverparking.car.Car;
import ru.job4j.lsp.cleverparking.parking.TypedParking;
import ru.job4j.lsp.cleverparking.parking.map.Cell;
import ru.job4j.lsp.cleverparking.parking.map.CellType;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * Класс, определяющий какие типа клеток может занимать машина
 */
public class CarType {
    public final static int INFINITY_SIZE = Integer.MAX_VALUE;
    private int minSize;
    private int maxSize;
    private String name;
    /**
     * Мапа действий при проверке на добавление
     */
    private Map<CellType, Rule> mapRules;

    public CarType(String name, int minSize, int maxSize,
                   Map<CellType, Rule> mapRules) {
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.name = name;
        this.mapRules = mapRules;
    }

    /**
     * Проверка + возвращение результатов, может ли машина такого типа встать
     * на клетку в текущих реалиях
     */
    public Cell[] getPossibleCells(CellType cellType, Holder holder) {
        var result = mapRules.get(cellType);
        return !Objects.isNull(result) ? result.apply(holder) : null;
    }

    public String getName() {
        return name;
    }

    /**
     * Проверяет, принадлежит ли машина к данному типу
     *
     * @param size
     *
     * @return
     */
    public boolean isThisType(int size) {
        return size >= minSize && size <= maxSize;
    }

    /**
     * Класс - структурка, чтобы передавать данные для работы с Rule
     */
    public static class Holder {
        private int x;
        private int y;
        private int size;
        private TypedParking parking;

        public Holder(int x, int y, int size, TypedParking parking) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.parking = parking;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public TypedParking getParking() {
            return parking;
        }

        public int getSize() {
            return size;
        }
    }

    /**
     * Класс, вмещяющий в себя самую функцию + её приоритет, так как может
     * быть приоритет расстановки, например HighCar по приоритету займёт
     * место для HighCar, а уже во вторую очередь будет рассматривать места
     * для маленьких машин.
     *
     * Общая идея состоит в том, чтобы разделять правила по их значимости, на
     * берегу знать могут ли они выполняться и проверять только те, которые
     * могут быть выполнены
     */
    public static class Rule {
        private Priority priority;
        private Function<Holder, Cell[]> carRule;
        /**
         * Предикат, который должен проверять, может ли в теории выполниться
         * данное правило и если да, то другие не будут проверяться
         */
        private BiPredicate<Car, TypedParking> isExecutable;

        public Rule(Priority priority, Function<Holder, Cell[]> carRule,
                    BiPredicate<Car, TypedParking> isExecutable) {
            this.priority = priority;
            this.carRule = carRule;
            this.isExecutable = isExecutable;
        }

        /**
         * Заглушка для вызова apply'я из функции
         */
        public Cell[] apply(Holder holder) {
            return carRule.apply(holder);
        }

        public Priority getPriority() {
            return priority;
        }

        public void setPriority(Priority priority) {
            this.priority = priority;
        }

        public Function<Holder, Cell[]> getCarRule() {
            return carRule;
        }

        public void setCarRule(Function<Holder, Cell[]> carRule) {
            this.carRule = carRule;
        }

        public BiPredicate<Car, TypedParking> getIsExecutable() {
            return isExecutable;
        }

        public void setIsExecutable(BiPredicate<Car, TypedParking> isExecutable) {
            this.isExecutable = isExecutable;
        }

        public enum Priority { //FIXME
            FIRST, SECOND
        }
    }
}
