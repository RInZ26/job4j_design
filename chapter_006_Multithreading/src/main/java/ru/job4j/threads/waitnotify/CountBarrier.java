package ru.job4j.threads.waitnotify;

import net.jcip.annotations.GuardedBy;

/**
 * При достижении count == total поток может работать
 */
public class CountBarrier {
    private final Object monitor = this;
    private final int total;
    @GuardedBy("monitor")
    private int count = 0;

    public CountBarrier(final int total) {
        this.total = total;
    }

    /**
     * Используем NotifyAll при увеличении счетчика, чтобы ВСЕ потоки
     * првоерили своё условие
     */
    public void count() {
        synchronized (monitor) {
            count++;
            monitor.notifyAll();
        }
    }

    /**
     * wait снимит блок с монитора, сам поток будет заблокирован до тех пор
     * пока count не изменится и его не уведомят об этом
     */
    public void await() {
        synchronized (monitor) {
            try {
                while (count != total) {
                    monitor.wait();
                }
                System.out.println(Thread.currentThread()
                                         .getName() + " finally is working");
            } catch (InterruptedException ie) {
                ie.printStackTrace();
                Thread.currentThread()
                      .interrupt();
            }
        }
    }
}
