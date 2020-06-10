package ru.job4j.collection;

import java.util.Iterator;

/**
 * Класс - простенький set, испольщуюзий SimpleArray Гарантирует отсутствие
 * дублей
 *
 * @author RinZ26
 */
public class SimpleSet<T> implements Iterable<T> {
    /**
     * Наше хранилище данных
     */
    private SimpleArray<T> data = new SimpleArray<>();

    /**
     * Метод добавления в data, если дубль - не пускает
     *
     * @return true - добавление успешно, false - если дубль
     */
    public boolean add(T element) {
	boolean result = false;
	if (isUnique(element)) {
	    data.add(element);
	    result = true;
	}
	return result;
    }

    /**
     * Проверка есть ли такой элемент в data (уникальнось)
     *
     * @param element
     * 	элемент
     *
     * @return ~
     */
    private boolean isUnique(T element) {
	boolean result = true;
	for (T dataElement : data) {
	    if (dataElement.equals(element)) {
		result = false;
		break;
	    }
	}
	return result;
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<T> iterator() {
	return data.iterator();
    }
}
