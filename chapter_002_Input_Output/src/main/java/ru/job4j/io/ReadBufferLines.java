package ru.job4j.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class ReadBufferLines {
    public static void main(String[] args) {
	/**
	 * autoflush флашит при использовании PRINTLN
	 */
	Path pathSource = Paths.get("C:\\test\\source.txt");
	try (BufferedReader reader = new BufferedReader(new FileReader(pathSource.toString()));
	     PrintWriter writer = new PrintWriter(new FileWriter(pathSource.toString()), true)) {
	    List<String> sourceList = Arrays.asList("МетодLines ЖуткоКОВАРЕН".split(" "));
	    sourceList.forEach(line -> writer.print(line + System.lineSeparator())); // запись в файл
	   // writer.close();

	    System.out.println(reader.lines().findFirst().get()); //МетодLines
	    System.out.println(reader.lines().findFirst().get()); // ?? ЖуткоКоварен
	} catch (Exception e) {
	    System.out.println("reader / writer init");
	    e.printStackTrace();
	}
    }
}