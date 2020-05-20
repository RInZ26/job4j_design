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
     * Указатель на следующую строку
     */
    private int row;
    /**
     * Указатель на следующий столбец
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
     * Проверка, есть ли следующая ячейка
     * Если массив пустой - скипаем
     *
     * @return есть : нет
     */
    @Override
    public boolean hasNext() {
	while (row < data.length && (data[row].length == 0 || column == data[row].length)) {
	    row++;
	    column = 0;
	}
	return row < data.length && column < data[row].length;
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