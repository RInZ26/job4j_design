package ru.job4j.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Search {
    public static void main(String[] args) throws IOException {
	Path start = Paths.get("C:\\projects");
	search(start, ".txt").forEach(System.out::println);
    }

    /**
     * p.toFile.getName() А НЕ ПРОСТО p.getFileName, потому что в первом случае возвращается стринг, а во втором Path
     * А endsWith у Path работает АБСОЛЮТНО не так, как у стринга. у Path он сравнивает именно блок. например через него можно найти конкретный файл name.txt,
     * но нельзя найти что-то что на что-то окончивается, например /Anothername.txt он уже не найдёт
     * Но, можно было конечно сделать p.getFileName().toString() и т.п.
     *
     * @param root путь из которого начинаем поиск
     * @param ext  расшрение файла. которо проверяется в предикате
     */
    public static List<Path> search(Path root, String ext) {
	SearchFiles searcher = new SearchFiles(p -> p.toFile().getName().endsWith(ext));
	try {
	    Files.walkFileTree(root, searcher);
	} catch (Exception e) {
	    System.out.println("gg");
	    e.printStackTrace();
	}
	return searcher.getListOfPathsWhichPassedRule();
    }
}