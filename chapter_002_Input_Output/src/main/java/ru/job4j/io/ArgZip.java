package ru.job4j.io;

import java.nio.file.Paths;

/**
 * Класс для работы с параметрами у main и вызовом zip
 */
public class ArgZip {
    /**
     * Те самые параметры main
     */
    private final String[] args;

    public ArgZip(String[] args) {
	this.args = args;
    }

    /**
     * Всё ли ок с args
     */
    public boolean valid() {
	if (args.length != 3) {
	    throw new IllegalArgumentException(String.format("%s ARGUMENTS. Need only 3. 1) SOURCE DIRECTORY 2)EXTENSION OF EXCLUDED FILES 3) TARGET .zip",
		    args.length < 3 ? "NOT ENOUGH" : "MORE THAN NEED"));
	}
	return true;
    }

    public String directory() {
	return args[0];
    }

    public String exclude() {
	return args[1];
    }

    public String output() {
	return args[2];
    }

    public static void main(String[] args) {
	ArgZip argZip = new ArgZip(args);
	argZip.valid();
	new Zip().packFiles(Search.search(Paths.get(argZip.directory()), argZip.exclude(), true), Paths.get(argZip.output()));
    }
}
