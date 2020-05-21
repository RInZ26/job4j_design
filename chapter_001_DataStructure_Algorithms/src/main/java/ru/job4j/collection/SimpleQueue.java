package ru.job4j.collection;

public class SimpleQueue<T> {
    /**
     * Количество элементов в очереди
     */
    private int countOfElements;

    /**
     * Стэк для хранения данных
     */
    private final SimpleStack<T> in = new SimpleStack<>();
    /**
     * Стэк для перебора
     */
    private final SimpleStack<T> out = new SimpleStack<>();

    /**
     * Извращенский перебор очереди через два стека
     * Сначала достаем все элементы, кроме последнего
     * В out получается очередь задом наперед, поэтому мы снова в цикле перекидываем её в in.
     *
     * @return первый элемент
     */
    public T poll() {
	countOfElements--;
	for (int c = 0; c < countOfElements; c++) {
	    out.push(in.pop());
	}
	T result = in.pop();
	for (int c = 0; c < countOfElements; c++) {
	    in.push(out.pop());
	}
	return result;
    }

    /**
     * Передаем элемент в очередь
     * Т.к. у нас push в стэке не boolean, необходим счетчик элементов в виде countOfelements
     * @param value ~
     */
    public void push(T value) {
	in.push(value);
	countOfElements++;
    }

    /**
     * Геттер для countOfElements.
     * @return ~
     */
    public int size() {
        return countOfElements;
    }
}