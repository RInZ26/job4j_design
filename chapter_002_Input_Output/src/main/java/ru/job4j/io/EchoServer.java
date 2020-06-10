package ru.job4j.io;

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

    /**
     * Вопрос - ответ на запросы пользовалея, возможные служебные варинты
     * реализованы через dispatcher messageFromRequest - та часть запроса,
     * которая идёт после http://localhost:9000/?msg="Вот эта часть" Так как
     * использовать регулярки - плохо, то здесь всё также делается примитивно
     * через работу со стрингом Важно! На забывать отправить OK_Request клиенту,
     * потому что иначе страница будет висеть.
     */
    public static void main(String[] args) throws IOException {
	try (ServerSocket server = new ServerSocket(9000)) {
	    DispatcherTextRequest dispatcherTextRequest =
		    new DispatcherTextRequest(server);
	    while (!server.isClosed()) {
		Socket socket = server.accept();
		try (OutputStream out = socket.getOutputStream();
		     BufferedReader in = new BufferedReader(
			     new InputStreamReader(socket.getInputStream()))) {
		    String str, messageFromRequest = null;
		    while (!(str = in.readLine()).isEmpty()) {
			if (messageFromRequest
				== null) { //first line from current session
			    messageFromRequest = getMessageFromRequest(str);
			}
			System.out.println(str);
		    }
		    if (messageFromRequest != null && !messageFromRequest
			    .equals("")) {
			out.write(OK_REQUEST.getBytes());
			out.write(dispatcherTextRequest.mapOfSystemUserRequests
					  .getOrDefault(messageFromRequest
								.toLowerCase(),
							dispatcherTextRequest
								.defaultSupplier(
									messageFromRequest))
					  .get().getBytes());
		    }
		}
	    }
	}
    }

    /**
     * Попытка поймать msg, которым мы получем
     *
     * @param request
     *
     * @return
     */
    private static String getMessageFromRequest(String request) {
	String preMessageTemplate = "/?msg=";
	int indexOfMsgStart = request.indexOf("/?msg=");
	return indexOfMsgStart != -1 ? request
		.substring(indexOfMsgStart + preMessageTemplate.length())
		.split(" ")[0] : "";
    }

    /**
     * Диспачтер, смотри SpeakWithMe
     */
    private static class DispatcherTextRequest {
	private static final String MESSAGE_HELLO = "hello";
	private static final String MESSAGE_EXIT = "exit";
	private static final String MESSAGE_WHAT = "what";
	ServerSocket server;
	Map<String, Supplier<String>> mapOfSystemUserRequests = new HashMap<>();

	/**
	 * Чтобы не выносить в глобал переменные, которые выносить вовсе не
	 * нужно, проще их тут закинуть В данном случае мы работамем с сервером,
	 * поэтому нам нужна ссылка на него
	 *
	 * @param serverSocket
	 */
	DispatcherTextRequest(ServerSocket serverSocket) {
	    this.server = serverSocket;
	    initDispatcher();
	}

	/**
	 * заполенение мапы нашими связками
	 */
	public void initDispatcher() {
	    mapOfSystemUserRequests.put(MESSAGE_HELLO, msgHelloSupplier());
	    mapOfSystemUserRequests.put(MESSAGE_WHAT, msgWhatSupplier());
	    mapOfSystemUserRequests.put(MESSAGE_EXIT, msgExitSupplier());
	}

	private Supplier<String> msgHelloSupplier() {
	    return (() -> "Hello there");
	}

	private Supplier<String> msgExitSupplier() {
	    return (() -> {
		try {
		    server.close();
		} catch (IOException io) {
		    System.out
			    .println("close Server by dispatcher exitSupplier");
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