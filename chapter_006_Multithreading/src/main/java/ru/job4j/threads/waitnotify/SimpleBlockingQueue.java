package ru.job4j.threads.waitnotify;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.LinkedList;
import java.util.Queue;

@ThreadSafe
public class SimpleBlockingQueue<T> {
    @GuardedBy("this")
    private Queue<T> queue = new LinkedList<>();
    /**
     * Искусственное ограничение очереди для демонстрации работы ожидания
     * потоков
     */
    private final int size;

    public SimpleBlockingQueue(int size) {
        this.size = size;
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }

    public synchronized int size() {
        return queue.size();
    }

    /**
     * Складываем value в queue
     */
    public synchronized void offer(T value) {
        try {
            while (queue.size() == size) {
                wait();
            }
            queue.add(value);
            System.out.println("Была добавлена " + value);
            notifyAll();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
            Thread.currentThread()
                  .interrupt();
        }
    }

    /**
     * Берем элемент, если есть, из queue
     */
    public synchronized T poll() {
        try {
            while (queue.isEmpty()) {
                wait();
            }
            notifyAll();
            T result = queue.poll();
            System.out.println("Была забрана " + result);
            return result;
        } catch (InterruptedException ie) {
            ie.printStackTrace();
            Thread.currentThread()
                  .interrupt();
            return null;
        }
    }
}
