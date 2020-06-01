package ru.job4j.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Класс для работы с конфигом
 *
 * @author RinZ26
 */
public class Config {
    /**
     * Регулярка для проверки на соответствие записи в виде что-то=что-то, иначе говоря - записи конфигурации
     */
    private static final Pattern PATTERN_FOR_CONFIG_RECORD = Pattern.compile("\\s*\\w+\\s*=\\s*\\w+\\s*");
    /**
     * Путь к файлу
     * "final для Map, чтобы не затереть мапу случаем. final для path указывает,
     * что Config работает только с один файлом настроек. Это похоже на класс Properties,
     * который также работает только с одним файлом настроек"(с)
     */
    private final String path;
    /**
     * Мапа для хранения настроек и их параметров Ключ - имя настройки, значение - значаение настройки
     */
    private final Map<String, String> mapOfProperties = new HashMap<>();

    public Config(String filePath) {
	this.path = filePath;
    }

    /**
     * Заполнение нашей мапы информацией из файла по пути filePath
     * Игнорируем пустые строки и комментарии  в виде #
     * Через стрим фильтруем его по паттерну.
     *      * Учитываются случаи, когда ничего=значение, ключ=ничего
     *      * А так же пробелы идиотские в любом месте
     */
    public void load() {
	try (BufferedReader in = new BufferedReader(new FileReader(path))) {
	    mapOfProperties.putAll(in.lines().filter(o -> PATTERN_FOR_CONFIG_RECORD.matcher(o).matches()).map(o -> {
		String[] splittedO = o.split("=");
		return new Holder(splittedO[0], splittedO.length > 1 ? splittedO[1] : null);
	    }).collect(Collectors.toMap(o -> o.key.trim(), o -> o.value.trim())));
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /**
     * Вывод мапы на экран
     */
    public void showMap() {
	for (Map.Entry<String, String> pair : mapOfProperties.entrySet()) {
	    System.out.println(pair.getKey() + " " + pair.getValue());
	}
    }

    /**
     * Проверка содержится ли ключ
     *
     */
    public boolean containsKey(String key) {
	return mapOfProperties.containsKey(key);
    }

    /**
     * Для получения значения по ключу из mapOfProperties
     */
    public String value(String key) {
	return mapOfProperties.get(key);
    }

    /**
     * Нужен для стрима в load
     */
    private static class Holder {
	String key;
	String value;

	Holder(String key, String value) {
	    this.key = key;
	    this.value = value;
	}
    }
}