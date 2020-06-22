package ru.job4j.io.exam;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.regex.Pattern;

class Shell {
    /**
     Регулярка корректной добавляемой строки
     */
    private static final Pattern PATTERN = Pattern.compile("(\\w+/?)+");

    /**
     Фейковый root для случаев, когда не можем подняться в dirUp
     */
    private static String root = "";

    /**
     Композируем диспатчер
     */
    private Dispatcher dispatcher = new Dispatcher();

    /**
     Переменная для отслеживания текущего состояния пути
     */
    private Path path = Paths.get(root);

    /**
     Перемещение по каталогам

     @param command
     - команда

     @return возвращает себя для chain вызова
     */
    Shell cd(final String command) {
        dispatcher.options.getOrDefault(command, ifNotOption(command))
                          .accept(command);
        return this;
    }

    /**
     Обёртка Path'а на вывод с другим слешем
     */
    public String path() {
        return new StringBuilder(path.toString()).insert(0, "\\")
                                                 .toString()
                                                 .replaceAll("\\\\", "/");
    }

    /**
     Обёртка для дефольного консьюмера в качестве промежуточного метода с
     различным выводом.
     */
    private Consumer<String> ifNotOption(String newPath) {
        return PATTERN.matcher(newPath).matches() ? dispatcher.dirDown()
                : ((String a) -> System.out.printf(
                        "Wrong command %s. Current" + " Path: %s \n", a,
                        Shell.this.path));
    }

    private class Dispatcher {
        static final String COMMAND_DOWN = "..";
        private Map<String, Consumer<String>> options = new HashMap<>();

        Dispatcher() {
            init();
        }

        /**
         Заполенине мапы диспатчера
         */
        void init() {
            options.put(COMMAND_DOWN, dirUp());
        }

        /**
         Поднимаемся на католог выше, если есть, если такого нет - значит мы
         достигли дна
         и возвращаем по сути пустой путь
         */
        Consumer<String> dirUp() {
            return (String a) -> Shell.this.path = Optional.of(path.getParent())
                                                           .orElse(Paths.get(
                                                                   Shell.root));
        }

        /**
         Заходим в дочерний каталог - по сути дефолтный консьюмер
         */
        Consumer<String> dirDown() {
            return (String newPath) -> Shell.this.path = Paths.get(
                    Shell.this.path.toString(), newPath);
        }

    }
}

public class Directory {
    public static void main(String[] args) {

        final Shell shell = new Shell();
        assert shell.path().equals("/");

        shell.cd("/");
        assert shell.path().equals("/");

        shell.cd("usr/..");
        assert shell.path().equals("/");

        shell.cd("usr").cd("local");
        shell.cd("../local").cd("./");
        assert shell.path().equals("/usr/local");

        shell.cd("..");
        assert shell.path().equals("/usr");

        shell.cd("//lib///");
        assert shell.path().equals("/lib");
    }
}