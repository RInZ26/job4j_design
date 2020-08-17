package ru.job4j.isp;

import java.util.Collection;
import java.util.List;

/**
 * Интерфейс - поддерживающий иерархичность, то есть, способность двигаться в
 * разные стороны наподобие дерева. Элемент может быть трёх видов: корень -
 * нет родителя, лист - нет детей, ветка - есть и то, и другое.
 * За определение типа отвечают методы isLeaf и isRoot
 *
 * @param <T> Тип элементов в иерархической цепочке
 */
public interface Hierarchy<T> {
    /**
     * Получение корня
     */
    T getParent();

    /**
     * Получение листьев
     */
    Collection<T> getChildren();

    /**
     * Первый элемент в дереве
     */
    boolean isRoot();

    /**
     * Последний элемент на "ветке"
     */
    boolean isLeaf();

    /**
     * Глубина относительно корня
     */
    int getDeep();

    void addChild(List<T> children);

    void addChild(T child);

    void setParent(T parent);

    void setChildren(Collection<T> collection);

    void removeChild(T child);
}
