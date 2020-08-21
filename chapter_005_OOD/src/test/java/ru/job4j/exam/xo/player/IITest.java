package ru.job4j.exam.xo.player;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;
import ru.job4j.exam.xo.game.Game;
import ru.job4j.exam.xo.game.SimpleXO;
import ru.job4j.exam.xo.map.SimpleGameMap;

import java.util.HashSet;

import static org.junit.Assert.assertThat;

public class IITest {
    @Test
    public void whenIICanFinishTheGame() {
        Player ii1 = new II('X');
        Player ii2 = new II('O');
        Game game = new SimpleXO(new HashSet<>() {{
            add(ii1);
            add(ii2);
        }}, new SimpleGameMap(3));
        assertThat(game.startGame(ii1), new Matcher<Player>() {
            @Override
            public boolean matches(Object item) {
                return item == null || item.equals(ii1) || item.equals(ii2);
            }

            @Override
            public void describeMismatch(Object item,
                                         Description mismatchDescription) {

            }

            @Override
            public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {

            }

            @Override
            public void describeTo(Description description) {

            }
        });
    }
}