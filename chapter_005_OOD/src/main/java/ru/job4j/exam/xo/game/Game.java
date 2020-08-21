package ru.job4j.exam.xo.game;

import ru.job4j.exam.xo.map.GameCell;
import ru.job4j.exam.xo.map.GameMap;
import ru.job4j.exam.xo.player.Player;

import java.util.Set;

public interface Game {
    Player startGame(Player first);

    Set<Rule<Game>> getRules();

    Set<Player> getPlayers();

    GameMap<GameCell> getMap();

    boolean addPlayer(Player player);

    boolean removePlayer(Player player);

    boolean addRule(Rule rule);

    boolean removeRule(Rule rule);
}
