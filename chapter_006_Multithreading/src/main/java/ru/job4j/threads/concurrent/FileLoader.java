package ru.job4j.threads.concurrent;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.regex.Pattern;

/**
 * Класс для загрузки извне с ограниченимем на скорость
 */
public class FileLoader {
    /**
     * дефолтная задержка в 1 секунду = 1000 ms
     */
    private static final int DEFAULT_DELAY = 1000;
    /**
     * Сообщение об ошибке при неверном запуске со main args[]
     *///"https://raw.githubusercontent.com/peterarsentev/course_test/master/pom.xml"
    private static final String ERROR_MESSAGE = "arguments should be "
            + "represent like: a b, where a = url, b = speed(number > 0)";

    /**
     * isValidUrl()
     */
    private static final Pattern URL_PATTERN = Pattern.compile(
            "(http(s)?:/)?(/.*)");

    /**
     * Вся логика вынесена в один static main исходя из того, что
     * масштабирования здесь не планируется
     *
     * BufferedReader читает рандомное количество байт, поэтому нам нужно
     * дождаться, когда он прочтёт необходимое количество, для этого идёт
     * цикл с двумя счетками readed - сколько было уже прочитано и counter -
     * сколько прочитано в последний раз.
     * Скорость мерится примитивно через два таймстампа, потому что в
     * принципе дескретность задержки в секундах - довольно большая, чтобы
     * пренебречь обращениями к System.currentTimeMillis().
     *
     * @param args аргументы cmd
     */
    public static void main(String[] args) {
        checkArgs(args);
        String url = args[0];
        int speed = Integer.parseInt(args[1]);
        byte[] kb = new byte[1024 * speed];
        try (BufferedInputStream in = new BufferedInputStream(
                new URL(url).openStream());
             FileOutputStream out = new FileOutputStream(getFileName(url))) {
            int counter = 0, readed, stage = 1;
            while (counter != -1) {
                long start = System.currentTimeMillis();
                counter = 0;
                readed = 0;
                while (readed != kb.length) {
                    if ((counter = in.read(kb, readed, kb.length - readed))
                            == -1) {
                        break;
                    }
                    readed += counter;
                }
                out.write(kb, 0, readed);
                long finish = System.currentTimeMillis() - start;
                long delay = DEFAULT_DELAY - finish;
                if (delay > 0) {
                    Thread.sleep(delay);
                }
                System.out.printf(
                        "Скачано %d килобайт, скорость %s b/s, " + "время "
                                + "загрузки - %s " + "ms, "
                                + "задержка - %s ms%s", readed / 1024,
                        kb.length, finish, delay, System.lineSeparator());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Валидатор аргументов main args
     */
    private static void checkArgs(String[] args) {
        if (args.length != 2 || !isValidUrl(args[0]) || !isValidSpeed(
                args[1])) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }
    }

    /**
     * Проверка (простенькая) валидности url
     */
    private static boolean isValidUrl(String url) {
        return URL_PATTERN.matcher(url)
                          .matches();
    }

    /**
     * Проверка, что второй аргумент - положительное число, да тут получился
     * даблпарсинг, но это не критично
     */
    private static boolean isValidSpeed(String speed) {
        int result;
        try {
            result = Integer.parseInt(speed);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return result > 0;
    }

    /**
     * Возвращает название файла из url, как последний блок с /, если вдруг
     * не засплитится - будет дефолтное название
     */
    private static String getFileName(String url) {
        var splitted = url.split("/");
        return splitted.length > 1 ? splitted[splitted.length - 1] : "file";
    }
}
