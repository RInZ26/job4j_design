package ru.job4j.collection;

public class SimpleStack<T> {
    private ForwardLinked<T> linked = new ForwardLinked<T>();

    /**
     * Берем верхний элемент из стека, удаляя его из коллекциии
     *
     * @return элемент
     */
    public T pop() {
	T element = linked.getLastElement();
	linked.deleteLast();
	return element;
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