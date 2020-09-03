package ru.job4j.threads.concurrent;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.io.*;
import java.util.Objects;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;

/**
 * Делаем допущение(на основании кода до рефакторинга), что работаем с
 * текстовыми файлами - где можем принимать всё за чары => можно
 * использовать Reader и Printer, в противном случае
 * лучше было бы использовать обычные байтовые потоки.
 *
 * Что касается многопоточности:
 * Всё-таки сеттеры в контексте многопточности и безопасности не очень
 * хороши, незачем здесь давать возможность менять файлы. Парсеры можно
 * всегда пересоздать
 */
@ThreadSafe
public class ParseFile {
    @GuardedBy("this")
    private final File file;

    public ParseFile(File file) {
        Objects.requireNonNull(file);
        this.file = file;
    }

    /**
     * Благодаря final переменной можно не беспокоиться о геттере и
     * псеведосеттере updateFile - их не нужно синхронизировать
     *
     * @return
     */
    public File getFile() {
        return file;
    }

    /**
     * Замещение сеттера с учетом того, что this.file - final => при попытке
     * изменения файла, нужно просто создавать новый парсер
     */
    public ParseFile updateFile(File file) {
        return new ParseFile(file);
    }

    /**
     * Раз подразумевается кастинг в чары, то есть смысл сделать Reader сразу
     */
    public String getContent() {
        return getContentByRule(c -> true);
    }

    /**
     * Без юникод символов
     */
    public String getContentWithoutUnicode() {
        return getContentByRule(c -> c < 0x80);
    }

    /**
     * Обобщенный метод чтения с фильтром
     */
    private synchronized String getContentByRule(IntPredicate rule) {
        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            return in.lines()
                     .flatMapToInt(String::chars)
                     .filter(rule::test)
                     .mapToObj(String::valueOf)
                     .collect(Collectors.joining());
        } catch (IOException io) {
            io.printStackTrace();
        }
        return "";
    }

    /**
     * Простое сохранение
     *
     * @param content
     */
    public synchronized void saveContent(String content) {
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.print(content);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}