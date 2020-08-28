package ru.job4j.threads.concurrent;

/**
 * Класс - имитация вращения в консоли
 */
public class ConsoleProgress implements Runnable {
    /**
     * При InterruptedException флаг прерывания сбрасывается, поэтому его
     * нужно обратно ставить через Thread.currentThread.interrupt()
     * В противном случае isInterrupt() == false;
     * Можно конечно и просто оставить true-catch, всё равно поток будет
     * terminated, но это как-то нехорошо выглядит.
     */
    @Override
    public void run() {
        try {
            while (!Thread.currentThread()
                          .isInterrupted()) {
                for (int c = 0; c < 2; c++) {
                    System.out.print("\r load: " + (c % 2 == 0 ? "|" : "\\|/"));
                    Thread.sleep(500);
                }
            }
        } catch (InterruptedException ie) {
            System.out.println(System.lineSeparator() + Thread.currentThread()
                                                              .isInterrupted());
            Thread.currentThread()
                  .interrupt();
            System.out.println(Thread.currentThread()
                                     .isInterrupted());
        }
    }

    public static void main(String[] args) {
        var thread = new Thread(new ConsoleProgress(), "ballThread");
        thread.start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
            Thread.currentThread()
                  .interrupt();
        }
        thread.interrupt();
    }
}
