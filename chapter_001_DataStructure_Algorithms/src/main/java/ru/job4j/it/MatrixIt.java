package ru.job4j.it;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Класс- итератор для двухмерного массива
 *
 * @author RinZ26
 */
public class MatrixIt implements Iterator<Integer> {
    /**
     * Массив данных
     */
    private final int[][] data;
    /**
     * Указатель на строку
     */
    private int row;
    /**
     * Указатель на столбец
     */
    private int column;

    /**
     * Основной конструктор
     *
     * @param data - иттерируемый массив
     */
    public MatrixIt(int[][] data) {
	this.data = data;
    }

    /**
     * Проверка, есть ли следующий массив
     * Без учета, что массив может быть null
     *
     * @return есть : нет
     */
    @Override
    public boolean hasNext() {
	boolean result = row < data.length && column < data[row].length;
	if (!result && row < data.length) {
	    row++;
	    column = 0;
	    return hasNext();
	}
	return result;
    }

    /**
     * Возвращает следующий элемент, если есть иначе Exception
     *
     * @return ~
     */
    @Override
    public Integer next() {
	if (!hasNext()) {
	    throw new NoSuchElementException();
	}
	if (column == data[row].length) {
	    row++;
	    column = 0;
	}
	return data[row][column++];
    }
}