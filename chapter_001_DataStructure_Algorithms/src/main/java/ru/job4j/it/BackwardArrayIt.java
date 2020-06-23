package ru.job4j.it;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 Класс - итератор для массива. Возвращает элементы в обратном порядке.

 @author RinZ26 */

public class BackwardArrayIt implements Iterator<Integer> {
    /**
     Массив данных
     */
    private final int[] data;
    /**
     Указатель на текущий обрабатываемый элемент
     */
    private int point;

    public BackwardArrayIt(int[] data) {
        this.data = data;
        point = data.length - 1;
    }

    /**
     Проверка, есть ли следующий элемент

     @return ~
     */
    @Override
    public boolean hasNext() {
        return point >= 0;
    }

    /**
     Возвращаем элемент в обратном порядке с учетом проверки hasNext

     @return ? элемент : Exception
     */
    @Override
    public Integer next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return data[point--];
    }
}