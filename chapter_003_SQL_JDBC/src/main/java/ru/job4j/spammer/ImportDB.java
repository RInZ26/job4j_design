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
import java.util.Properties;
import java.util.stream.Collectors;

public class ImportDB {

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

    public ImportDB(Properties cfg, String dump) {
        this.cfg = cfg;
        this.dump = dump;
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
    public void save(List<User> users)
            throws ClassNotFoundException, SQLException {
        Class.forName(cfg.getProperty("jdbc.driver"));
        try (Connection cn = DriverManager.getConnection(
                cfg.getProperty("jdbc.url"), cfg.getProperty("jdbc.username"),
                cfg.getProperty("jdbc.password"))) {
            PreparedStatement statement;
            for (User user : users) {
                statement = cn.prepareStatement(SAVE_QUERY);
                statement.setString(1, user.name);
                statement.setString(2, user.email);
                statement.executeUpdate();
            }
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

    public static void main(String[] args) throws Exception {
        Properties cfg = new Properties();
        try (InputStream in = ImportDB.class.getClassLoader()
                                            .getResourceAsStream(
                                                    "app.properties")) {
            cfg.load(in);
        }
        ImportDB db = new ImportDB(cfg, "./dump.txt");
        db.save(db.load());
    }
}