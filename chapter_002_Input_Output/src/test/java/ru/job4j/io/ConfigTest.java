package ru.job4j.io;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class ConfigTest {
    String path;
    Config config;

    @Before
    public void setUp() {
	LogFilter.saveToFile(Arrays.asList("name=Petr"),
			     "whenPairWithoutComment.txt");
	LogFilter.saveToFile(Collections.emptyList(), "whenFileIsEmpty.txt");
	LogFilter.saveToFile(
		Arrays.asList("  ", "", "#fakeadmin=ERROR", "name=Petr",
			      "password=admin"),
		"whenFileHasCommentsAndEmptyLines.txt");
	LogFilter.saveToFile(
		Arrays.asList("  terran=imba  ", "#44555", "name=Petr",
			      "password=admin"), "whenFileHas3difKeys.txt");
	LogFilter.saveToFile(Arrays.asList("=admin"),
			     "whenFirstFieldInPairIsEmpty.txt");
	LogFilter.saveToFile(Arrays.asList("password="),
			     "whenSecondFieldInPairIsEmpty.txt");
    }

    @Test
    public void whenPairWithoutComment() {
	path = "whenPairWithoutComment.txt";
	config = new Config(path);
	config.load();
	config.showMap();
	assertThat(
		config.value("name"),
		is("Petr")
	);
    }

    @Test
    public void whenFileIsEmpty() {
	path = "whenFileIsEmpty.txt";
	config = new Config(path);
	config.load();
	assertNull(
		config.value("name")
	);
    }

    @Test
    public void whenFileHasCommentsAndEmptyLines() {
	path = "whenFileHasCommentsAndEmptyLines.txt";
	config = new Config(path);
	config.load();
	assertNull(
		config.value("#fakeadmin")
	);
	assertNull(config.value(""));
	assertNull(config.value("  "));
    }

    @Test
    public void whenFileHas3difKeys() {
	path = "whenFileHas3difKeys.txt";
	config = new Config(path);
	config.load();
	config.showMap();
	assertThat(
		config.value("name"),
		is("Petr"));
	assertThat(
		config.value("terran"),
		is("imba"));
	assertThat(
		config.value("password"),
		is("admin"));
    }

    /**
     * Могут быть ужасы в случае записи =value Как бы он вдруг не стал
     * интерпритировать "" как ключ Или =value как ключ
     * <p>
     * Не будет благодаря проверки на паттерн
     */
    @Test
    public void whenFirstFieldInPairIsEmpty() {
	path = "whenFirstFieldInPairIsEmpty.txt";
	config = new Config(path);
	config.load();
	assertNull(
		config.value(""));
	assertNull(config.value("=admin"));
    }

    @Test
    public void whenSecondFieldInPairIsEmpty() {
	path = "whenSecondFieldInPairIsEmpty.txt";
	config = new Config(path);
	config.load();
	assertNull(
		config.value("password"));
	assertFalse(config.containsKey("password"));
    }
}