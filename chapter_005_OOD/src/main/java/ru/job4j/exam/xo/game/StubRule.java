package ru.job4j.exam.xo.game;

import java.util.function.Predicate;

public class StubRule<T extends Game> implements Rule<T> {
    private Predicate<T> rule;

    public StubRule(Predicate<T> rule) {
        this.rule = rule;
    }

    @Override
    public Predicate<T> getRule() {
        return rule;
    }
}
