package ru.job4j.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Класс для фильтра данных из файлов
 *
 * @author RinZ26
 */
public class LogFilter {
    /**
     * РЕГУЛЯРКИ ОБЯЗАТЕЛЬНО ДОЛЖНЫ БЫТЬ ЛИБО STATIC FINAL, ЛИБО НИКАК вообще
     * Связано с тем, что они работают с синхронизацией + довольно сложным
     * устройством внутри себя
     */
    private static final Pattern PATTERN_FOR_ERRORS =
	    Pattern.compile("\\?*\\s404\\s\\d*");

    /**
     * Может показаться, что фильтр занимается лишним, но нет. Чтобы не
     * сохранять в память и пользоваться идеей Buffered - а именно, не нагружать
     * память - уже в самом фильтре идёт получение данных и сразу же фильтрация
     */
    public static List<String> filter(String file) {
	try (BufferedReader in = new BufferedReader(new FileReader(file))) {
	    return in.lines().filter(o -> PATTERN_FOR_ERRORS.matcher(o).find())
		     .collect(Collectors.toList());
	} catch (Exception e) {
	    e.printStackTrace();
	    return Collections.emptyList();
	}
    }

    /**
     * Для записи в файл более умным способом!
     *
     * @param listOfData
     * 	- что записываем
     * @param filePath
     * 	куда записываем
     */
    public static void saveToFile(List<String> listOfData, String filePath) {
	try (PrintWriter out = new PrintWriter(new File(filePath))) {
	    listOfData.forEach(o -> {
		out.print(o);
		out.print(System.lineSeparator());
	    });
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public static void main(String[] args) {
	List<String> log = filter("log.txt");
	log.forEach(System.out::println);
	saveToFile(log, "404.txt");
    }
}