package ru.job4j.io.exam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 Класс - поисковик файла в каталоге и подкаталогах
 */
public class Searcher {
    /**
     Обеспечение логгирования
     */
    private static final Logger LOG = LoggerFactory.getLogger(
            Searcher.class.getName());
    /**
     Откуда начинается поиск
     */
    private Path root;
    /**
     Предикат для поиска
     */
    private Predicate<Path> rule;

    Searcher(Path root, Predicate<Path> rule) {
        this.root = root;
        this.rule = rule;
    }

    public static void main(String[] args) {
        if (isValidCommand(args)) {
            Searcher searcher = new Searcher(Paths.get(args[1]), new Dispatcher(
                    args).predicateFactory.get(args[4]).get());
            exportData(Paths.get(args[6]), searcher.search());
            System.out.println("Successful");
        }
    }

    /**
     Загрузка данных

     @param target
     куда грузим
     @param list
     Что грузим
     */
    public static boolean exportData(Path target, List list) {
        try (PrintWriter writer = new PrintWriter(
                new FileWriter(target.toFile()), true)) {
            list.forEach(writer::println);
        } catch (Exception e) {
            LOG.error("exportData", e);
        }
        return true;
    }

    /**
     Преобразует маску в паттерн, чтобы объединить варианты поиска по маске и по
     регулярке
     */
    private static Pattern maskToPattern(String mask) {
        return Pattern.compile(
                mask.replaceAll("\\*", ".*").replaceAll("\\?", "."));
    }

    /**
     Общая валидация. Сначале isValidKeys,т.к. там общие проверки, потом уже
     isValidValues
     */
    public static boolean isValidCommand(String[] args) {
        return isValidKeys(args) && isValidValues(args);
    }

    /**
     Валидация ключей из args.

     keys - Возможный список ключей для опции -n Не
     вижу смысла здесь делать HashMap или какой-то массив
     Хотя можно было сделать и регулярку, но их и так уже много
     (У нас массив, думаю, нет смысла заниматься склеиванием миллиона
     подстрок ради этого)
     */
    private static boolean isValidKeys(String[] args) {
        String keys = "-m-f-r";
        if (args.length != 7 || !args[0].equals("-d") || !args[2].equals("-n")
                || !keys.contains(args[4]) || !args[5].equals("-o")) {
            throw new IllegalArgumentException(getHint());
        }
        return true;
    }

    /**
     Валидация значений из args.
     Имеет смысл делать это отдельно, потому что первоначально нам важно
     проверить ключи, а потом уже это.
     Так, нужно проверить существование папки root -d C:/directory
     и существование parentDirectory(directory2) у target -o directory2/log.txt
     Но, может быть сл
     Подразумевается, что сначала вызовется isValidKeys, который сделает
     общие проверки - на размер args, поэтому здесь это не проверяется
     повторно и методы private.
     Возможный синтаксис команды: -d c:/directory1 -n *.txt -m -o directory2/log
     .txt

     Проверка на маску/регулярку не предусмотрена
     */
    private static boolean isValidValues(String[] args) {
        try {
            Path source = Paths.get(args[1]);
            Path target = Paths.get(args[6]);
            if (source.toFile().exists() && isValidTarget(target)) {
                return true;
            } else {
                throw new IllegalArgumentException(
                        "Used directories are illegal");
            }
        } catch (InvalidPathException ipe) {
            throw new IllegalArgumentException(getHint());
        }
    }

    /**
     Проверка капризного target. Проблема в том, что он может легально не
     существовать
     (ведь его создаёт в loadData writer сам спокойно), но в то же время,
     может уже нелегально не существовать его родитель, то есть папка перед
     файлом
     */
    private static boolean isValidTarget(Path target) {
        if (target.toFile().exists()) {
            return true;
        }
        Path parent = target.getParent();
        if (parent == null) {
            return true;
        } else {
            return parent.toFile().exists();
        }
    }

    /**
     Сообщение с подсказкой, когда корявый вызов
     */
    public static String getHint() {
        return ("Ключи \n" + "-d - директория в которая начинать поиск.\n"
                + "-n - имя файл, маска, либо регулярное выражение.\n"
                + "-m - искать по макс, либо -f - полное совпадение имени. "
                + "-r регулярное выражение.\n"
                + "-o - результат записать в файл\n"
                + " Example: -d c:/ -n *.txt " + "-m " + "-o log.txt ");
    }

    /**
     Поиск файлов по предикату
     */
    public List<Path> search() {
        SearcherFileVisitor fileVisitor = new SearcherFileVisitor(rule);
        try {
            Files.walkFileTree(root, fileVisitor);
        } catch (Exception e) {
            LOG.error("search", e);
        }
        return fileVisitor.getPaths();
    }

    /**
     Так как у нас есть варианты у ключа условия -n от которого зависит
     Predicate, то лучше(наверное) организовать его через диспатчер
     */
    private static class Dispatcher {
        private Map<String, Supplier<Predicate<Path>>> predicateFactory = new HashMap<>();
        /**
         Возможные ключи
         */
        private String mKey = "-m";
        private String fKey = "-f";
        private String rKey = "-r";
        /**
         Аргументы
         */
        private String[] args;

        Dispatcher(String[] args) {
            this.args = args;
            initMap();
        }

        /**
         Заполняем мапу
         */
        public void initMap() {
            try {
                predicateFactory.put(mKey, mKeySupplier());
                predicateFactory.put(fKey, fKeySupplier());
                predicateFactory.put(rKey, rKeySupplier());
            } catch (Exception e) {
                LOG.error("Заполнение searchFactory", e);
            }
        }

        private Supplier<Predicate<Path>> mKeySupplier() {
            return () -> path -> maskToPattern(args[3]).matcher(
                    path.getFileName().toString()).matches();
        }

        private Supplier<Predicate<Path>> fKeySupplier() {
            return () -> path -> path.getFileName().toString().equals(args[1]);
        }

        private Supplier<Predicate<Path>> rKeySupplier() {
            return () -> path -> Pattern.compile(args[3])
                                        .matcher(path.getFileName().toString())
                                        .matches();
        }
    }
}