package ru.job4j.collection;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Упрощенная реализиация LinkedList
 *
 * @param <T> дженерик
 */
public class SimpleLinkedList<T> implements Iterable<T> {
    /**
     * * Отражает "версию" коллекции. Если было внесено какое-то изменение,
     * счетчик увеличивается. Используется для корректной работы итераторов
     */
    private int versionOfCollection;
    /**
     * Указатель на первый элемент цепочки
     */
    Node<T> start;
    /**
     * Указатель на последний элемент цепочки
     */
    Node<T> finish;

    /**
     * Количество элементов, которые есть связном списке, аналог list.size
     */
    private int countOfElements;

    /**
     * Добавление элемента в связный список.
     * Вначале обработка случая первого использоваения списка:
     * Если первого элемента нет, обычное присвоение.
     * Если есть первый, но нет последнего, тогда уже начинаем присваивать ссылки
     * <p>
     * В противном случае, если список имеет и start и finish, то работаем только с finish.
     *
     * @param element элемент
     */
    public void add(T element) {
	Node<T> addedNode = new Node<T>(finish, null, element);
	if (start == null) {
	    start = addedNode;
	} else if (finish == null) {
	    start.next = addedNode;
	    addedNode.previous = start;
	    finish = addedNode;
	} else {
	    finish.next = addedNode;
	    finish = addedNode;
	}
	countOfElements++;
	versionOfCollection++;
    }

    /**
     * Взятие элемента по индексу. В зависимости от индекса, мы бежим либо в прямую сторону, либо в обратную
     *
     * @param index - индекс
     * @return элемент найденной ноды / IndexOutOfBounds;
     * Null'a не должно быть по логике никогда
     */
    public T get(int index) {
	if (index == Objects.checkIndex(index, countOfElements)) {
	    Node<T> resultNode;
	    if (index <= countOfElements / 2) {
		resultNode = start;
		for (int c = 0; c < index; c++) {
		    resultNode = resultNode.next;
		}
	    } else {
		resultNode = finish;
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
	     */
	    private Node<T> currentNode = new Node(null, SimpleLinkedList.this.start, null);

	    @Override
	    public boolean hasNext() {
		if (currentVersionOfCollection != SimpleLinkedList.this.versionOfCollection) {
		    throw new ConcurrentModificationException();
		}
		return currentNode.next != null;
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
     * Реализация Node из linkedList
     * Идея структуры с указателем на предыдущий элемент и следующий
     *
     * @param <T>
     */
    private static class Node<T> {
	/**
	 * Ссылка на прошлый элемент
	 */
	Node<T> previous;
	/**
	 * Ссылка на следующий
	 */
	Node<T> next;
	/**
	 * Элемент
	 */
	T element;

	Node(Node<T> previous, Node<T> next, T element) {
	    this.previous = previous;
	    this.next = next;
	    this.element = element;
	}
    }
}
