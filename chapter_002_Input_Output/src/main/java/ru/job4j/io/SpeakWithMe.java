package ru.job4j.io;

import java.io.*;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


/**
 * Класс для одиноких людей
 */
public class SpeakWithMe {
    private static final String FINISH_WORD = "Закончить";
    private static final String STOP_WORD = "Стоп";
    private static final String CONTINUE_WORD = "Продолжить";
    Path pathSourceOfAnswers, pathLogOfConversation;
    List<String> systemAnswersList = Collections.emptyList();


    public SpeakWithMe(Path pathSourceOfAnswers, Path pathLogOfConversation) {
	this.pathSourceOfAnswers = pathSourceOfAnswers;
	this.pathLogOfConversation = pathLogOfConversation;
    }


    /**
     * Консольный чат с записью ответов в лог.
     * Данные берутся из внутренней коллекции, предварительно заполненной из файла(если такой есть).
     * Поддерживаются команды - простые стринги. Енум здесь не особо нужен.
     * Из минусов - учитывается регистр команд, плата за switch и неиспользование equalsIgnoreCase
     * Команды: Стоп - программа перестаёт отвечать и НЕ ДАЁТ ОТВЕТ на команду стоп (важно)
     * Продолжить - возобновляет общение - на саму команду ответа не даёт
     * Закончить - выход из цикла
     *
     * По поводу использования коллекции и предварительной загрузки вместо работы с потоком BufferedReader на чтение файла ответов
     * Надо уточнить у менторов: метод lines у bufferedreader ведёт себя очень странно и после одного обращения к стриму(lines) после терминальной операции
     * оставляет итератор - где бог на душу положил. Поэтому повторный проход - не имеет смысла
     * Есть возможность маркировки (до lines делаем mark, и потом через reset можем вернуться туда, где поставили закладку) - но это черная магия
     */
    public void wannaSpeak() {
	if (systemAnswersList.isEmpty()) {
	    loadFile(pathSourceOfAnswers);
	}
	try (PrintWriter writerLog = new PrintWriter(new FileWriter(pathLogOfConversation.toString()), true);
	     BufferedReader readerUser = new BufferedReader(new InputStreamReader(System.in))) {
	    Random rnd = new Random();
	    boolean userDesireOfSpeaking = true;
	    String wordUser = "", wordSystem = "";
	    while (!wordUser.equals(FINISH_WORD)) {
		System.out.println("Say something human: ");
		System.out.println("Команды: Закончить, Стоп, Продолжить");
		wordUser = readerUser.readLine();
		switch (wordUser) {
		    case FINISH_WORD:
		    case STOP_WORD:
			userDesireOfSpeaking = false;
			wordSystem = "";
			break;
		    case CONTINUE_WORD:
			userDesireOfSpeaking = true;
			wordSystem = "";
			break;
		    default:
			wordSystem = userDesireOfSpeaking ? systemAnswersList.get(rnd.nextInt(systemAnswersList.size())) : "";
			break;
		}
		if (userDesireOfSpeaking) {
		    System.out.println(wordSystem + System.lineSeparator());
		}
		writerLog.printf("%s  -  %s" + System.lineSeparator(), wordUser, wordSystem);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /**
     * Проверка, что файл ответов существует. Boolean тут ни к чему по сути, но хочется поставить
     *
     * @param path путь до файла
     */
    private static boolean isExistAndFile(Path path) {
	if (!path.toFile().exists() && !path.toFile().isFile()) {
	    throw new IllegalArgumentException("File of answers doesn't exist or was input wrong");
	}
	return true;
    }

    /**
     * Заполнение коллекции ответов из файла с внутренней проверкой на возможность оного
     * Почему она вообще нужна - читай speakWithMe
     */
    private boolean loadFile(Path pathAnswerFile) {
	isExistAndFile(pathAnswerFile);
	try (BufferedReader reader = new BufferedReader(new FileReader(pathAnswerFile.toString()))) {
	    systemAnswersList = reader.lines().collect(Collectors.toList());
	} catch (Exception e) {
	    System.out.println("problem with load file");
	    e.printStackTrace();
	}
	return true;
    }
}
