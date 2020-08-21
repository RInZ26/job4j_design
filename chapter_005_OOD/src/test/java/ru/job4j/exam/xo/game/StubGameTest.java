package ru.job4j.exam.xo.game;

import org.junit.Test;
import ru.job4j.exam.xo.map.GameCell;
import ru.job4j.exam.xo.map.StubMap;
import ru.job4j.exam.xo.player.Player;
import ru.job4j.exam.xo.player.StubPlayer;

import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.function.Predicate;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class StubGameTest {
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
        boolean result = true;
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
    Predicate<Game> secondaryDiagonal = (game) -> {
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

    Rule<Game> defaultRule = new StubRule(horizontal.or(vertical)
                                                    .or(mainDiagonal.or(
                                                            secondaryDiagonal)));

    @Test
    public void mainDiagWin() {
        StringJoiner playerTurns = new StringJoiner(System.lineSeparator());
        playerTurns.add("0")
                   .add("2")
                   .add("1")
                   .add("1")
                   .add("2")
                   .add("0");
        var in = new ByteArrayInputStream(playerTurns.toString()
                                                     .getBytes());
        System.setIn(in);
        Player player = new StubPlayer();
        Game game = new StubGame(Collections.emptySet(), new StubMap(3),
                                 new HashSet<>(Arrays.asList(defaultRule)));
        assertThat(game.startGame(player), is(player));
    }

    @Test
    public void horizontalWin() {
        StringJoiner playerTurns = new StringJoiner(System.lineSeparator());
        playerTurns.add("0")
                   .add("0")
                   .add("0")
                   .add("1")
                   .add("0")
                   .add("2");
        var in = new ByteArrayInputStream(playerTurns.toString()
                                                     .getBytes());
        System.setIn(in);
        Player player = new StubPlayer();
        Game game = new StubGame(Collections.emptySet(), new StubMap(3),
                                 new HashSet<>(Arrays.asList(defaultRule)));
        assertThat(game.startGame(player), is(player));
    }

    @Test
    public void verticalWin() {
        StringJoiner playerTurns = new StringJoiner(System.lineSeparator());
        playerTurns.add("0")
                   .add("0")
                   .add("1")
                   .add("0")
                   .add("2")
                   .add("0");
        var in = new ByteArrayInputStream(playerTurns.toString()
                                                     .getBytes());
        System.setIn(in);
        Player player = new StubPlayer();
        Game game = new StubGame(Collections.emptySet(), new StubMap(3),
                                 new HashSet<>(Arrays.asList(defaultRule)));
        assertThat(game.startGame(player), is(player));
    }

    @Test
    public void subDiagWin() {
        StringJoiner playerTurns = new StringJoiner(System.lineSeparator());
        playerTurns.add("0")
                   .add("0")
                   .add("1")
                   .add("1")
                   .add("2")
                   .add("2");
        var in = new ByteArrayInputStream(playerTurns.toString()
                                                     .getBytes());
        System.setIn(in);
        Player player = new StubPlayer();
        Game game = new StubGame(Collections.emptySet(), new StubMap(3),
                                 new HashSet<>(Arrays.asList(defaultRule)));
        assertThat(game.startGame(player), is(player));
    }
}
