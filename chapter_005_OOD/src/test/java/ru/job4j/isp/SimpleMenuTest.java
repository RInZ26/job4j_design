package ru.job4j.isp;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.StringJoiner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SimpleMenuTest {
    @Test
    public void whenSearch() {
        MenuItem menuItem0 = new SimpleMenuItem("katya");
        MenuItem menuItem1 = new SimpleMenuItem("anya");
        Menu<MenuItem> menu = new SimpleMenu();
        menu.add(menuItem0);
        menu.add(menuItem0, menuItem1);
        assertThat(menu.findBy(item -> item.getName()
                                           .equals("katya")),
                   is(Arrays.asList(menuItem0)));
    }

    @Test
    public void whenRemoveBrunchThenChildrenDied() {
        MenuItem menuItem0 = new SimpleMenuItem("anya");
        MenuItem<SimpleMenuItem> menuItem1 = new SimpleMenuItem("katya");
        MenuItem<SimpleMenuItem> menuItem2 = new SimpleMenuItem("natasha");
        Menu<MenuItem> menu = new SimpleMenu();
        menu.add(menuItem0);
        menu.add(menuItem0, menuItem1);
        menu.add(menuItem1, menuItem2);
        menu.remove(menuItem1);
        assertThat(menuItem0.getChildren(), is(Collections.EMPTY_SET));
    }

    @Test
    public void whenRemoveRootThenAllDie() {
        MenuItem menuItem0 = new SimpleMenuItem("anya");
        MenuItem menuItem1 = new SimpleMenuItem("katya");
        MenuItem menuItem2 = new SimpleMenuItem("natasha");
        Menu<MenuItem> menu = new SimpleMenu();
        menu.add(menuItem0);
        menu.add(menuItem0, menuItem1);
        menu.add(menuItem1, menuItem2);
        menu.remove(menuItem0);
        assertThat(menu.getCollection(), is(Collections.EMPTY_SET));
    }

    @Test
    public void whenRemoveLeafChild() {
        MenuItem menuItem0 = new SimpleMenuItem("anya");
        MenuItem menuItem1 = new SimpleMenuItem("katya");
        Menu<MenuItem> menu = new SimpleMenu();
        menu.add(menuItem0);
        menu.add(menuItem1);
        menu.remove(menuItem1);
        assertThat(menu.getCollection(),
                   is(new HashSet<>(Arrays.asList(menuItem0))));
    }

    @Test
    public void whenRemoveLeafFromBrunchWithAFewLeafs() {
        MenuItem root = new SimpleMenuItem("anya");
        MenuItem brunch = new SimpleMenuItem("katya");
        MenuItem child1 = new SimpleMenuItem("natasha");
        MenuItem child2 = new SimpleMenuItem("alina");
        Menu<MenuItem> menu = new SimpleMenu<>();
        menu.add(root);
        menu.add(root, brunch);
        menu.add(brunch, child1);
        menu.add(brunch, child2);
        menu.remove(child1);
        assertThat(menu.getCollection(),
                   is(new HashSet<>(Arrays.asList(root, brunch, child2))));
        assertThat(brunch.getChildren(),
                   is(new HashSet(Arrays.asList(child2))));
    }

    @Test
    public void whenDisplaySingleTree() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        MenuItem root1 = new SimpleMenuItem("anya");
        MenuItem brunch1 = new SimpleMenuItem("katya");
        MenuItem child1 = new SimpleMenuItem("natasha");
        MenuItem child2 = new SimpleMenuItem("alina");
        Menu<MenuItem> menu = new SimpleMenu<>();
        menu.add(root1);
        menu.add(root1, brunch1);
        menu.add(brunch1, child1);
        menu.add(brunch1, child2);
        menu.display();
        StringJoiner joiner = new StringJoiner(System.lineSeparator());
        joiner.add(" anya")
              .add("*** katya")
              .add("****** alina")
              .add("****** " + "natasha")
              .add("");
        assertThat(out.toString(), is(joiner.toString()));
    }

    @Test
    public void whenDisplayMultiTree() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        MenuItem root1 = new SimpleMenuItem("anya");
        MenuItem brunch1 = new SimpleMenuItem("katya");
        MenuItem child1 = new SimpleMenuItem("natasha");
        MenuItem child2 = new SimpleMenuItem("alina");
        MenuItem brunch2 = new SimpleMenuItem("matvey");
        MenuItem child3 = new SimpleMenuItem("dima");
        Menu<MenuItem> menu = new SimpleMenu<>();
        menu.add(root1);
        menu.add(root1, brunch1);
        menu.add(brunch1, child1);
        menu.add(brunch1, child2);
        menu.add(root1, brunch2);
        menu.add(brunch2, child3);
        menu.display();
        StringJoiner joiner = new StringJoiner(System.lineSeparator());
        joiner.add(" anya")
              .add("*** katya")
              .add("****** alina")
              .add("****** natasha")
              .add("*** matvey")
              .add("****** dima")
              .add("");
        assertThat(out.toString(), is(joiner.toString()));
    }
}