package ru.job4j.threads.concurrent.nonblockingalgo;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.LongAdder;

/**
 * Потокобезопасный за счёт атомиков счётчик
 */
@ThreadSafe
public class ThreadSafeCounter<T> {
    private final LongAdder count = new LongAdder();

    public void increment() {
        count.increment();
    }

    public int get() {
        return count.intValue();
    }
}