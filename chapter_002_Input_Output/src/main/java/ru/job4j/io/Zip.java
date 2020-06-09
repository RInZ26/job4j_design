package ru.job4j.io;

import java.io.*;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Класс - Архивируем неархивируемое!
 */
public class Zip {

    /**
     * Метод архивирования списка файлов (путей). Сначала открываем поток для зипа, потом для каждого файла создаёт сущность Entry, которая является
     * чем-то вроде ячейки в таблице у зипа (УСЛОВНО) в ней указывается имя файла в который будем записывать. ИМЯ ФАЙЛА
     * НЕ ПУТЬ, НЕ ЧТО-ТО ЕЩЁ, А ПРОСТО ИМЯ. Далее начинаем записывать.
     * Странно, что не делаем closeEntry, но видимо при создании новой, автоматически она становится главной (так и есть).
     *
     * @param sources источники, которые мы в основном будем получать из метода Search.search()
     * @param target  - куда заносим
     */
    public void packFiles(List<Path> sources, Path target) {
	try (ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(target.toString())))) {
	    for (Path fileFromSources : sources) {
		ZipEntry nextZipEntry = new ZipEntry(fileFromSources.getFileName().toString());
		zipOutputStream.putNextEntry(nextZipEntry);
		try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(fileFromSources.toString()))) {
		    zipOutputStream.write(in.readAllBytes());
		}
	    }
	} catch (Exception e) {
	    System.out.println("ZipOutput");
	    e.printStackTrace();
	}
    }

    /**
     * То же самое но для одного файла
     */
    public void packSingleFile(Path source, Path target) {
	try (ZipOutputStream zip = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(target.toString())))) {
	    ZipEntry zipEntry = new ZipEntry(source.getFileName().toString());
	    zip.putNextEntry(zipEntry);
	    try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(source.toString()))) {
		zip.write(in.readAllBytes());
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
