package ru.job4j.threads.waitnotify;

import org.junit.Test;

import static org.awaitility.Awaitility.await;

public class ParallelSearchTest {
    @Test
    public void whenProducersStopThenThreadIsShouldDie() {
        ParallelSearch ps = new ParallelSearch(10);
        var prodFirst = ps.startProducer(100);
        var consFirst = ps.startConsumer();
        await().until(() -> !consFirst.isAlive() && !prodFirst.isAlive());
    }
}