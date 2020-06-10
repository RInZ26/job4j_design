package ru.job4j.generic;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Класс - обертка для массивов
 *
 * @param <T>
 * 	тип массива
 */
public class SimpleArray<T> implements Iterable<T> {
    /**
     * Хранилище данных
     */
    private Object[] data;
    /**
     * Указатель на следующую свободную ячейку;
     */
    private int pointer;

    /**
     * Основной конструктор, где мы создаем массив объектов размером @param
     * size
     *
     * @param size
     * 	размер массива
     */
    public SimpleArray(int size) {
	this.data = new Object[size];
    }

    /**
     * Добавление элемента
     *
     * @param model
     * 	элемент
     *
     * @return можно ли добавить
     */
    public boolean add(T model) {
	if (pointer < data.length) {
	    data[pointer++] = model;
	    return true;
	}
	return false;
    }

    /**
     * Присвоение значения элементу
     *
     * @param index
     * 	- индекс
     * @param model
     * 	- новое значение
     *
     * @return успех операции в зависимости от isIndexValid
     */
    public boolean set(int index, T model) {
	boolean result = isIndexValid(index);
	if (result) {
	    data[index] = model;
	}
	return result;
    }

    /**
     * Удаление элемента из массива путем смещения всех ячеек влево и заNULLение
     * последнего элемента pointer также уменьшается
     *
     * @param index
     * 	индекс удаляемого элемента
     *
     * @return успех операции в зависимости от isIndexValid
     */
    public boolean remove(int index) {
	boolean result = isIndexValid(index);
	if (result) {
	    if (data.length - 1 - index >= 0) {
		System.arraycopy(data, index + 1, data, index,
				 data.length - 1 - index);
	    }
	    data[data.length - 1] = null;
	    pointer--;
	}
	return result;
    }

    /**
     * Получение элемента по индексу с учетом isIndexValid
     *
     * @param index
     * 	- индекс
     *
     * @return элемент / null
     */
    public T get(int index) {
	if (isIndexValid(index)) {
	    return (T) data[index];
	} else {
	    return null;
	}
    }

    /**
     * Проверка, что индекс валидный
     *
     * @param index
     * 	индекс
     *
     * @return true/false;
     */
    private boolean isIndexValid(int index) {
	return index == Objects.checkIndex(index, data.length);
    }

    /**
     * Возвращает размер массива date
     *
     * @return ~
     */
    public int size() {
	return data.length;
    }

    @Override
    public Iterator<T> iterator() {
	return new Iterator<T>() {
	    /**
	     * Указатель внутри итератора на следующий элемент
	     */
	    int pointer;

	    /**
	     * Копируем содержимое иттерируемой коллекции
	     */
	    Object[] data = SimpleArray.this.data;

	    @Override
	    public boolean hasNext() {
		return pointer < data.length;
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
