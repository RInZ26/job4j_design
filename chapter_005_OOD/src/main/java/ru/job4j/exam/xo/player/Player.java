package ru.job4j.exam.xo.player;

import ru.job4j.exam.xo.game.Game;
import ru.job4j.exam.xo.map.GameCell;

public interface Player {
    char getLabel();

    GameCell turn(Game game);
}
