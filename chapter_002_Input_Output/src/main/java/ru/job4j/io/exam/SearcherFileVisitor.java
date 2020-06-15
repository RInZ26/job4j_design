package ru.job4j.io.exam;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static java.nio.file.FileVisitResult.CONTINUE;

/**
 Класс для обхода файлов внутри директории
 */
class SearcherFileVisitor implements FileVisitor<Path> {
    /**
     Список файлов, которые прошли проверку
     */
    private List<Path> paths = new ArrayList<>();

    /**
     Обернули регулярку в условие предиката, чтобы еще ловить обычное имя файла
     при этом, в таком случае в 1/3 случаев мы будем работать не с регуляркой
     */
    private Predicate<Path> rule;

    SearcherFileVisitor(Predicate<Path> rule) {
        this.rule = rule;
    }

    /**
     Геттер для списка путей
     */
    public List<Path> getPaths() {
        return paths;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir,
                                             BasicFileAttributes attrs)
            throws IOException {
        return CONTINUE;
    }

    /**
     Если файл удовлетворяет условию, добавляем его в список
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
        return CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc)
            throws IOException {
        return CONTINUE;
    }
}