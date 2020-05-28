package ru.job4j.io;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
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
     * Путь к файлу
     */
    private String filePath;
    /**
     * Мапа для хранения настроек и их параметров Ключ - имя настройки, значение - значаение настройки
     */
    private Map<String, String> mapOfProperties = new HashMap<>();

    //TODO почему строка должна быть final здесь?
    public Config(String filePath) {
	this.filePath = filePath;
    }

    /**
     * Заполнение нашей мапы информацией из файла по пути filePath
     * Игнорируем пустые строки и комментарии  в виде #
     * Через стрим фильтруем его по паттерну.
     *      * Учитываются случаи, когда ничего=значение, ключ=ничего
     *      * А так же пробелы идиотские в любом месте
     */
    public void load() {
	try (BufferedReader in = new BufferedReader(new FileReader(filePath))) {
	    Pattern optionPattern = Pattern.compile("\\s*\\w+\\s*=\\s*\\w+\\s*");
	    mapOfProperties = in.lines().filter(o -> optionPattern.matcher(o).matches()).map(o -> {
		String[] splittedO = o.split("=");
		return new Holder(splittedO[0], splittedO.length > 1 ? splittedO[1] : null);
	    }).collect(Collectors.toMap(o -> o.key.trim(), o -> o.value.trim()));
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