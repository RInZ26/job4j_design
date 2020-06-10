package ru.job4j.collection;

/**
 * Класс - очередь. Данные хранятся по необходимости, либо в in, либо в out,
 * либо в in+out Поэтому для отслеживания размера есть счетчик
 *
 * @param <T>
 */
public class SimpleQueue<T> {
    /**
     * Стэк для хранения данных в прямом порядке
     */
    private final SimpleStack<T> in = new SimpleStack<>();
    /**
     * Стэк для хранения элементов, готовых к выдаче в порядке очереди
     */
    private final SimpleStack<T> out = new SimpleStack<>();
    /**
     * Количество элементов в очереди
     */
    private int countOfElements;

    /**
     * out отражает in наоборот. Пока out непустой, мы можем работать с ним,
     * накапливая данные в In. Как только он опустеет - перекидываем в него из
     * in
     * <p>
     *
     * @return первый элемент
     */
    public T poll() {
	countOfElements--;
	if (out.size() == 0) {
	    while (0 < in.size()) {
		out.push(in.pop());
	    }
	}
	return out.pop();
    }

    /**
     * Передаем элемент в очередь Т.к. у нас push в стэке не boolean, необходим
     * счетчик элементов в виде countOfelements
     *
     * @param value
     * 	~
     */
    public void push(T value) {
	in.push(value);
	countOfElements++;
    }

    /**
     * Геттер для countOfElements.
     *
     * @return ~
     */
    public int size() {
	return countOfElements;
    }
}