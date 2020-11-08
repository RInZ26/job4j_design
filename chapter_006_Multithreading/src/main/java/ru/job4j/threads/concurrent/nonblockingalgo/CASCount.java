package ru.job4j.threads.concurrent.nonblockingalgo;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Потокобезопасный за счёт атомиков счётчик
 */
@ThreadSafe
public class CASCount<T> {
    private final AtomicReference<Integer> count = new AtomicReference<>();

    /**
     * Ищем момент, чтобы увеличить счетчик посредством while
     * Моментом будет считаться совпадение значения взятого в tempVal и
     * полученного в момент фактического применения CAS операции
     */
    public void increment() {
        Integer tempVal;
        do {
            tempVal = count.get();
        } while (!count.compareAndSet(tempVal, tempVal + 1));
    }

    public int get() {
        return count.get();
    }
}