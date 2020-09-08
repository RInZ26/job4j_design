package ru.job4j.threads.waitnotify;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Thread.State.WAITING;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SimpleBlockingQueueTest {

    @Test
    public void whenConsumerAndProducerRun() throws InterruptedException {
        SimpleBlockingQueue<String> queue = new SimpleBlockingQueue<>(3);
        List<String> offerList = Arrays.asList("anya", "natasha", "alina",
                                               "luda", "katya");
        List<String> pollList = new ArrayList<>();
        Thread producer = new Thread(() -> offerList.forEach(queue::offer));
        Thread consumer = new Thread(() -> {
            for (int c = 0; c < offerList.size(); c++) {
                pollList.add(queue.poll());
            }
        });
        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
        assertThat(pollList, is(offerList));
    }

    /**
     * В тестах с Only в циклах специально заведены x2 условия, чтобы не было
     * случая, что 1 поток успел всё сделать. В тестах используется
     * библиотека awaitlility как замена ужасному Thread.sleep() - с помощью
     * неё мы ждём когда обе нити перейдут в состояние Waiting - что и нужно
     */
    @Test
    public void whenOnlyProducersRun() throws InterruptedException {
        List<String> offerList = Arrays.asList("anya", "natasha", "alina",
                                               "luda", "katya");
        SimpleBlockingQueue<String> queue = new SimpleBlockingQueue<>(
                offerList.size());
        Thread producer1 = new Thread(() -> {
            offerList.forEach(queue::offer);
            offerList.forEach(queue::offer);
        });
        Thread producer2 = new Thread(() -> {
            offerList.forEach(queue::offer);
            offerList.forEach(queue::offer);
        });
        producer1.start();
        producer2.start();
        await().until(() -> producer1.getState()
                                     .equals(WAITING) && producer2.getState()
                                                                  .equals(WAITING));
        assertThat(queue.size(), is(5));
    }

    @Test
    public void whenOnlyConsumersRun() throws InterruptedException {
        List<String> offerList = Arrays.asList("anya", "natasha", "alina",
                                               "luda", "katya");
        SimpleBlockingQueue<String> queue = new SimpleBlockingQueue<>(
                offerList.size());
        offerList.forEach(queue::offer);
        List<String> pollList = new ArrayList<>();
        Thread consumer1 = new Thread(() -> {
            for (int c = 0; c < offerList.size() * 2; c++) {
                pollList.add(queue.poll());
            }
        });
        Thread consumer2 = new Thread(() -> {
            for (int c = 0; c < offerList.size() * 2; c++) {
                pollList.add(queue.poll());
            }
        });
        consumer1.start();
        consumer2.start();
        await().until(() -> consumer1.getState()
                                     .equals(WAITING) && consumer2.getState()
                                                                  .equals(WAITING));
        assertThat(queue.size(), is(0));
        assertThat(pollList.size(), is(5));
    }
}