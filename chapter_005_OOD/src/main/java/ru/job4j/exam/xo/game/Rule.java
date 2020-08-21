package ru.job4j.exam.xo.game;

import java.util.function.Predicate;

public interface Rule<T> {
    Predicate<? super T> getRule();
}
