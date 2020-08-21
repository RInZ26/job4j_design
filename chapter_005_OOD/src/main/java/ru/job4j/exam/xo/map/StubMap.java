package ru.job4j.exam.xo.map;

public class StubMap implements GameMap<GameCell> {
    private GameCell[][] map;

    public StubMap(int size) {
        map = buildMap(size);
    }

    @Override
    public GameCell[][] getCells() {
        return map;
    }

    @Override
    public boolean setCell(int x, int y) {
        return false;
    }

    @Override
    public GameCell getCell(int x, int y) {
        return checkCoordinates(x, y) ? map[x][y] : null;
    }

    @Override
    public boolean checkCoordinates(int x, int y) {
        return x < map.length && x >= 0 && y < map[x].length && y >= 0;
    }

    @Override
    public GameCell[][] buildMap(int size) {
        var cells = new GameCell[size][size];
        for (int c = 0; c < size; c++) {
            cells[c] = new GameCell[size];
            for (int i = 0; i < size; i++) {
                cells[c][i] = new StubCell(c, i);
            }
        }
        return cells;
    }

    @Override
    public boolean checkCell(Cell cell) {
        var coords = cell.getCoordinates();
        return checkCoordinates(coords[0], coords[1]);
    }

    @Override
    public void print() {
    }
}
