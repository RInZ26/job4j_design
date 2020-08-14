package ru.job4j.lsp.cleverparking.parking.map;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class MarkupTest {
    @Test
    public void whenDefaultCreating() {
        Markup markup = Markup.createSquareMarkup(3, 4); //size = 3
        assertThat(markup.getMap().length, is(3));
        assertThat(markup.getMap()[0].length, is(3));
        assertThat(markup.getMap()[1].length, is(3));
        assertThat(markup.getMap()[2].length, is(3));
    }

    @Test
    public void checkDiapason() {
        Markup markup = Markup.createSquareMarkup(2, 3);
        assertTrue(markup.checkDiapasonInLine(1, 2, 1, (cell) -> cell.getType()
                                                                     .equals(CellType.EMPTY_CELL)));
        assertFalse(markup.checkDiapasonInLine(1, 2, 5, (cell) -> cell.getType()
                                                                     .equals(CellType.EMPTY_CELL)));
    }
}