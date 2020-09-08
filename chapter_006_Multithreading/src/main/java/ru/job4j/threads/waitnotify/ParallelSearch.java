package ru.job4j.threads.waitnotify;

import net.jcip.annotations.GuardedBy;

/**
 * Класс в котором параллельно создаются и считываются заявки благодаря
 *
 * @ThreadSafe коллекции SimpleBlockingQueue с внутренними wait. Здесь же +ом
 * нивелируется бесконечное пребывание консьюмера в состоянии wait, так как
 * идёт проверка на флаг окончания работы isSearchFinish
 */
public class ParallelSearch {
    /**
     * Кол-во producer'ов, которые ещё не завершили работу, а, следовательно,
     * ещё не должны закончить работу и потребители
     */
    @GuardedBy("this")
    private int producerCount;
    private SimpleBlockingQueue<Integer> queue;

    /**
     * @param size - размер очереди
     */
    public ParallelSearch(int size) {
        queue = new SimpleBlockingQueue<>(size);
    }

    public synchronized int getProducerCount() {
        return producerCount;
    }

    public synchronized void setProducerCount(int producerCount) {
        this.producerCount = producerCount;
    }

    /**
     * Создаёт и запускает нового продьюсера элементов
     *
     * @param count количество созданных элементов
     */
    public Thread startProducer(int count) {
        var result = new Thread(() -> {
            setProducerCount(getProducerCount() + 1);
            for (int index = 0; index != count; index++) {
                queue.offer(index);
            }
            setProducerCount(getProducerCount() + -1);
        });
        result.start();
        return result;
    }

    /**
     * Создаёт и запускает нового консьюмера
     */
    public Thread startConsumer() {
        var result = new Thread(() -> {
            while (isWorking()) {
                queue.poll();
            }
        });
        result.start();
        return result;
    }

    /**
     * Метод для удобства проверки окончания работы для консьюмеров
     */
    private synchronized boolean isWorking() {
        return getProducerCount() != 0 || !queue.isEmpty();
    }
}