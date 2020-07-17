package ru.job4j.gc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.ref.SoftReference;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class FileCache implements Cache<Path, String> {
    private final static Logger LOG = LoggerFactory.getLogger(
            FileCache.class.getName());
    private Map<String, SoftReference<String>> cache = new HashMap<>();

    /**
     * Чтобы избежать случая, когда перед вызовом у SoftReference метода get()
     * его не вызвал gc, делаем явно Strong ссылку, при этом нужно понять, не
     * стерлось ли уже значение до вызова, если стёрлось - мы пробуем
     * загрузить его заново и если загрузка прошла успешно (uploadFile
     * -boolean), то идёт повторная попытка взять файл.
     *
     * В теории может случться так, что мы попадём в бесконечную рекурсию,
     * когда uploadFile будет грузить, GC - стирать, а get пытаться прочитать и
     * запускать upload. Но по логике в таком случае просто должен случиться
     * либо OutOfMemory рано или поздно, либо за счёт освобождения других
     * ресурсов get успеет сделать Strong Reference и спасти объект
     *
     * @param key ключ
     */
    @Override
    public String get(Path key) {
        var softRef = cache.get(key.getFileName()
                                   .toString());
        String strongRef = null;
        if (!Objects.isNull(softRef)) {
            strongRef = softRef.get();
        }
        if (!Objects.isNull(strongRef)) {
            return strongRef;
        } else {
            return uploadFile(key) ? get(key) : null;
        }
    }

    /**
     * Загружает из файла данные и оборачивает их в SoftReference, по сути
     * этот метод и есть ядро кэша
     * @param path
     * @return
     */
    private boolean uploadFile(Path path) {
        LOG.debug("UPLOAD {} IS RUNNING", path.getFileName()
                                              .toString()
                                              .toUpperCase());
        boolean result = true;
        try (BufferedReader in = new BufferedReader(
                new FileReader(path.toString()))) {
            cache.put(path.getFileName()
                          .toString(), new SoftReference<String>(in.lines()
                                                                   .collect(
                                                                           Collectors.joining(
                                                                                   ""))));
        } catch (Exception e) {
            LOG.error("upload fell down", e);
            result = false;
        }
        return result;
    }

    public static void main(String[] args) {
        String alpha = "C:\\projects\\job4j_design"
                + "\\chapter_004_GarbageCollection\\alpha.txt";
        String beta = "C:\\projects\\job4j_design"
                + "\\chapter_004_GarbageCollection\\beta.txt";
        String gamma = "C:\\projects\\job4j_design"
                + "\\chapter_004_GarbageCollection\\gamma.txt";
        String gamma2 = "C:\\projects\\job4j_design"
                + "\\chapter_004_GarbageCollection\\gamma2.txt";
        String gamma3 = "C:\\projects\\job4j_design"
                + "\\chapter_004_GarbageCollection\\gamma3.txt";
        Cache tempCache = new FileCache();
        for (int c = 0; c < Long.MAX_VALUE; c++) {
            tempCache.get(Paths.get(alpha));
            tempCache.get(Paths.get(beta));
            tempCache.get(Paths.get(gamma));
            tempCache.get(Paths.get(gamma2));
            tempCache.get(Paths.get(gamma3));
        }
    }
}
