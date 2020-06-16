package ru.job4j.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;


/**
 Класс для одиноких людей
 */
public class SpeakWithMe {
    /**
     Логгер для класса
     */
    private static final Logger LOG = LoggerFactory.getLogger(
            SpeakWithMe.class.getName());
    /**
     Попробуем вынести желание пользователя сюда в глобал [update]Это глупо,
     потому что можно просто их передавать в диспатчер
     */
    private static boolean userDesireOfSpeaking = true;
    private static boolean isTheEnd;

    public static void main(String[] args) {
        wannaSpeak(Paths.get("C:\\projects\\SpeakWithMe\\systemAnswers.txt"),
                   Paths.get(
                           "C:\\projects\\SpeakWithMe\\logOfConversation.txt"));
    }

    /**
     Запись в коллекцию из файл при частом обращении - оправдано! Потому что
     операция чтения дорогая. Держать стрим на запись долго - не надо, поэтому
     лог формируется в конце в отдельном минитрае Используется паттерн dispatcher
     в том или ином виде. Логика - избегать ифы и изменять код при расширении не
     здесь, а в диспатчере По поводу работы самого автоответчика: Есть два глобал
     булиана: isTheEnd - завершение работы, userDesireOfSpeaking - желание юзера
     получать ответы от системы Поддерживается три системные команды - они
     описаны в диспатчере.
     */
    public static void wannaSpeak(Path pathSourceOfAnswers,
                                  Path pathLogOfConversation) {
        List<String> listOfSystemAnswers;
        List<String> logOfConversation;
        try (BufferedReader userReader = new BufferedReader(
                new InputStreamReader(System.in))) {
            Dispatcher dispatcher = new Dispatcher();
            listOfSystemAnswers = loadFile(pathSourceOfAnswers);
            logOfConversation = new ArrayList<>();
            String userQuestion, systemAnswer;
            while (!isTheEnd) {
                System.out.println("Write something: ");
                userQuestion = userReader.readLine();
                System.out.println(
                        systemAnswer = dispatcher.systemReactions.getOrDefault(
                                userQuestion.toLowerCase(),
                                dispatcher.defaultSystemAnswer(
                                        listOfSystemAnswers)).get());
                logOfConversation.add(
                        String.format("%s  -  %s", userQuestion, systemAnswer));
            }
            try (PrintWriter writerLog = new PrintWriter(
                    new FileWriter(pathLogOfConversation.toString()))) {
                logOfConversation.forEach(writerLog::println);
            } catch (Exception e) {
                LOG.error("load log", e);
            }
        } catch (Exception e) {
            LOG.error("reader init", e);
        }
    }

    /**
     Проверка, что файл ответов существует. Boolean тут ни к чему по сути, но
     хочется поставить

     @param path
     путь до файла
     */
    private static boolean isExistAndFile(Path path) {
        if (!path.toFile().exists() && !path.toFile().isFile()) {
            throw new IllegalArgumentException(
                    "File of answers doesn't exist or was input wrong");
        }
        return true;
    }

    /**
     Заполнение коллекции ответов из файла с внутренней проверкой на возможность
     оного Почему она вообще нужна - читай speakWithMe
     */
    private static List<String> loadFile(Path pathAnswerFile) {
        isExistAndFile(pathAnswerFile);
        try (BufferedReader reader = new BufferedReader(
                new FileReader(pathAnswerFile.toString()))) {
            return reader.lines().collect(Collectors.toList());
        } catch (Exception e) {
            LOG.error("problem with load file", e);
        }
        return Collections.emptyList();
    }

    /**
     Используем хитрый диспатч вместо свичей https://github.com/peterarsentev/code_quality_principles
     У нас есть мапа по ключевым словам, за которыми закреплены определенные
     действия, выглядит неплохо, хотя и пришлось вынести булианы в global
     */
    private static class Dispatcher {
        private static final String FINISH_WORD = "закончить";
        private static final String STOP_WORD = "стоп";
        private static final String CONTINUE_WORD = "продолжить";
        private static final String SYSTEM_SILENCED = "";
        Map<String, Supplier<String>> systemReactions = new HashMap<>();

        Dispatcher() {
            init();
        }

        // Init all system words and it functions
        void init() {
            systemReactions.put(FINISH_WORD, finishWordSupplier());
            systemReactions.put(CONTINUE_WORD, continueWordSupplier());
            systemReactions.put(STOP_WORD, stopWordSupplier());
        }

        /**
         Этот метод и далее просто заносят логику действий, которые должны
         выполняться при вводе команды пользователем
         */
        private Supplier<String> finishWordSupplier() {
            return () -> {
                userDesireOfSpeaking = false;
                isTheEnd = true;
                return SYSTEM_SILENCED;
            };
        }

        private Supplier<String> continueWordSupplier() {
            return () -> {
                userDesireOfSpeaking = true;
                return SYSTEM_SILENCED;
            };
        }

        private Supplier<String> stopWordSupplier() {
            return () -> {
                userDesireOfSpeaking = false;
                return SYSTEM_SILENCED;
            };
        }

        /**
         Эта штука выполняется, когда ничего не нашлось в мапе посредством
         getOrDefault

         @param listOfSystemAnswers
         ссылка на коллекцию, с которой работаем
         */
        private Supplier<String> defaultSystemAnswer(
                List<String> listOfSystemAnswers) {
            Random rnd = new Random();
            return () -> userDesireOfSpeaking
                    ? listOfSystemAnswers.get(
                    rnd.nextInt(listOfSystemAnswers.size()))
                    : SYSTEM_SILENCED;
        }

    }
    //Попытка дружить с enum вместо стрингов
/*
    enum SystemAnswers {
	FINISH_WORD("Закончить"),
	STOP_WORD("Стоп"),
	CONTINUE_WORD("Продолжить");
	String value;

	SystemAnswers(String value) {
	    this.value = value;
	}
    }
    */
}