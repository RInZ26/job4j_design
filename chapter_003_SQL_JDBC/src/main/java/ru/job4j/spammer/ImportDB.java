package ru.job4j.spammer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 Класс для записи данных из файла в бд, так как это наш ресурс - используется
 autocloseable
 */
public class ImportDB implements AutoCloseable {

    private static final String SAVE_QUERY =
            "INSERT INTO users(name, email) " + "VALUES(?, ?)";
    /**
     Файл параметров для подключения к СУБД
     */
    private Properties cfg;
    /**
     Имя/путь файла, который содержит данные для БД
     */
    private String dump;

    /**
     Наше подключение к бд
     */
    private Connection connection;

    /**
     Дефолтный конструткор
     */
    public ImportDB(Properties cfg, String dump) {
        this.cfg = cfg;
        this.dump = dump;
    }

    public static void main(String[] args) throws Exception {
        Properties cfg = new Properties();
        try (InputStream in = ImportDB.class.getClassLoader()
                                            .getResourceAsStream(
                                                    "app.properties")) {
            cfg.load(in);
        }
        try (ImportDB db = new ImportDB(cfg, "./dump.txt")) {
            db.init();
            db.save(db.load());
        }
    }

    /**
     Инициализация подключения к СУБД
     */
    private void init() throws ClassNotFoundException, SQLException {
        Class.forName(cfg.getProperty("jdbc.driver"));
        connection = DriverManager.getConnection(cfg.getProperty("jdbc.url"),
                                                 cfg.getProperty(
                                                         "jdbc.username"),
                                                 cfg.getProperty(
                                                         "jdbc.password"));
    }

    /**
     Загрузка файла для последующей отправки в БД
     */
    public List<User> load() throws IOException {
        List<User> users = Collections.emptyList();
        try (BufferedReader rd = new BufferedReader(new FileReader(dump))) {
            users = rd.lines().map(line -> {
                String[] splitted = line.split(";");
                return splitted.length > 1 ? new User(splitted[0], splitted[1])
                        : new User(splitted[0], null);
            }).collect(Collectors.toList());
        }

        return users;

    }

    /**
     Insert данных в ДБ
     Здесь statement можно явно не закрывать, потому что Connection в этом
     же блоке

     @param users
     вставляемый лист
     */
    public void save(List<User> users) {
        if (connection != null) {
            for (User user : users) {
                try (PreparedStatement statement = connection.prepareStatement(
                        SAVE_QUERY)) {
                    statement.setString(1, user.name);
                    statement.setString(2, user.email);
                    statement.executeUpdate();
                } catch (SQLException sqlE) {
                    sqlE.printStackTrace();
                }
            }
        } else {
            System.out.println("Connection is nonexistent");
        }
    }

    @Override
    public void close() throws Exception {
        if (!Objects.isNull(connection)) {
            connection.close();
        }
    }

    /**
     Миникласс - модель данных, сопоставимая с таблицей в БД
     */
    private static class User {
        private String name;
        private String email;

        public User(String name, String email) {
            this.name = name;
            this.email = email;
        }
    }
}
