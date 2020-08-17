package ru.job4j.isp;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class SimpleMenuItemTest {
    @Test
    public void whenNodeIsNotRootThenGetParent() {
        MenuItem menuItem0 = new SimpleMenuItem("katya");
        MenuItem menuItem1 = new SimpleMenuItem("anya");
        Menu<MenuItem> menu = new SimpleMenu();
        menu.add(menuItem0);
        menu.add(menuItem0, menuItem1);
        assertThat(menuItem1.getParent(), is(menuItem0));
    }

    @Test
    public void whenNodeIsNotLeafThenGetChildren() {
        MenuItem menuItem0 = new SimpleMenuItem("katya");
        MenuItem menuItem1 = new SimpleMenuItem("anya");
        Menu<MenuItem> menu = new SimpleMenu();
        menu.add(menuItem0);
        menu.add(menuItem0, menuItem1);
        assertThat(menuItem0.getChildren(),
                   is(new HashSet<>(Arrays.asList(menuItem1))));
    }

    @Test
    public void whenNodeIsRootThenGetParentIsNull() {
        MenuItem menuItem0 = new SimpleMenuItem("katya");
        Menu<MenuItem> menu = new SimpleMenu();
        menu.add(menuItem0);
        assertThat(menuItem0.getParent(), is(SimpleMenuItem.EMPTY_ROOT));
    }

    @Test
    public void whenNodeIsLeafThenGetChildrenNull() {
        MenuItem menuItem0 = new SimpleMenuItem("anya");
        Menu<MenuItem> menu = new SimpleMenu();
        menu.add(menuItem0);
        assertThat(menuItem0.getChildren(), is(Collections.emptySet()));
    }

    @Test
    public void whenNodeIsLeafThenTrue() {
        MenuItem menuItem0 = new SimpleMenuItem("anya");
        Menu<MenuItem> menu = new SimpleMenu();
        menu.add(menuItem0);
        assertTrue(menuItem0.isLeaf());
    }

    @Test
    public void whenNodeIsRootThenTrue() {
        MenuItem menuItem0 = new SimpleMenuItem("anya");
        Menu<MenuItem> menu = new SimpleMenu();
        menu.add(menuItem0);
        assertTrue(menuItem0.isRoot());
    }

    @Test
    public void whenNodeIsNotLeafThenFalse() {
        MenuItem menuItem0 = new SimpleMenuItem("anya");
        MenuItem menuItem1 = new SimpleMenuItem("katya");
        Menu<MenuItem> menu = new SimpleMenu();
        menu.add(menuItem0);
        menu.add(menuItem0, menuItem1);
        assertFalse(menuItem0.isLeaf());
    }

    @Test
    public void whenNodeIsNotRootThenFalse() {
        MenuItem menuItem0 = new SimpleMenuItem("anya");
        MenuItem menuItem1 = new SimpleMenuItem("katya");
        Menu<MenuItem> menu = new SimpleMenu();
        menu.add(menuItem0);
        menu.add(menuItem0, menuItem1);
        assertFalse(menuItem1.isRoot());
    }

    @Test
    public void whenDisplaySecondDeep() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        MenuItem menuItem0 = new SimpleMenuItem("anya");
        MenuItem menuItem1 = new SimpleMenuItem("katya");
        MenuItem menuItem2 = new SimpleMenuItem("natasha");
        menuItem0.addChild(menuItem1);
        menuItem1.setParent(menuItem0);
        menuItem1.addChild(menuItem2);
        menuItem2.setParent(menuItem1);
        menuItem2.display();
        assertThat(outputStream.toString(), is("****** natasha" + System.lineSeparator()));
    }

    @Test
    public void whenDisplayFirstDeep() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        MenuItem menuItem0 = new SimpleMenuItem("anya");
        MenuItem menuItem1 = new SimpleMenuItem("katya");
        menuItem0.addChild(menuItem1);
        menuItem1.setParent(menuItem0);
        menuItem1.display();
        assertThat(outputStream.toString(), is("*** katya" + System.lineSeparator()));
    }

    @Test
    public void whenDisplayZeroDeep() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        MenuItem menuItem0 = new SimpleMenuItem("anya");
        menuItem0.display();
        assertThat(outputStream.toString(), is(" anya" + System.lineSeparator()));
    }
}