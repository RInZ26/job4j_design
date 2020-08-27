package ru.job4j.threads.concurrent;

/**
 * Класс, запускающий внутри себя потоки и имеющий методы отслеживания сосотяния
 */
public class ThreadState {
    /**
     * Потоки, которые конкурируют друг с другом за запуск printAllInfo
     */
    private Thread first;
    private Thread second;
    /**
     * главная нить, переданная извне, всегда ожидающая из-за join
     */
    private Thread main;

    public ThreadState(Thread main) {
        this.main = main;
        first = new Thread(() -> {
            for (int c = 0; c < 5; c++) {
                try {
                    printAllInfo();
                    Thread.sleep(100);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }, "first");
        second = new Thread(() -> {
            for (int c = 0; c < 5; c++) {
                try {
                    printAllInfo();
                    Thread.sleep(100);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }, "second");
        first.start();
        second.start();
    }

    public Thread getFirst() {
        return first;
    }

    public Thread getSecond() {
        return second;
    }

    /**
     * Синхронизирующийся метод, выводящий состояние всех потоков
     */
    private synchronized void printAllInfo() {
        System.out.println("***  Info from " + Thread.currentThread()
                                                     .getName());
        printInfo(first);
        printInfo(second);
        printInfo(main);
    }

    /**
     * Простенький вывод на экран информации о нити
     */
    static void printInfo(Thread thread) {
        System.out.printf("%s -> %s%s", thread.getName(), thread.getState(),
                          System.lineSeparator());
    }
}
