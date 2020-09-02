package ru.job4j.threads.concurrent;

/**
 * Работа с volatile. В данном случае проблем с многопоточкой не будет,
 * потому что мы всегда будем обращаться к фактическому значению переменной,
 * которое содержится в RAM, а не в регистрах
 */
public class DCLSingleton {
    private static volatile DCLSingleton inst;

    private DCLSingleton() {
    }

    /**
     * В данном случае inst всегда будет одно и то же для всех потоков
     */
    public static DCLSingleton instOf() {
        return inst == null ? new DCLSingleton() : inst;
    }
}
