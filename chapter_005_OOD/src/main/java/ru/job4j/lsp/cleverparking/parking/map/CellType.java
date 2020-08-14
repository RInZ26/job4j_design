package ru.job4j.lsp.cleverparking.parking.map;

import java.util.Objects;

public class CellType {
    public static final CellType UNPLACEABLE_CELL = new CellType("UNPLACEABLE");
    public static final CellType EMPTY_CELL = new CellType("EMPTY");
    private String name;

    public CellType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CellType cellType = (CellType) o;
        return Objects.equals(name, cellType.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
