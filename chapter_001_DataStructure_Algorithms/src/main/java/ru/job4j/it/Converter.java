package ru.job4j.it;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Класс - для работы с итераторами
 */
public class Converter {
    /**
     * Возвращает итератор, перебирающий все элементы из различных итераторов
     *
     * @param it итератор итераторов
     * @return ~
     */
    Iterator<Integer> convert(Iterator<Iterator<Integer>> it) {
	return new Iterator<Integer>() {
	    /**
	     * Текущий итератор, что-то а-ля pointer в обычном итераторе
	     */
	    Iterator<Integer> currentIterator = Collections.emptyIterator();

	    /**
	     * Проверка, есть ли хоть где-нибудь элемент
	     * @return ~
	     */
	    @Override
	    public boolean hasNext() {
		while (it.hasNext() && !currentIterator.hasNext()) {
		    currentIterator = it.next();
		}
		return currentIterator.hasNext();
	    }

	    /**
	     * Берем следущий элемент, если есть
	     * @return ~
	     */
	    @Override
	    public Integer next() {
		if (!hasNext()) {
		    throw new NoSuchElementException();
		}
		return currentIterator.next();
	    }
	};
    }
}