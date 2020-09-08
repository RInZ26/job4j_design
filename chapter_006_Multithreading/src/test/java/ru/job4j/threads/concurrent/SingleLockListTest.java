package ru.job4j.threads.concurrent;

import org.junit.Test;

import java.util.*;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SingleLockListTest {
    @Test
    public void add() throws InterruptedException {
        SingleLockList<Integer> list = new SingleLockList<>();
        Thread first = new Thread(() -> list.add(1));
        Thread second = new Thread(() -> list.add(2));
        first.start();
        second.start();
        first.join();
        second.join();
        Set<Integer> rsl = new TreeSet<>();
        list.iterator()
            .forEachRemaining(rsl::add);
        assertThat(rsl, is(Set.of(1, 2)));
    }

    @Test
    public void getIteratorThenChangeCollection() throws InterruptedException {
        var list = new SingleLockList<Integer>();
        Thread first = new Thread(() -> list.add(1));
        Thread second = new Thread(() -> list.add(2));
        first.start();
        first.join();
        List<Integer> afterFirst = new ArrayList<>();
        List<Integer> afterSecond = new ArrayList<>();
        var iterFirst = list.iterator();
        second.start();
        second.join();
        iterFirst.forEachRemaining(afterFirst::add);
        assertThat(afterFirst, is(Collections.singletonList(1)));
        list.iterator()
            .forEachRemaining(afterSecond::add);
        assertThat(afterSecond, is(Arrays.asList(1, 2)));
    }
}