package ru.job4j.collection;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ForwardLinked<T> implements Iterable<T> {
    /**
     "Голова" односвязного списка
     */
    private Node<T> head;

    /**
     Добавление элемента с использованием findLast

     @param value
     ~
     */
    public void add(T value) {
        Node<T> node = new Node<T>(value, null);
        Node<T> tail = getLastNode();
        if (tail == null) {
            head = node;
        } else {
            tail.next = node;
        }
    }

    /**
     Удаление head элемента
     */
    public void deleteFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        head = head.next;
    }


    /**
     Удаление последнего элемента Учитывается, что head может быть равен null
     или head - единственный элемент в списке Возвращает значение удаленного
     */
    public T deleteLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        T element;
        if (head.next == null) {
            element = head.value;
            head = null;
        } else {
            Node<T> penultNode = head;
            while (penultNode.next.next != null) {
                penultNode = penultNode.next;
            }
            element = penultNode.next.value;
            penultNode.next = null;
        }
        return element;
    }

    /**
     Проверка пуста ли коллекция -  по сути проверка на head==null, ведь по
     head и можно сделать вывод, пуста коллекция или нет

     @return ~
     */
    public boolean isEmpty() {
        return head == null;
    }

    /**
     Возвращает последний Node или null, если head == null

     @return last Node
     */
    private Node<T> getLastNode() {
        Node<T> lastNode = null;
        if (!isEmpty()) {
            lastNode = head;
            while (lastNode.next != null) {
                lastNode = lastNode.next;
            }
        }
        return lastNode;
    }


    /**
     Переворот списка. firstNode - указатель на первый, элемент, secondNode -
     на второй элемент. OldThreadOfNodes - чтобы не потерять прошлую цепочку,
     берется как secondNode.next и далее. Идея следующая: Рассматривается пара
     Node, ссылка из второй переопределяется на первую(secondNode.next =
     firstNode) и всё сдвигается
     */
    public void revert() {
        if (!isEmpty()) {
            Node<T> firstNode = head;
            Node<T> secondNode = head;
            Node<T> oldThreadOfNodes = head.next;
            while (oldThreadOfNodes != null) {
                secondNode = oldThreadOfNodes;
                oldThreadOfNodes = oldThreadOfNodes.next;
                secondNode.next = firstNode;
                firstNode = secondNode;
            }
            head = secondNode;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            Node<T> node = head;

            @Override
            public boolean hasNext() {
                return node != null;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T value = node.value;
                node = node.next;
                return value;
            }
        };
    }

    private static class Node<T> {
        T value;
        Node<T> next;

        public Node(T value, Node<T> next) {
            this.value = value;
            this.next = next;
        }
    }

}