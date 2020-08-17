package ru.job4j.isp;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public interface Menu<T extends Hierarchy> extends Searchable<T>, Displayed {
    boolean add(T menuItem);

    /**
     * Добавление меню на определенный уровень
     *
     * @param root - menuItem родитель
     * @param leaf - добавляемый menuItem
     *
     * @return успех?
     */
    boolean add(T root, T leaf);

    /**
     * По ключу удаляет элемент меню
     *
     * @param rule условие, например имя
     *
     * @return удаленные элемент
     */
    List<T> removeByKey(Predicate<T> rule);

    /**
     * Удаление уже конкретно объекта
     *
     * @param menuItem удаляемый объект
     *
     * @return успех ?
     */
    boolean remove(T menuItem);

    Collection<T> getCollection();
}
