package ru.job4j.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Search {
    public static void main(String[] args) throws IOException {
	if (args.length < 3) {
	    throw new IllegalArgumentException("Root folder is null or Extension of File is null, or  option is empty u have to put 3 arguments");
	}
	search(Paths.get(args[0]), args[1], Boolean.valueOf(args[2])).forEach(System.out::println);
    }

    /**
     * p.toFile.getName() А НЕ ПРОСТО p.getFileName, потому что в первом случае возвращается стринг, а во втором Path
     * А endsWith у Path работает АБСОЛЮТНО не так, как у стринга. у Path он сравнивает именно блок. например через него можно найти конкретный файл name.txt,
     * но нельзя найти что-то что на что-то окончивается, например /Anothername.txt он уже не найдёт
     * Но, можно было конечно сделать p.getFileName().toString() и т.п.
     *
     * @param root путь из которого начинаем поиск
     * @param ext  расшрение файла. которо проверяется в предикате
     * @param isLookingForFilesExcludedThisExtensionOrLookingForOnlyThisOne - хотим ли мы найти ВСЁ кроме файлов с ext, либо наоборот только их
     */
    public static List<Path> search(Path root, String ext, boolean isLookingForFilesExcludedThisExtensionOrLookingForOnlyThisOne) {
	SearchFiles searcher = new SearchFiles(p -> p.getFileName().toString().endsWith(ext) ^ isLookingForFilesExcludedThisExtensionOrLookingForOnlyThisOne);
	try {
	    Files.walkFileTree(root, searcher);
	} catch (Exception e) {
	    System.out.println("gg");
	    e.printStackTrace();
	}
	return searcher.getListOfPathsWhichPassedRule();
    }
}