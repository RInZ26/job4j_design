package ru.job4j.io;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Класс для фильтра данных из файлов
 *
 * @author RinZ26
 */
public class LogFilter {
    /**
     * Может показаться, что фильтер занимается лишним, но нет.
     * Так как у нас паттерн, мы можем его использовать и для просто копирования из файла
     * указав \\?* в pattern
     */
    public static List<String> filter(String file, String pattern) {
	try (BufferedReader in = new BufferedReader(new FileReader(file))) {
	    return in.lines().filter(o -> Pattern.compile(pattern).matcher(o).find()).collect(Collectors.toList());
	} catch (Exception e) {
	    e.printStackTrace();
	    System.out.println("gg");
	    return Collections.emptyList();
	}
    }

    /**
     * Для записи в файл более умным способом!
     *
     * @param listOfData - что записываем
     * @param filePath   куда записываем
     */
    public static void saveToFile(List<String> listOfData, String filePath) {
	try (PrintWriter out = new PrintWriter(new BufferedOutputStream(new FileOutputStream(filePath)))) {
	    listOfData.forEach(o -> {
		out.write(o);
		out.write(System.lineSeparator());
	    });
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public static void main(String[] args) {
	String pattern = "\\?*\\s404\\s\\d*";
	List<String> log = filter("log.txt", pattern);
	log.forEach(System.out::println);
	saveToFile(log, "404.txt");
    }
}