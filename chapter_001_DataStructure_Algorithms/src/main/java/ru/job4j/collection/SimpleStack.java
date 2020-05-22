package ru.job4j.collection;

public class SimpleStack<T> {
    /**
     * Хранилище элементов
     */
    private ForwardLinked<T> linked = new ForwardLinked<T>();

    /**
     * Берем верхний элемент из стека, удаляя его из коллекциии
     *
     * @return элемент
     */
    public T pop() {
	return linked.deleteLast();
    }

    /**
     * Кладём элемент наверх стека
     *
     * @param value ~
     */
    public void push(T value) {
	linked.add(value);
    }
}