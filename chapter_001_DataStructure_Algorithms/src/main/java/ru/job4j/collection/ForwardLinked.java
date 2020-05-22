package ru.job4j.collection;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ForwardLinked<T> implements Iterable<T> {
    /**
     * "Голова" односвязного списка
     */
    private Node<T> head;

    /**
     * Добавление элемента с использованием findLast
     *
     * @param value ~
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
     * Удаление head элемента
     */
    public void deleteFirst() {
	if (isEmpty()) {
	    throw new NoSuchElementException();
	}
	head = head.next;
    }


    /**
     * Удаление последнего элемента
     * Учитывается, что head может быть равен null или head - единственный элемент в списке
     * Дублирует идею getlastElement и возвращает значение удаленного
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
     * Проверка пуста ли коллекция -  по сути проверка на head==null, ведь по head и можно сделать вывод, пуста коллекция или нет
     *
     * @return ~
     */
    public boolean isEmpty() {
	return head == null;
    }

    /**
     * Возвращает последний Node или null, если head == null
     *
     * @return last Node
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
     * Возвращает элемент из последний Node или null, если её нет
     *
     * @return ~
     */
    private T getLastElement() {
	Node<T> lastNode = getLastNode();
	if (lastNode != null) {
	    return lastNode.value;
	} else {
	    return null;
	}
    }

    /**
     * Переворот списка.
     * revertedHead - цепочка задом-наперёд
     * Идея захкватывать через getLast, добавлять в revertedNextNode и вызывать deleteLast
     * Продолжать это пока reveredNextNode.next не добежит до head
     */
    public void revert() {
	if (!isEmpty()) {
	    Node<T> revertedHead = getLastNode();
	    deleteLast();
	    Node<T> revertedNextNode = getLastNode();
	    if (revertedNextNode != null) {
		revertedHead.next = revertedNextNode;
		deleteLast();
		while (revertedNextNode != head) {
		    revertedNextNode.next = getLastNode();
		    if (revertedNextNode.next == null) {
			break;
		    }
		    deleteLast();
		    revertedNextNode = revertedNextNode.next;
		}
	    }
	    head = revertedHead;
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