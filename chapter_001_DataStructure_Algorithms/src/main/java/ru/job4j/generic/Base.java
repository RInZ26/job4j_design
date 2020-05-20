package ru.job4j.generic;

import java.util.Objects;

/**
 * Класс - заготовка под классы - модели данных
 */
public abstract class Base {
    private final String id;

    protected Base(final String id) {
	this.id = id;
    }

    public String getId() {
	return id;
    }
}