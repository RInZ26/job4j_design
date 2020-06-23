package ru.job4j.collection;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 Упрощенная реализиация LinkedList

 @param <T>
 дженерик
 */
public class SimpleLinkedList<T> implements Iterable<T> {
    /**
     Указатель на первый элемент цепочки
     */
    private Node<T> start;
    /**
     Указатель на последний элемент цепочки
     */
    private Node<T> finish;
    /**
     Отражает "версию" коллекции. Если было внесено какое-то изменение,
     счетчик увеличивается. Используется для корректной работы итераторов
     */
    private int versionOfCollection;
    /**
     Количество элементов, которые есть связном списке, аналог list.size
     */
    private int countOfElements;

    /**
     Конструктор с элегантными пустыми Node, для красивой реализации методов
     add, get.
     */
    public SimpleLinkedList() {
        start = new Node<>();
        finish = new Node<>();
        start.next = finish;
        finish.previous = start;
    }

    /**
     Добавление элемента в связный список. Элегантно пользуемся пустыми Nodaми
     и через них формируем список. То есть он выглядит так: До первого Add:
     (FS)fakeStart( <- null . -> FF) -   (FF)fakeFinish(<- FS, ->null) После:
     FS (<-null, -> NewNode) -  NewNode (<- FS, -> FF) - FF (<-NewNode,->
     null) И элементов в списке будет фиксироваться именно 1.

     @param element
     элемент
     */
    public void add(T element) {
        Node<T> addedNode = new Node<T>(null, finish, element);
        finish.previous.next = addedNode;
        finish.previous = addedNode;
        countOfElements++;
        versionOfCollection++;
    }

    /**
     Взятие элемента по индексу. В зависимости от индекса, мы бежим либо в
     прямую сторону, либо в обратную resultNode -  Т.к. у нас первый и
     последний элемент в списке является заглушкой, то здесь в качестве
     первого элемента выступает start.next || finish.previous

     @param index
     - индекс

     @return элемент найденной ноды / IndexOutOfBounds; Null'a не должно быть
     по логике никогда
     */
    public T get(int index) {
        if (index == Objects.checkIndex(index, countOfElements)) {
            Node<T> resultNode;
            if (index <= countOfElements / 2) {
                resultNode = start.next;
                for (int c = 0; c < index; c++) {
                    resultNode = resultNode.next;
                }
            } else {
                resultNode = finish.previous;
                for (int c = countOfElements; c > index; c--) {
                    resultNode = resultNode.previous;
                }
            }
            return resultNode.element;
        } else {
            return null;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            /**
             * Отслеживание изменений между версиями коллекций
             */
            private int currentVersionOfCollection = SimpleLinkedList.this.versionOfCollection;
            /**
             * Поинтер на ТЕКУЩИЙ Node, но с учетом того, что мы всегда проверяем currentNode.next
             *  Т.к. у нас первый и последний элемент в списке является заглушкой,
             *  то здесь в качестве первого элемента выступает start.next, а
             *  в качестве проверки на окочание - finish
             */
            private Node<T> currentNode = new Node<T>(null,
                                                      SimpleLinkedList.this.start.next,
                                                      null);

            @Override
            public boolean hasNext() {
                if (currentVersionOfCollection
                        != SimpleLinkedList.this.versionOfCollection) {
                    throw new ConcurrentModificationException();
                }
                return currentNode.next != finish;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                currentNode = currentNode.next;
                return currentNode.element;
            }
        };
    }

    /**
     Реализация Node из linkedList Идея структуры с указателем на предыдущий
     элемент и следующий

     @param <T>
     */
    private static class Node<T> {
        /**
         Ссылка на прошлый элемент
         */
        private Node<T> previous;
        /**
         Ссылка на следующий
         */
        private Node<T> next;
        /**
         Элемент
         */
        private T element;

        /**
         Основной конструктор

         @param previous
         Ссылка на прошлую
         @param next
         Ссылка на следующую
         @param element
         значение
         */
        Node(Node<T> previous, Node<T> next, T element) {
            this.previous = previous;
            this.next = next;
            this.element = element;
        }

        /**
         Для создания null-заглушек в конструкторе SimpleLinkedList
         */
        Node() {
        }
    }
}
