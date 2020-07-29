package ru.job4j.gc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

//size ~390kb
public class User {
    private static int countOfAlive;
    private static int countOfCreated;
    private static int countOfKilled;
    private String name;
    private List<String> fatList = new ArrayList<>(100000); //для нагрузки

    public User(String name) {
        this.name = name;
       // System.out.printf("\nI am alive %s\n ", name);
        countOfAlive++;
        countOfCreated++;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.printf("\nI am dead %s   ", name);
        System.out.println("Free mem: " + (Runtime.getRuntime()
                                                  .freeMemory() / 1024));
        countOfKilled++;
        countOfAlive--;
    }

    public static void main(String[] args) throws Exception {
        test(Long.MAX_VALUE);
    }

    /**
     * Метод для "примеерной" оценки работы GC.
     * В цикле создаются мусорные объекты, + по условию (раз в 10 объетокв)
     * объекты заносят в лист, чтобы GC их не убивал сразу.
     * Ещё есть объекты с именем humongous(~ 2mB) - заведомо большие для heap
     * объекты,
     * которые GC должен по логике сразу кидать в Tenured
     *
     * @param count - кол-во объектов, которые создаём
     *
     * @throws Exception
     */
    public static void test(long count) throws Exception {
        long kb = 1024;
        List<User> survivedList = new ArrayList<>();
        User humongous;
        for (int c = 0; c < count; c++) {
            if (c % 10 == 0) {
                survivedList.add(new User("I will survive " + c));
            }
            if (c % 100 == 0) {
                System.out.println("Free mem: " + (Runtime.getRuntime()
                                                          .freeMemory() / kb));
               humongous = new User("IAMHUMONGOUS " + c) {
                    private List<User> humongousList = new ArrayList<>();
                    {
                        for (int c = 0; c < 5; c++) {
                            humongousList.add(new User("humongous's"));
                        }
                    }
                };
                System.out.println("Free mem: " + (Runtime.getRuntime()
                                                          .freeMemory() / kb));
            }
            new User(String.valueOf(c));
            System.out.printf("Было создано: %s, Было убито %s, Выжили %s  ",
                              countOfCreated, countOfKilled, countOfAlive);
            System.out.println("Free mem: " + (Runtime.getRuntime()
                                                      .freeMemory() / kb));
        }
    }

    public static boolean generateBy(String origin, String line) {
        boolean result = true;
        String regex = "[,. " + "!:\"\']";
        Set<String> originWords = Arrays.stream(origin.split(regex))
                                        .collect(Collectors.toSet());
        for (String s : line.split(regex)) {
            if (!originWords.contains(s)) {
                result = false;
                break;
            }
        }
        return result;
    }
}