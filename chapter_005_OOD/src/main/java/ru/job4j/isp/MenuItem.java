package ru.job4j.isp;

/**
 * Расширение Hierarchy с учетом специфичных методов для меню
 * @param <T>
 */
public interface MenuItem<T> extends Hierarchy<T>, Action, Displayed {
    String getName();
}
