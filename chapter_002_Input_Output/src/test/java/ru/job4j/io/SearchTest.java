package ru.job4j.io;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SearchTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void testExistExtension() {
	Path txtPath1 = null, txtPath2 = null, jsPath1 = null;
	Path rootPath;
	String extensionOfFile;
	rootPath = Paths.get(temporaryFolder.getRoot().getAbsolutePath());
	try {
	    txtPath1 = temporaryFolder.newFile("1.txt").toPath();
	    txtPath2 = temporaryFolder.newFile("2.txt").toPath();
	    jsPath1 = temporaryFolder.newFile("4.js").toPath();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	extensionOfFile = ".txt";
	assertThat(Search.search(rootPath, extensionOfFile),
		   is(Arrays.asList(txtPath1, txtPath2)));
    }

    @Test
    public void testEmptyExtension() {
	Path txtPath1 = null, txtPath2 = null, jsPath1 = null;
	Path rootPath;
	String extensionOfFile;
	rootPath = Paths.get(temporaryFolder.getRoot().getAbsolutePath());
	try {
	    txtPath1 = temporaryFolder.newFile("1.txt").toPath();
	    txtPath2 = temporaryFolder.newFile("2.txt").toPath();
	    jsPath1 = temporaryFolder.newFile("4.js").toPath();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	extensionOfFile = "";
	assertThat(Search.search(rootPath, extensionOfFile),
		   is(Arrays.asList(txtPath1, txtPath2, jsPath1)));
    }

    @Test
    public void testExtensionWhichDoesntExistInFolder() {
	Path txtPath1 = null, txtPath2 = null, jsPath1 = null;
	Path rootPath;
	String extensionOfFile;
	rootPath = Paths.get(temporaryFolder.getRoot().getAbsolutePath());
	try {
	    txtPath1 = temporaryFolder.newFile("1.txt").toPath();
	    txtPath2 = temporaryFolder.newFile("2.txt").toPath();
	    jsPath1 = temporaryFolder.newFile("4.js").toPath();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	extensionOfFile = ".docx";
	assertThat(Search.search(rootPath, extensionOfFile),
		   is(Collections.emptyList()));
    }
}