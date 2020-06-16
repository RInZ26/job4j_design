package ru.job4j.io;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Predicate;

/**
 Класс для работы с параметрами у main и вызовом zip
 */
public class ArgZip {
    /**
     Те самые параметры main
     */
    private final String[] args;
    /**
     //-d - directory - которую мы хотим архивировать
     */
    private Path directory;
    /**
     // -e - exclude - исключить файлы *.xml
     */
    private String exclude;
    /**
     // -o - output - во что мы архивируем.
     */
    private Path output;

    public ArgZip(String[] args) {
        this.args = args;
    }

    /**
     Геттер Предиката исключения файла по расширению

     @param ext
     что исключаем
     */
    public static Predicate<Path> getPredicate(String ext) {
        return path -> !path.toString().endsWith(ext);
    }

    public static void main(String[] args) {
        ArgZip argZip = new ArgZip(args);
        if (argZip.initIfValid()) {
            System.out.println("All are going nice");
            new Zip().packFiles(Search.seekByPredicate(argZip.directory,
                                                       getPredicate(
                                                               argZip.exclude)),
                                argZip.output);
        }
    }

    /**
     Всё ли ок с args Без реглуярок ОСОЗНАННО => примитивные сплиты. Второй if на
     IllegalArgumentException - одновременно проверяет и ключи и наличие
     параметра Выглядит убого конечно. В этом же методе идёт инициализация полей
     класса, потому что строки уже заспличены - нет смысла делать это дважды.
     */
    public boolean initIfValid() {
        if (args.length != 3) {
            throw new IllegalArgumentException(String.format(
                    "%s ARGUMENTS. Need only 3. 1) SOURCE DIRECTORY 2)EXTENSION OF EXCLUDED FILES 3) TARGET .zip",
                    args.length < 3
                            ? "NOT ENOUGH"
                            : "MORE THAN NEED"));
        }
        boolean result = false;
        String[] splDir = args[0].split("=");
        String[] splExc = args[1].split("=");
        String[] splOut = args[2].split("=");
        if (!splDir[0].equals("-d") || !splExc[0].equals("-e")
                || !splOut[0].equals("-o") || splDir.length != 2
                || splExc.length != 2 || splOut.length != 2) {
            throw new IllegalArgumentException(
                    "One of parameters is empty or using wrong key. U have to init  -d=\"sourseDirectory\" -e=\"excludeExtension\" -o=\"outputZipFile\"");
        }
        if (isDir(splDir[1]) && isOutput(splOut[1])) {
            directory = Paths.get(splDir[1]);
            exclude = splExc[1];
            output = Paths.get(splOut[1]);

            result = true;
        }
        return result;
    }

    /**
     Проверка аргумента -d directory на то, что это вообще путь && существует &&
     является директорией

     @param path
     аргумент
     */
    public boolean isDir(String path) {
        boolean result = false;
        try {
            Path questionableDirectory = Paths.get(path);
            if (questionableDirectory.toFile().exists() && questionableDirectory
                    .toFile()
                    .isDirectory()) {
                result = true;
            }
        } catch (InvalidPathException invalidPE) {
            System.out.println("Invalid Path Exception");
        }
        return result;
    }

    /**
     Проверка outputa на то, что это архив

     @param output
     аргумент
     */
    public boolean isOutput(String output) {
        return (output.endsWith(".zip"));
    }
}