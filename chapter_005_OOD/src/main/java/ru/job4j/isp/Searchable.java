package ru.job4j.isp;

import java.util.List;
import java.util.function.Predicate;

/**
 * Интерфейс, агреггирующий методы поиска - просто идейно он для меню неплох
 *
 * @param <T>
 */
public interface Searchable<T> {
    /**
     * По определенному ключу находит элемент в структуре (через equals и
     * итератор к примеру)
     *
     * @param key - предикат по которому что-то ищем
     *
     * @return элементы
     */
    List<T> findBy(Predicate<T> key);

    /**
     * обычный contains для проверки есть ли элемент или нет
     */
    boolean contains(T element);
}
