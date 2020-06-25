package ru.job4j.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.*;
import java.util.Date;
import java.util.*;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class AlertRabbit implements AutoCloseable {
    private static final Logger LOG = LoggerFactory.getLogger(
            AlertRabbit.class.getName());
    private static final String INSERT_QUERY =
            "INSERT INTO rabbits(created)" + "VALUES(?)";
    /**
     * Если мы хотим работать с БД долго, connection лучше вынести
     */
    private Connection connection;
    /**
     * Файл настроек, вынесен для более простой навигации
     */
    private Properties cfg;

    public AlertRabbit(Properties cfg) {
        this.cfg = cfg;
    }

    /**
     * Дефолтная загрузка properties в cfg
     */
    private static Properties loadCfg() {
        Properties cfg = null;
        try {
            cfg = new Properties();
            cfg.load(AlertRabbit.class.getClassLoader()
                                      .getResourceAsStream(
                                              "rabbit.properties"));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return cfg;
    }

    /**
     * Подключаемся к СУБД и инициализируем connection
     */
    private void initConnection() {
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
            connection = DriverManager.getConnection(
                    cfg.getProperty("jdbc.url"),
                    cfg.getProperty("jdbc.username"),
                    cfg.getProperty("jdbc.password"));
        } catch (ClassNotFoundException e) {
            LOG.error("Проблема с ресурсами", e);
        } catch (SQLException e) {
            LOG.error("Проблема с подключением", e);
        }
    }

    public static void main(String[] args) {
        try (AlertRabbit rabbit = new AlertRabbit(loadCfg())) {
            rabbit.initConnection();
            try {
                List<String> data = new ArrayList<>();
                Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
                scheduler.start();
                JobDataMap jobData = new JobDataMap();
                jobData.put("connection", rabbit.connection);
                jobData.put("insertQuery", INSERT_QUERY);
                jobData.put("data", data);
                jobData.put("log", LOG);
                JobDetail job = newJob(Rabbit.class).usingJobData(jobData)
                                                    .build();
                SimpleScheduleBuilder times = simpleSchedule().withIntervalInSeconds(
                        Integer.parseInt(
                                rabbit.cfg.getProperty("rabbit.interval")))
                                                              .repeatForever();
                Trigger trigger = newTrigger().startNow()
                                              .withSchedule(times)
                                              .build();
                scheduler.scheduleJob(job, trigger);
                Thread.sleep(10000);
                data.forEach(System.out::println);
                scheduler.shutdown();
            } catch (SchedulerException se) {
                LOG.error("проблема с шедулером", se);
            }
        } catch (Exception e) {
            LOG.error("Вообще всё плохо - connection не закрылся видимо", e);
        }
    }

    @Override
    public void close() throws Exception {
        if (!Objects.isNull(connection)) {
            connection.close();
        }
    }

    /**
     * Выносим его в статик(inner), иначе Job не будет нормально отрабатывать
     * из-за проблем с доступом (только nested классы что ли?)
     */
    public static class Rabbit implements Job {
        /**
         * Берем из мапы все наши данные, которые нужны
         * - Connection connection (ЗА ЕГО ЗАКРЫТИЕ ОТВЕЧАЕТ main, здесь
         * закрывать ничего не нужно)
         * - String Sql - insert скрипт
         * - List data - для записи данных
         * - Log log- - логгер
         * Да, к ним можно было бы получить доступ и так(кроме локального
         * list), потому что они у нас глобальные, но идея именно в
         * использовании чего-то переданного
         */
        @Override
        public void execute(JobExecutionContext context)
                throws JobExecutionException {
            JobDataMap map = context.getJobDetail().getJobDataMap();
            List<String> data = (List<String>) map.get("data");
            Logger log = (Logger) map.get("log");
            String insertQuery = map.getString("insertQuery");
            try {
                Connection cn = (Connection) map.get("connection");
                try (PreparedStatement statement = cn.prepareStatement(
                        insertQuery, Statement.RETURN_GENERATED_KEYS)) {
                    long currentTime = System.currentTimeMillis();
                    statement.setDate(1, new java.sql.Date(currentTime));
                    if (0 < statement.executeUpdate()) {
                        try (ResultSet keys = statement.getGeneratedKeys()) {
                            while (keys.next()) {
                                data.add(String.format("id: %d created: %s",
                                                       keys.getInt("id"),
                                                       new Date(
                                                               currentTime).toString()));
                            }
                        }
                    }
                }
            } catch (SQLException sqle) {
                log.error("execute упал с подключениями к БД", sqle);
            }
        }
    }
}