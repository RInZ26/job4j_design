package ru.job4j.kiss;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

/**
 * Kiss - Keep It Simple and Short (неспроста созвучно с kys)
 */
public class MaxMin {
    /**
     * Находит максимум в коллекции по компаратору
     */
    public <T> T max(List<T> values, Comparator<T> comparator) {
        return checker(values, comparator, (comp -> comp > 0));
    }

    /**
     * Находит минимум в коллекции по компаратору
     */
    public <T> T min(List<T> values, Comparator<T> comparator) {
        return checker(values, comparator, (comp -> comp < 0));
    }

    /**
     * Общий метод, который по предикату определяет как трактовать результат
     * комперинга
     */
    private <T> T checker(List<T> values, Comparator<T> comparator,
                          Predicate<Integer> rule) {
        if (values.size() == 0) {
            return null;
        }
        T result = values.get(0);
        for (T val : values) {
            result = rule.test(comparator.compare(val, result)) ? val : result;
        }
        return result;
    }
}