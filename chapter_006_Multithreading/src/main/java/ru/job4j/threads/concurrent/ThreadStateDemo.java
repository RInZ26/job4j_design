package ru.job4j.threads.concurrent;

import java.util.function.Predicate;

import static java.lang.Thread.State.TERMINATED;

/**
 * Класс - демонстрация различных состояний у нити
 */
public class ThreadStateDemo {
    public static void main(String[] args) {
        var demo = new ThreadState(Thread.currentThread());
        try {
            demo.getFirst()
                .join();
            demo.getSecond()
                .join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Predicate<Thread> rule = (t -> t.getState()
                                        .equals(TERMINATED));
        if (rule.test(demo.getFirst()) && rule.test(demo.getSecond())) {
            System.out.println("Работа завершена");
        }
    }
}
