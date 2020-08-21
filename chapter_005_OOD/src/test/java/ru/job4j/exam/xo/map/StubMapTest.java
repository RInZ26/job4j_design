package ru.job4j.exam.xo.map;

import org.junit.Test;
import ru.job4j.exam.xo.player.Player;
import ru.job4j.exam.xo.player.StubPlayer;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class StubMapTest {
    @Test
    public void whenFreeCellIsChosenThenSetPlayerAndReturnTrue() {
        Map<GameCell> map = new StubMap(3);
        Player player = new StubPlayer();
        var cell = map.getCell(0, 0);
        assertTrue(cell.setPlayer(player));
        assertThat(cell.getPlayer(), is(player));
    }

    @Test
    public void whenBusyCellIsChosenThenFalse() {
        Map<GameCell> map = new StubMap(3);
        Player player0 = new StubPlayer();
        Player player1 = new StubPlayer();
        var cell = map.getCell(0, 0);
        cell.setPlayer(player0);
        assertFalse(cell.setPlayer(player1));
        assertThat(cell.getPlayer(), is(player0));
    }
}