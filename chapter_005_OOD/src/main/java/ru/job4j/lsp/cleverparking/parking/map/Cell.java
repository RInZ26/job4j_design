package ru.job4j.lsp.cleverparking.parking.map;

import ru.job4j.lsp.cleverparking.car.Car;

import java.util.Objects;

public class Cell {
    private int x;
    private int y;
    private CellType type;
    /**
     * Кто сейчас стоит на клетке
     */
    private Car owner;

    Cell(int x, int y, CellType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public CellType getType() {
        return type;
    }

    public void setType(CellType type) {
        this.type = type;
    }

    public boolean isFree() {
        return Objects.isNull(owner);
    }

    public Car getOwner() {
        return owner;
    }

    public void setOwner(Car owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Cell cell = (Cell) o;
        return x == cell.x && y == cell.y && Objects.equals(type, cell.type)
                && Objects.equals(owner, cell.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, type, owner);
    }

    @Override //fixme delete
    public String toString() {
        return isFree() ? "НИКТО" : owner.toString();
    }
}