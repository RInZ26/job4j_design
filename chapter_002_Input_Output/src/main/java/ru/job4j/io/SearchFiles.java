package ru.job4j.io;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static java.nio.file.FileVisitResult.CONTINUE;

public class SearchFiles implements FileVisitor<Path> {
    /**
     * Предикат который проверяет пути, например на расширения
     */
    private Predicate<Path> rule;
    /**
     * Список тех, кто прошёл проверку
     */
    private List<Path> paths = new ArrayList<>();

    public SearchFiles(Predicate<Path> rule) {
	this.rule = rule;
    }

    public List<Path> getPaths() {
	return paths;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir,
	    BasicFileAttributes attrs) throws IOException {
	return CONTINUE;
    }

    /**
     * Что происходит при посещении файла
     */
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
	    throws IOException {
	if (rule.test(file)) {
	    paths.add(file);
	}
	return CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc)
	    throws IOException {
	System.out.println("Problem file " + file);
	return CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc)
	    throws IOException {
	return CONTINUE;
    }
}
