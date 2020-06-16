package ru.job4j.io;

import java.io.FileInputStream;
import java.util.function.Predicate;

public class EvenNumberFile {
    public static void main(String[] args) {
        String filename = "even.txt";
        ResultFile.writeInFile(filename, new Object[]{1, 5, 15, 17},
                               System.lineSeparator());
        Predicate<Integer> isEven = (o -> o % 2 == 0);
        try (FileInputStream in = new FileInputStream(filename)) {
            StringBuilder infoFromFile = new StringBuilder();
            int nextByte;
            while ((nextByte = in.read()) != -1) {
                infoFromFile.append((char) nextByte);
            }
            for (String str : infoFromFile.toString()
                                          .split(System.lineSeparator())) {
                System.out.println(
                        isEven.test(Integer.parseInt(str)) + " " + str);
            }
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }
}