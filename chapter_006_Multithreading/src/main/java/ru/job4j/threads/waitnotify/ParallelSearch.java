package ru.job4j.threads.waitnotify;

public class ParallelSearch {
    /**
     * Количество элементов в очереди
     */
    private static final int SIZE_OF_QUEUE = 3;

    public static void main(String[] args) {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(15);
        final Thread consumer = new Thread(() -> {
            while (!Thread.currentThread()
                          .isInterrupted()) {
                System.out.println(queue.poll());
            }
            Thread.currentThread()
                  .interrupt();
        });
        consumer.start();
        new Thread(() -> {
            for (int index = 0; index < SIZE_OF_QUEUE; index++) {
                queue.offer(index);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread()
                          .interrupt();
                }
            }
            consumer.interrupt();
        }).start();
    }
}