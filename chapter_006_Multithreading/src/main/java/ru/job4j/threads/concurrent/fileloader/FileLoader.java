package ru.job4j.threads.concurrent.fileloader;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.regex.Pattern;

/**
 * Класс для загрузки извне с ограниченимем на скорость
 */
public class FileLoader implements Runnable {
    /**
     * isValidUrl()
     */
    private static final Pattern URL_PATTERN = Pattern.compile(
            "(http(s)?:/)" + "?" + "(/.*)");

    /**
     * дефолтная задержка в 1 секунду = 1000 ms
     */
    private int defaultDelay = 1000;
    /**
     * Сообщение об ошибке при неверном запуске со main args[]
     */
    private String errorMessage =
            "arguments should be " + "represented like: " + "a" + " b, where a "
                    + "= url, b = speed(number > 0)";

    /**
     * Аргументы, прилетевшие из cmd
     */
    private String[] args;

    public FileLoader(String[] args) {
        this.args = args;
    }

    public static void main(String[] args) throws InterruptedException {
        var thread = new Thread(new FileLoader(args));
        thread.start();
        thread.join();
    }

    /**
     * BufferedReader читает рандомное количество байт, поэтому нам нужно
     * дождаться, когда он прочтёт необходимое количество, для этого идёт цикл с
     * двумя счетками readed - сколько было уже прочитано и counter - сколько
     * прочитано в последний раз. Скорость мерится примитивно через два
     * таймстампа, потому что в принципе дескретность задержки в секундах -
     * довольно большая, чтобы пренебречь обращениями к System
     * .currentTimeMillis().
     */
    @Override public void run() {
        checkArgs(args);
        String url = args[0];
        int speed = Integer.parseInt(args[1]);
        byte[] kb = new byte[1024 * speed];
        try (BufferedInputStream in =
                     new BufferedInputStream(new URL(url).openStream());
             FileOutputStream out = new FileOutputStream(getFileName(url))) {
            int counter = 0, readed;
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
                double difTime = getLoadTime(start, System.currentTimeMillis());
                double delay = defaultDelay - difTime;
                if (delay > 0) {
                    Thread.sleep((long) delay);
                }
                System.out.printf("Скачано %d бит, скорость %s b/s, " + "время "
                        + "загрузки - %s " + "ms, " + "задержка - " + "%s"
                        + " ms%s", readed, getSpeed(delay, speed), difTime,
                        delay, System.lineSeparator());
            }
        } catch (InterruptedException ie) {
            Thread.currentThread()
                    .interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Валидатор аргументов main args
     */
    private void checkArgs(String[] args) {
        if (args.length != 2 || !isValidUrl(args[0])
                || !isValidSpeed(args[1])) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    /**
     * Проверка (простенькая) валидности url
     */
    private boolean isValidUrl(String url) {
        return URL_PATTERN.matcher(url)
                .matches();
    }

    /**
     * Проверка, что второй аргумент - положительное число, да тут получился
     * даблпарсинг, но это не критично
     */
    private boolean isValidSpeed(String speed) {
        int result;
        try {
            result = Integer.parseInt(speed);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return result > 0;
    }

    /**
     * Возвращает название файла из url, как последний блок с /, если вдруг не
     * засплитится - будет дефолтное название
     */
    private String getFileName(String url) {
        var splitted = url.split("/");
        return splitted.length > 1 ? splitted[splitted.length - 1] : "file";
    }

    /**
     * Подсчитывает скорость с которой скачался последний "пакет" данных,
     * который эквивалентен нашей заявленной скорости
     *
     * @param time ms
     * @param size kb / s
     *
     * @return b/ms
     */
    private double getSpeed(double time, int size) {
        return size * 1024 * time / 1000.d;
    }

    /**
     * Подсчёт разницы между стартом и окончанием загрузки
     *
     * @param start  начало
     * @param finish конец
     *
     * @return разница
     */
    private double getLoadTime(long start, long finish) {
        long dif = finish - start;
        return dif == 0 ? 1 : dif;
    }

    public int getDefaultDelay() {
        return defaultDelay;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setDefaultDelay(int defaultDelay) {
        this.defaultDelay = defaultDelay;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
