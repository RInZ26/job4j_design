package ru.job4j.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Класс для баловства с директориями
 */
public class Dir {
    public static void main(String[] args) {
	File file = new File("c:\\projects");
	if (!file.exists()) {
	    throw new IllegalArgumentException(String.format("Not exist %s", file.getAbsoluteFile()));
	}
	if (!file.isDirectory()) {
	    throw new IllegalArgumentException(String.format("Not directory %s", file.getAbsoluteFile()));
	}
	for (File subfile : file.listFiles()) {
	    System.out.println(subfile.getName() + "  " + getSize(subfile));
	}
    }

    /**
     * В цикле ищем файлы внутри папок на всю глубину через рекурсию... Работает довольно долго, идея взята с хабра
     * @param nextFile следующий проверяемый файл
     */
    private static long getSize(File nextFile) {
	long size = 0;
	if (nextFile.isDirectory()) {
	    File[] innerFilesList = nextFile.listFiles();
	    if (innerFilesList != null) {
		for (File subFile : innerFilesList) {
		    size += getSize(subFile);
		}
	    }
	    return size;
	} else {
	    return size + nextFile.length();
	}
    }
}
