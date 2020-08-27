package ru.job4j.threads.concurrent;

/**
 * Класс для демонстрации работы sleep и небольшой хинт-абуз с изменением
 * информации с \r
 */
public class Wget {
    public static void main(String[] args) throws InterruptedException {
        Thread loadThread = new Thread(() -> {
            for (int c = 0; c <= 100; c++) {
                System.out.print("\r Loading: " + c + "%");
                try {
                    Thread.sleep(300);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        });
        loadThread.start();
        loadThread.join();
    }
}
