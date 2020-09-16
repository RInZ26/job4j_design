package ru.job4j.threads.waitnotify;

import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SimpleBlockingQueueTest2 {
    @Test
    public void whenFetchAllThenGetIt() throws InterruptedException {
        final CopyOnWriteArrayList<Integer> buffer =
                new CopyOnWriteArrayList<>();
        final SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(
                10);
        Thread producer = new Thread(() -> {
            IntStream.range(0, 5)
                     .forEach(queue::offer);
        });
        producer.start();
        Thread consumer = new Thread(() -> {
            while (!queue.isEmpty() || !Thread.currentThread()
                                              .isInterrupted()) {
                buffer.add(queue.poll());
            }
        });
        consumer.start();
        producer.join();
        consumer.interrupt();
        assertThat(buffer, is(Arrays.asList(0, 1, 2, 3, 4)));
    }
}