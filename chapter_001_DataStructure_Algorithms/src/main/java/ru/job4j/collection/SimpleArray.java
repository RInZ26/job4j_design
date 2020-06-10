package ru.job4j.collection;

import java.util.*;

/**
 * Динамический вариант generic.SimpleArray
 *
 * @param <T>
 * 	дженерик
 */
public class SimpleArray<T> implements Iterable<T> {
    /**
     * Константа дефолтного размера коллекции для конструктора без параметров
     * (подражание ArrayList)
     */
    public static final int DEFAULT_SIZE = 10;
    /**
     * Отражает "версию" коллекции. Если было внесено какое-то изменение,
     * счетчик увеличивается. Используется для корректной работы итераторов
     */
    public int versionOfCollection;
    /**
     * Хранилище данных
     */
    private Object[] data;

    /**
     * Количество элементов, которые есть в data, аналог list.size и в то же
     * время является аналогом pointer, чтоб мы знали, куда добавлять элементы
     */
    private int countOfElements;

    /**
     * Конструктор, где data присваивается значение по умолчанию
     */
    public SimpleArray() {
	data = new Object[DEFAULT_SIZE];
    }

    /**
     * ~ кастомное значение
     *
     * @param size
     * 	кастомное значение размеры
     */
    public SimpleArray(int size) {
	data = new Object[size];
    }

    /**
     * Возвращает элемет, если index в норме
     *
     * @param index
     * 	индекс
     *
     * @return Элемент или IndexOutOfBounds упадёт
     */
    public T get(int index) {
	return index == Objects.checkIndex(index, countOfElements)
	       ? (T) data[index] : null;
    }

    /**
     * Добавление элемента в коллекцию с поддержкой динамичесого расширения
     *
     * @param model
     * 	добавляемый элемент
     */
    public void add(T model) {
	if (countOfElements + 1 == data.length) {
	    dataSizeIsGrows();
	}
	data[countOfElements++] = model;
	versionOfCollection++;
    }

    /**
     * getter для countOfElements
     *
     * @return ~
     */
    public int size() {
	return countOfElements;
    }

    /**
     * Пересоздание массива с увеличением длины массива в два раза (как эталон)
     */
    private void dataSizeIsGrows() {
	data = Arrays.copyOf(data, data.length * 2);
    }

    @Override
    public Iterator<T> iterator() {
	return new Iterator<T>() {
	    /**
	     * Отслеживание изменений между версиями коллекций
	     */
	    private int currentVersionOfCollection =
		    SimpleArray.this.versionOfCollection;

	    /**
	     * Ссылка на итерируемую коллекцию
	     */
	    private Object[] data = SimpleArray.this.data;

	    /**
	     * Указатель на следующий элемент, работает в паре с SimpleArray.countOfElements (его getter size())
	     */
	    private int pointer;

	    @Override
	    public boolean hasNext() {
		if (currentVersionOfCollection
			!= SimpleArray.this.versionOfCollection) {
		    throw new ConcurrentModificationException();
		}
		return pointer < size();
	    }

	    @Override
	    public T next() {
		if (!hasNext()) {
		    throw new NoSuchElementException();
		}
		return (T) data[pointer++];
	    }
	};
    }
}