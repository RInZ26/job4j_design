package ru.job4j.threads.concurrent;

import java.util.function.Predicate;

import static java.lang.Thread.State.TERMINATED;

/**
 * Класс - демонстрация различных состояний у нити
 */
public class ThreadStateDemo {
    public static void main(String[] args) throws InterruptedException {
        var demo = new ThreadState(Thread.currentThread());
        Predicate<Thread> rule = (t -> t.getState()
                                        .equals(TERMINATED));
        while (!rule.test(demo.getFirst()) && !rule.test(demo.getSecond())) {
            Thread.sleep(1);
        }
        ThreadState.printInfo(demo.getFirst());
        ThreadState.printInfo(demo.getSecond());
        System.out.println("Работа завершена");
    }
}
