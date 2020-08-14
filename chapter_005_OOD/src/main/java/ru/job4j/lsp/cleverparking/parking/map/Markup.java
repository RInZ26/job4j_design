package ru.job4j.lsp.cleverparking.parking.map;

import java.util.Arrays;
import java.util.function.Predicate;

public class Markup {
    private final Cell[][] map;

    Markup(int x, int y) {
        map = new Cell[x][y];
        for (int row = 0; row < map.length; row++) {
            for (int column = 0; column < map[row].length; column++) {
                map[row][column] = new Cell(row, column, CellType.EMPTY_CELL);
            }
        }
    }

    public Cell getCell(int x, int y) {
        checkCoordinates(x, y);
        return map[x][y];
    }

    public void setCell(int x, int y, CellType type) {
        checkCoordinates(x, y);
        map[x][y].setType(type);
    }

    /**
     * Проверка на лигитимность координат
     */
    public void checkCoordinates(int x, int y) {
        if (!(0 <= x && x < map.length) && (0 <= y && y < map[x].length)) {
            throw new IllegalArgumentException("Illegal coordinates in markup");
        }
    }

    /**
     * Хоть и лишний здесь метод, но возвращает квадратный маркап в качестве
     * дефолтного варианта парковки
     * Параметры носят формальный характер - для них ищется ближайший квадрат
     */
    public static Markup createSquareMarkup(int x, int y) {
        int size = (int) Math.ceil(Math.sqrt(x + y));
        return new Markup(size, size);
    }

    public Cell[][] getMap() {
        return map;
    }

    /**
     * 2 цикла, потому что сначала лучше проверить свободность
     *
     * @param x      строка
     * @param y      столбец
     * @param length длина,которую мы хотим проверить начиная с x y ВКЛЮЧИТЕЛЬНО
     * @param rule   условие
     */
    public boolean checkDiapasonInLine(int x, int y, int length,
                                       Predicate<Cell> rule) {
        if (y + length - 1 >= map[x].length) {
            return false;
        }
        for (int c = y; c <= y + length; c++) {
            if (!isFreeCell(x, y)) {
                return false;
            }
        }
        for (int c = y; c < y + length; c++) {
            if (!rule.test(map[x][c])) {
                return false;
            }
        }
        return true;
    }

    public Cell[] getRange(int x, int y, int length) {
        checkCoordinates(x, y);
        return Arrays.copyOfRange(map[x], y, length);
    }

    public boolean isFreeCell(int x, int y) {
        return getCell(x, y).isFree();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Markup markup = (Markup) o;
        return Arrays.equals(map, markup.map);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(map);
    }
}
