package ru.job4j.isp;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Все элемент меню хранятся в HashSet для достижения скорости поиска в O(1).
 * Минус в том, что удаление будет выглядеть немного громоздко, по сути O
 * (глубина элемента) с другой стороны есть вариант хранить элементы в виде
 * одной ноды аля глубокий linkedList, тогда поиск будет
 * O(n), как и удаление, что хуже
 *
 * @param <T>
 */
public class SimpleMenu<T extends MenuItem<T>> implements Menu<T> {
    private HashSet<T> menuSet = new HashSet<>();

    @Override
    public boolean add(T menuItem) {
        menuSet.add(menuItem);
        return true;
    }

    @Override
    public boolean add(T root, T leaf) {
        if (menuSet.contains(root)) {
            root.addChild(leaf);
            add(leaf);
            return true;
        }
        return false;
    }

    @Override
    public List<T> removeByKey(Predicate<T> rule) {
        //todo
        return null;
    }

    @Override
    public boolean remove(T menuItem) {
        boolean result = menuSet.contains(menuItem);
        if (result) {
            removeCascade(menuItem);
            if (!menuItem.isRoot()) {
                menuItem.getParent()
                        .removeChild(menuItem);
            }
            menuSet.remove(menuItem);
        }
        return result;
    }

    /**
     * Каскадное удаление элемента с удалением из сета
     */
    private void removeCascade(T menuItem) {
        if (!menuItem.isLeaf()) {
            menuItem.getChildren()
                    .forEach(this::removeCascade);
        }
        if (!menuItem.isRoot()) {
            menuItem.getParent()
                    .removeChild(menuItem);
            menuSet.remove(menuItem);
        }
    }

    @Override
    public List<T> findBy(Predicate<T> key) {
        List<T> result = new ArrayList<>();
        for (T e : menuSet) {
            if (key.test(e)) {
                result.add(e);
            }
        }
        return result;
    }

    @Override
    public boolean contains(T element) {
        return menuSet.contains(element);
    }

    @Override
    public Collection<T> getCollection() {
        return menuSet;
    }

    @Override
    public void display() {
        for (T item : menuSet.stream()
                             .filter(e -> e.isRoot())
                             .sorted(Comparator.comparing(MenuItem::getName))
                             .collect(Collectors.toList())) {
            displayChildren(item);
        }
    }

    private void displayChildren(T item) {
        item.display();
        if (!item.isLeaf()) {
            item.getChildren()
                .stream()
                .sorted(Comparator.comparing(MenuItem::getName))
                .forEach(this::displayChildren);
        }
    }
}
