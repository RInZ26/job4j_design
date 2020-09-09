package ru.job4j.threads.concurrent;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import ru.job4j.collection.SimpleArray;

import java.util.Iterator;

/**
 * Потокобезопаная коллекция - общий ресурс между нитями
 */
@ThreadSafe
public class SingleLockList<T> implements Iterable<T> {
    /**
     * Раз у нас композиция SimpleArray, то пусть и блокировки, связанные
     * с ней будут лежать на ответственности монитора этой коллекции
     */
    @GuardedBy("list")
    private final SimpleArray<T> list = new SimpleArray<>();

    public void add(T value) {
        synchronized (list) {
            list.add(value);
        }
    }

    public T get(int index) {
        synchronized (list) {
            return list.get(index);
        }
    }

    /**
     * Хотим скопировать коллекцию для того, чтобы итератор работал с ней,
     * таким образом обеспечивая fail-safe режим. самое просто по сути -
     * создать ещё один SimpleArray и занести туда все элементы.
     */
    public SimpleArray<T> copy() {
        var result = new SimpleArray<T>();
        synchronized (list) {
            list.forEach(result::add);
        }
        return result;
    }

    @Override
    public Iterator<T> iterator() {
        return copy().iterator();
    }
}