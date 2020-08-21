package ru.job4j.exam.xo.map;

import ru.job4j.exam.xo.display.Printable;

public interface GameMap<T extends GameCell> extends Map<T>, Printable {
    /**
     * "Билдинг" квадратной карты
     */
    T[][] buildMap(int size);
}
