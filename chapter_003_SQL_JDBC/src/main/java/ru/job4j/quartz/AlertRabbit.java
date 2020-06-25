package ru.job4j.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;
import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class AlertRabbit {
    public static void main(String[] args) {
        try {
            Properties cfg = new Properties();
            try {
                cfg.load(AlertRabbit.class.getClassLoader()
                                          .getResourceAsStream(
                                                  "rabbit.properties"));
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDetail job = newJob(Rabbit.class).build();
            SimpleScheduleBuilder times = simpleSchedule().withIntervalInSeconds(
                    Integer.parseInt(cfg.getProperty("rabbit.interval")))
                                                          .repeatForever();
            Trigger trigger = newTrigger().startNow()
                                          .withSchedule(times)
                                          .build();
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException se) {
            se.printStackTrace();
        }
    }

    public static class Rabbit implements Job {
        @Override
        public void execute(JobExecutionContext context)
                throws JobExecutionException {
            System.out.println("Rabbit runs here ... ");
        }
    }
}