package io;

import java.io.FileOutputStream;
import java.util.Arrays;

/**
 * Класс для демонстрации работы FileInputStream
 */
public class ResultFile {

    public static int[][] getMultiplicationTable(int size) {
	int[][] multiplicationTable = new int[size][size];
	for (int c = 0; c < size; c++) {
	    for (int i = 0; i < size; i++) {
		multiplicationTable[c][i] = (c + 1) * (i + 1);
	    }
	}
	return multiplicationTable;
    }

    public static void main(String[] args) {
	try (FileOutputStream out = new FileOutputStream("multiplicationTable.txt")) {
	    for (int[] intArray : getMultiplicationTable(9)) {
		for (int i : intArray) {
		    out.write((i + " ").getBytes());
		}
		out.write(System.lineSeparator().getBytes());
	    }
	} catch (Exception e) {
	}
    }
}