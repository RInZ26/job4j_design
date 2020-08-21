package ru.job4j.exam.xo.map;

import ru.job4j.exam.xo.player.Player;

public interface GameCell extends Cell {
    Player getPlayer();

    boolean setPlayer(Player player);

    boolean isFree();
}
