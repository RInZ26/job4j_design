package ru.job4j.exam.xo.map;

import ru.job4j.exam.xo.player.Player;

import java.util.Objects;

public class SimpleGameMap implements GameMap<GameCell> {
    private GameCell[][] map;

    public SimpleGameMap(int size) {
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
        System.out.println();
        System.out.println(aroundCells(displayCells(), '*'));
        System.out.println();
    }

    private String displayCell(GameCell cell) {
        Player player = cell.getPlayer();
        return Character.toString(Objects.isNull(player) ? ' '
                                          : cell.getPlayer()
                                                .getLabel());
    }

    /**
     * собирает оформленные ячейки в массивы
     */
    private String[] displayCells() {
        String[] result = new String[map.length];
        StringBuilder builder;
        int c = 0;
        for (GameCell[] gameCells : map) {
            builder = new StringBuilder();
            for (GameCell cell : gameCells) {
                builder.append(displayCell(cell));
            }
            result[c++] = builder.toString();
        }
        return result;
    }

    /**
     * Обводит по контору рамкой ЛИНИИ (массивы) ячеек
     *
     * @param weave - чем обводим
     */
    private String aroundCells(String[] cellLines, char weave) {
        StringBuilder strb = new StringBuilder();
        strb.insert(0, displayHelper(map.length + 2, weave));
        strb.append(System.lineSeparator());
        for (String cell : cellLines) {
            strb.append(weave)
                .append(cell)
                .append(weave)
                .append(System.lineSeparator());
        }
        strb.append(displayHelper(map.length + 2, weave));
        return strb.toString();
    }

    /**
     * Возвращает строку с повторением указанного количества символов
     */
    private String displayHelper(int count, char character) {
        var sb = new StringBuilder(character);
        for (int c = 0; c < count; c++) {
            sb.append(character);
        }
        return sb.toString();
    }
}