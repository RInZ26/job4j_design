package ru.job4j.threads.concurrent.linked;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;

/**
 * Если хотим поддерживать иммутабельность объектов с изменяющимся полями,
 * нужно вместо сеттеров использовать пересоздание объектов, а все поля
 * сделать final
 */
@NotThreadSafe
@Immutable //сама аннотация ничего не делает, она просто маркер
public class Node<T> {
    private final Node<T> next;
    private final T value;

    public Node(Node<T> next, T value) {
        this.next = next;
        this.value = value;
    }

    public Node<T> getNext() {
        return next;
    }

    public T getValue() {
        return value;
    }

    /**
     * Альтернатива "сеттерам" для поддержки Immutable
     */
    public Node<T> updateNext(Node<T> next) {
        return new Node<>(next, this.value);
    }

    public Node<T> updateValue(T value) {
        return new Node<>(this.next, value);
    }
}