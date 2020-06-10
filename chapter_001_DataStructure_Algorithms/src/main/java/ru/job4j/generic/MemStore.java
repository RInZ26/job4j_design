package ru.job4j.generic;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс - хранилище
 *
 * @param <T>
 */
public final class MemStore<T extends Base> implements Store<T> {
    /**
     * Хранилище данных
     */
    private final List<T> mem = new ArrayList<>();

    /**
     * Добавление элемента
     *
     * @param model
     * 	элемент
     */
    @Override
    public void add(T model) {
	mem.add(model);
    }

    /**
     * Замена элемента по его id
     *
     * @param id
     * 	тянется из Base
     * @param model
     * 	заменяющий элемент
     *
     * @return true - операция удалась, false - такого id нет
     */
    @Override
    public boolean replace(String id, T model) {
	int indexOfReplacedElement = findIndexById(id);
	if (indexOfReplacedElement != -1) {
	    mem.set(indexOfReplacedElement, model);
	    return true;
	}
	return false;
    }

    /**
     * Удаление элемента по id Т.к. нельзя создать что-то вроде new Base(id) (с
     * учетом переопределения equals в нём), то удаление через
     * list.remove(Object) не выглядит хорошим Да, можно воспользоваться и
     * findById, но особой разницы между этим и list.remove(index) - не вижу.
     *
     * @param id
     * 	- тянется с Base
     *
     * @return перация удалась, false - такого id нет
     */
    @Override
    public boolean delete(String id) {
	int indexOfReplacedElement = findIndexById(id);
	if (indexOfReplacedElement != -1) {
	    mem.remove(indexOfReplacedElement);
	    return true;
	}
	return false;
    }

    /**
     * Поиск элемента по id (которое заложено в Base)
     *
     * @param id
     * 	- Base.id
     *
     * @return найденный элемент / null
     */
    @Override
    public T findById(String id) {
	for (T t : mem) {
	    if (t.getId().equals(id)) {
		return t;
	    }
	}
	return null;
    }

    /**
     * Находит id элемента в коллекции
     *
     * @return id / -1, если элемента нет
     */
    private int findIndexById(String id) {
	for (int c = 0; c < mem.size(); c++) {
	    if (mem.get(c).getId().equals(id)) {
		return c;
	    }
	}
	return -1;
    }
}