package ru.job4j.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс для фильтра данных из файлов
 *
 * @author RinZ26
 */
public class LogFilter {
    /**
     * Элегантно через фильтер проходим по стриму, можно было сделать ещё лучше и добавить в параметры предикат и проверять по фильтру уже его
     * А так алгоритм: Получаем заспличенный по /r лист строк из getListOfDataFromFile
     * Через стрим фльитруем его по критерию, предварительно расспличивая каждую строку по пробелам
     */
    public static List<String> filter(String file) {
	return getListOfDataFromFile(file).stream().filter(o -> {
	    String[] splittedO = o.split(" ");
	    return Integer.parseInt(splittedO[splittedO.length - 2]) == 404;
	}).collect(Collectors.toList());
    }

    /**
     * Чтобы filter не занимался чем не надо, чтение будест происходить отдельно
     */
    private static List<String> getListOfDataFromFile(String file) {
	try (BufferedReader in = new BufferedReader(new FileReader(file))) {
	    int inNext; // для проверки окончания файла
	    StringBuilder dataStore = new StringBuilder();
	    while ((inNext = in.read()) != -1) {
		dataStore.append((char) inNext);
	    }
	    return Arrays.asList(dataStore.toString().split(System.lineSeparator()));
	} catch (Exception e) {
	    System.out.println(e.getStackTrace());
	}
	return Collections.emptyList();
    }

    public static void main(String[] args) {
	List<String> log = filter("log.txt");
	log.forEach(System.out::println);
    }
}