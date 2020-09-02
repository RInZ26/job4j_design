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
    private final Node next;
    private final T value;

    public Node(Node next, T value) {
        this.next = next;
        this.value = value;
    }

    public Node getNext() {
        return next;
    }

    public T getValue() {
        return value;
    }

    /**
     * Альтернатива "сеттерам" для поддержки Immutable
     */
    public Node<T> updateNext(Node next) {
        return new Node<T>(next, this.value);
    }

    public Node<T> updateValue(T value) {
        return new Node<T>(this.next, value);
    }
}