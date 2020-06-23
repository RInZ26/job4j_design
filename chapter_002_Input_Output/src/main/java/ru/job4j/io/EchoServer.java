package ru.job4j.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class EchoServer {
    private static final String OK_REQUEST = "HTTP/1.1 200 OK\r\n\r\n";
    private static final Logger LOG = LoggerFactory.getLogger(
            EchoServer.class.getName());

    /**
     Вопрос - ответ на запросы пользовалея, возможные служебные варинты
     реализованы через dispatcher messageFromRequest - та часть запроса, которая
     идёт после http://localhost:9000/?msg="Вот эта часть" Так как использовать
     регулярки - плохо, то здесь всё также делается примитивно через работу со
     стрингом Важно! На забывать отправить OK_Request клиенту, потому что иначе
     страница будет висеть. message - сообщение от пользователя
     */
    public static void main(String[] args) throws IOException {
        try (ServerSocket server = new ServerSocket(9000)) {
            Dispatcher dispatcher = new Dispatcher(server);
            while (!server.isClosed()) {
                Socket socket = server.accept();
                try (OutputStream out = socket.getOutputStream();
                     BufferedReader in = new BufferedReader(
                             new InputStreamReader(socket.getInputStream()))) {
                    String line, message = null;
                    while (!(line = in.readLine()).isEmpty()) {
                        if (message == null) { //first line from current session
                            message = getMessage(line);
                        }
                        System.out.println(line);
                    }
                    if (message != null && !message.equals("")) {
                        out.write(OK_REQUEST.getBytes());
                        out.write(dispatcher.systemWords.getOrDefault(
                                message.toLowerCase(),
                                dispatcher.defaultSupplier(message))
                                                        .get()
                                                        .getBytes());
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("init server", e);
        }
    }

    /**
     Попытка поймать msg, которым мы получем

     @param request

     @return
     */
    private static String getMessage(String request) {
        String preMessageTemplate = "/?msg=";
        int indexOfMsgStart = request.indexOf("/?msg=");
        return indexOfMsgStart != -1 ? request.substring(
                indexOfMsgStart + preMessageTemplate.length()).split(" ")[0]
                : "";
    }

    /**
     Диспачтер, смотри SpeakWithMe
     */
    private static class Dispatcher {
        private static final String MESSAGE_HELLO = "hello";
        private static final String MESSAGE_EXIT = "exit";
        private static final String MESSAGE_WHAT = "what";
       private ServerSocket server;
        private Map<String, Supplier<String>> systemWords = new HashMap<>();

        /**
         Чтобы не выносить в глобал переменные, которые выносить вовсе не нужно,
         проще их тут закинуть В данном случае мы работамем с сервером, поэтому
         нам нужна ссылка на него

         @param serverSocket
         */
        Dispatcher(ServerSocket serverSocket) {
            this.server = serverSocket;
            initDispatcher();
        }

        /**
         заполенение мапы нашими связками
         */
        public void initDispatcher() {
            systemWords.put(MESSAGE_HELLO, msgHelloSupplier());
            systemWords.put(MESSAGE_WHAT, msgWhatSupplier());
            systemWords.put(MESSAGE_EXIT, msgExitSupplier());
        }

        private Supplier<String> msgHelloSupplier() {
            return (() -> "Hello there");
        }

        private Supplier<String> msgExitSupplier() {
            return (() -> {
                try {
                    server.close();
                } catch (IOException io) {
                    LOG.error("close Server by dispatcher exitSupplier", io);
                }
                return "";
            });
        }

        private Supplier<String> msgWhatSupplier() {
            return (() -> "What what?");
        }

        private Supplier<String> defaultSupplier(String request) {
            return () -> request;
        }
    }
}