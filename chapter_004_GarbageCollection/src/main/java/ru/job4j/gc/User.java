package ru.job4j.gc;

import java.util.ArrayList;
import java.util.List;

//size ~390kb
public class User {
    private static int countOfAlive;
    private static int countOfCreated;
    private static int countOfKilled;
    private String name;
    private List<String> fatList = new ArrayList<>(100000); //для нагрузки

    public User(String name) {
        this.name = name;
        System.out.printf("\nI am alive %s\n ", name);
        countOfAlive++;
        countOfCreated++;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.printf("\nI am dead %s\n", name);
        countOfKilled++;
        countOfAlive--;
    }

    public static void main(String[] args) throws Exception {
        test(10000);
    }

    public static void test(int count) throws Exception {
        long kb = 1024;
        for (int c = 0; c < count; c++) {
            new User(String.valueOf(c));
            System.out.printf("Было создано: %s, Было убито %s, Выжили %s  ",
                              countOfCreated, countOfKilled, countOfAlive);
            System.out.println("Free mem: " + (Runtime.getRuntime()
                                                  .freeMemory() / kb));
        }
    }
}