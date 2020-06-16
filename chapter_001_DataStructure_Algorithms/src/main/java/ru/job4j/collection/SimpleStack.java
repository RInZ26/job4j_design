package ru.job4j.collection;

public class SimpleStack<T> {
    /**
     Количество элементов
     */
    private int countOfElements;
    /**
     Хранилище элементов
     */
    private ForwardLinked<T> linked = new ForwardLinked<T>();

    /**
     геттер для countOfElements
     */
    public int size() {
        return countOfElements;
    }

    /**
     Берем верхний элемент из стека, удаляя его из коллекциии

     @return элемент
     */
    public T pop() {
        countOfElements--;
        return linked.deleteLast();
    }

    /**
     Кладём элемент наверх стека

     @param value
     ~
     */
    public void push(T value) {
        linked.add(value);
        countOfElements++;
    }
}