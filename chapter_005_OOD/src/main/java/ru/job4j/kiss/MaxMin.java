package ru.job4j.kiss;

import java.util.Comparator;
import java.util.List;

/**
 * Kiss - Keep It Simple and Short (неспроста созвучно с kys)
 */
public class MaxMin {
    /**
     * Находит максимум в коллекции по компаратору
     */
    public <T> T max(List<T> values, Comparator<T> comparator) {
        return checker(values, comparator);
    }

    /**
     * Находит минимум в коллекции по компаратору
     */
    public <T> T min(List<T> values, Comparator<T> comparator) {
        return checker(values, comparator.reversed());
    }

    /**
     * Общий метод, который по компаратору ищет как бы максимум(>), поэтому в
     * min мы используем reversed
     */
    private <T> T checker(List<T> values, Comparator<T> comparator) {
        if (values.size() == 0) {
            return null;
        }
        T result = values.get(0);
        for (T val : values) {
            result = comparator.compare(val, result) > 0 ? val : result;
        }
        return result;
    }
}