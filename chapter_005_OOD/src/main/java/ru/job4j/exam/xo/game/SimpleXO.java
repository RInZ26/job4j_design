package ru.job4j.exam.xo.game;

import ru.job4j.exam.xo.map.GameCell;
import ru.job4j.exam.xo.map.GameMap;
import ru.job4j.exam.xo.player.Player;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Простая реализация крестиков-ноликов с уже встроенными правилами
 */
public class SimpleXO implements Game {
    /**
     * Игровая карта
     */
    private GameMap<GameCell> gameMap;
    /**
     * Список правил
     */
    private Set<Rule<Game>> rules;
    /**
     * Список игроков
     */
    private Set<Player> players;

    public SimpleXO(Set<Player> players, GameMap gameMap) {
        this.gameMap = gameMap;
        this.rules = new HashSet<>() {{
            add(SimpleRule.getDefaultRules());
        }};
        this.players = players;
    }

    /**
     * Запуск игры с выбором первого начинающего - достигается за счёт абуза
     * сетов (во второй раз first не будет добавлен)
     *
     * @param first игрок, который ходит первым
     *
     * @return победившего игрока / null, если ничья
     */
    @Nullable
    @Override
    public Player startGame(Player first) {
        Set<Player> participantSet = new LinkedHashSet();
        participantSet.add(first);
        participantSet.addAll(players);

        while (!isGameEnd()) {
            for (Player participant : participantSet) {
                gameMap.print();
                if (playerTurn(participant)) {
                    gameMap.print();
                    return participant;
                }
            }
        }
        return null;
    }

    @Override
    public Set<Rule<Game>> getRules() {
        return rules;
    }

    @Override
    public Set<Player> getPlayers() {
        return players;
    }

    @Override
    public GameMap getMap() {
        return gameMap;
    }

    /**
     * Имитация хода игрока
     *
     * @param player
     *
     * @return ход был выигрышным ?
     */
    private boolean playerTurn(Player player) {
        boolean result = false;
        GameCell cell = null;
        do {
            if (isGameEnd()) {
                break;
            }
            cell = player.turn(this);
            result = checkPlayersTurn(cell);
        } while (!result);
        if (result) {
            cell.setPlayer(player);
        }
        return checkWin();
    }

    /**
     * Проверка лигитимности хода игрока -  координаты и занятость
     */
    private boolean checkPlayersTurn(GameCell cell) {
        return gameMap.checkCell(cell) && Objects.isNull(cell.getPlayer());
    }

    /**
     * Проверка состояния карты на wincondition по всем введенным rule
     * Так как игра пошаговая, то чей ход был последним - тот и победитель
     */
    private boolean checkWin() {
        return rules.stream()
                    .allMatch((rule -> rule.getRule()
                                           .test(this)));
    }

    /**
     * Проверка на завершение игры, если свободных ячеек на поле не осталась
     */
    private boolean isGameEnd() {
        return Arrays.stream(gameMap.getCells())
                     .flatMap(Arrays::stream)
                     .noneMatch(cell -> Objects.isNull(cell.getPlayer()));
    }

    @Override
    public boolean addPlayer(Player player) {
        return players.add(player);
    }

    @Override
    public boolean removePlayer(Player player) {
        return players.remove(player);
    }

    @Override
    public boolean addRule(Rule rule) {
        return rules.add(rule);
    }

    @Override
    public boolean removeRule(Rule rule) {
        return rules.remove(rule);
    }
}
