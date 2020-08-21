package ru.job4j.exam.xo.game;

import ru.job4j.exam.xo.map.GameCell;
import ru.job4j.exam.xo.player.Player;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

public class SimpleRule<T> implements Rule<T> {
    Predicate<T> rule;

    public SimpleRule(Predicate<T> rule) {
        this.rule = rule;
    }

    @Override
    public Predicate<? super T> getRule() {
        return rule;
    }

    /**
     * Возвращает дефолтные правила для крестиков-ноликов
     */
    public static Rule<Game> getDefaultRules() {
        Predicate<Game> horizontal = (game) -> {
            var matrix = game.getMap()
                             .getCells();
            for (GameCell[] cells : matrix) {
                Player curPlayer = cells[0].getPlayer();
                if (Objects.isNull(curPlayer)) {
                    continue;
                }
                if (Arrays.stream(cells)
                          .allMatch(c -> curPlayer.equals(c.getPlayer()))) {
                    return true;
                }
            }
            return false;
        };
        Predicate<Game> vertical = (game) -> {
            boolean result = false;
            var matrix = game.getMap()
                             .getCells();
            for (int y = 0; y < matrix[0].length; y++) {
                Player player = matrix[0][y].getPlayer();
                if (Objects.isNull(player)) {
                    continue;
                }
                result = true;
                for (int x = 0; x < matrix.length; x++) {
                    if (!player.equals(game.getMap()
                                           .getCell(x, y)
                                           .getPlayer())) {
                        result = false;
                        break;
                    }
                }
                if (result) {
                    break;
                }
            }
            return result;
        };
        Predicate<Game> mainDiagonal = (game) -> {
            boolean result = true;
            var matrix = game.getMap()
                             .getCells();
            Player player = matrix[0][0].getPlayer();
            if (Objects.isNull(player)) {
                return false;
            }
            for (int x = 1; x < matrix.length; x++) {
                if (!player.equals(game.getMap()
                                       .getCell(x, x)
                                       .getPlayer())) {
                    result = false;
                    break;
                }
            }
            return result;
        };
        Predicate<Game> subDiagonal = (game) -> {
            boolean result = true;
            var matrix = game.getMap()
                             .getCells();
            Player player = matrix[0][matrix.length - 1].getPlayer();
            if (Objects.isNull(player)) {
                return false;
            }
            for (int x = matrix.length - 1, y = 0; x >= 0; x--, y++) {
                if (!player.equals(game.getMap()
                                       .getCell(x, y)
                                       .getPlayer())) {
                    result = false;
                    break;
                }
            }
            return result;
        };
        return new SimpleRule(horizontal.or(vertical)
                                        .or(mainDiagonal.or(
                                                subDiagonal)));
    }
}
