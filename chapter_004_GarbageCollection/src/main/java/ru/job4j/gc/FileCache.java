package ru.job4j.gc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.ref.SoftReference;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class FileCache implements Cache<Path, String> {
    private final static Logger LOG = LoggerFactory.getLogger(
            FileCache.class.getName());
    private Map<String, SoftReference<String>> cache = new HashMap<>();

    /**
        Пытаемся взять объект из мапы, если его нету - загружаем ручками.

     */
    @Override
    public String get(Path key) {
        if (!cache.containsKey(key)) {
            return uploadFile(key);
        } else {
            String data = cache.get(key)
                               .get();
            return !Objects.isNull(data) ? data : uploadFile(key);
        }
    }

    /**
     * Загружает из файла данные и оборачивает их в SoftReference, по сути
     * этот метод и есть ядро кэша
     *
     * @param path
     *
     * @return
     */
    private String uploadFile(Path path) {
        LOG.debug("UPLOAD {} IS RUNNING", path.getFileName()
                                              .toString()
                                              .toUpperCase());
        String text = null;
        try (BufferedReader in = new BufferedReader(
                new FileReader(path.toString()))) {
            cache.put(path.getFileName()
                          .toString(), new SoftReference<String>(
                    text = in.lines()
                             .collect(Collectors.joining(""))));
        } catch (Exception e) {
            LOG.error("upload fell down", e);
        }
        return text;
    }
}
