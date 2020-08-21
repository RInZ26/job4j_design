package ru.job4j.exam.xo.player;

import ru.job4j.exam.xo.game.Game;
import ru.job4j.exam.xo.map.GameCell;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Простой ИИ с рандомными ходами
 */
public class II implements Player {
    /**
     * значок игрока
     */
    char label;

    public II(char label) {
        this.label = label;
    }

    @Override
    public char getLabel() {
        return label;
    }

    @Override
    public GameCell turn(Game game) {
        return randomShot(game);
    }

    /**
     * Случайный выстрел по свободной ячейке
     */
    private GameCell randomShot(Game game) {
        Random rnd = new Random();
        var cells = getFreeCells(game);
        return cells[rnd.nextInt(cells.length)];
    }

    /**
     * Подбирает свободные ячейки для выбора. Хотя идейно, этот метод и
     * просится в условный gameMap - это кажется избыточным
     */
    private GameCell[] getFreeCells(Game game) {
        return Arrays.stream(game.getMap()
                                 .getCells())
                     .flatMap(Arrays::stream)
                     .filter(GameCell::isFree)
                     .collect(Collectors.toList())
                     .toArray(GameCell[]::new);
    }
}
