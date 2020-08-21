package ru.job4j.exam.xo.map;

public interface Map<T extends Cell> {
    T[][] getCells();

    boolean setCell(int x, int y);

    T getCell(int x, int y);

    boolean checkCoordinates(int x, int y);

    boolean checkCell(Cell cell);
}
