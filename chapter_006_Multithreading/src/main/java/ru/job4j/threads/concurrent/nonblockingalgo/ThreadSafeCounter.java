package ru.job4j.threads.concurrent.nonblockingalgo;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Потокобезопасный за счёт атомиков счётчик
 */
@ThreadSafe
public class ThreadSafeCounter {
    /**
     * Т.к. поддерживает CAS операции
     */
    private final AtomicReference<Integer> atomicInc = new AtomicReference<>(0);

    /**
     * Трактуем эту операцию как CAS и увеличиваем только в том случае, если
     * значение ещё никто не поменял из других потоков
     */
    public void increment() {
        int expected = atomicInc.get();
        atomicInc.compareAndSet(expected, expected + 1);
    }

    /**
     * За счёт volatile value внутри AtomicReference должно вернуться
     * актуальное значение на данный момент
     */
    public int get() {
        return atomicInc.get();
    }
}