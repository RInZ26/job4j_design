package ru.job4j.it;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 Класс - -итератор для четных чисел

 @author RinZ26 */
public class EvenNumbersIterator implements Iterator<Integer> {
    /**
     Итерируемый массив
     */
    private int[] data;
    /**
     Указатель на текущий элемент в массиве
     */
    private int pointer;

    /**
     Основной конструктор

     @param data
     - итерируемый массив
     */
    public EvenNumbersIterator(int[] data) {
        this.data = data;
    }

    /**
     Проверка есть ли четный элемент дальше, если есть ? меняет pointer и
     возвращает true : false

     @return
     */
    @Override
    public boolean hasNext() {
        boolean result = false;
        for (int c = pointer; c < data.length; c++) {
            if (isEven(data[c])) {
                result = true;
                pointer = c;
                break;
            }
        }
        return result;
    }

    /**
     После проверки на hasNext - возвращает либо элемент с увеличением
     pointer'a, либо выбрасывает exception

     @return ~
     */
    @Override
    public Integer next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return data[pointer++];
    }

    /**
     Проверка, четное ли число, или нет

     @param number

     @return ~
     */
    public boolean isEven(int number) {
        return number % 2 == 0;
    }
}
