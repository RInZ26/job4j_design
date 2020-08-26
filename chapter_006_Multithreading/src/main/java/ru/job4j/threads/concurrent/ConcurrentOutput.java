package ru.job4j.threads.concurrent;

/**
 * Класс-демостранция работы потоков
 */
public class ConcurrentOutput {
    public static void main(String[] args) {
        Thread first = new Thread(() -> System.out.println(
                Thread.currentThread()
                      .getName()), "first");
        Thread second = new Thread(() -> System.out.println(
                Thread.currentThread()
                      .getName()), "second");
        first.start();
        second.start();
        System.out.println(Thread.currentThread()
                                 .getName());
    }
}
