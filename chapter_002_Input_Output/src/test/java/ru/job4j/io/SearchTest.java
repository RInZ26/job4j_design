package ru.job4j.io;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;


import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class SearchTest {
    Path txtPath1, txtPath2, jsPath1, fileEmpty;
    Path rootPath;
    String extensionOfFile;
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() {
	rootPath = Paths.get(temporaryFolder.getRoot().getAbsolutePath());
	try {
	    txtPath1 = temporaryFolder.newFile("1.txt").toPath();
	    txtPath2 = temporaryFolder.newFile("2.txt").toPath();
	    //fileEmpty = temporaryFolder.newFile("").toPath(); Это самой директорией является получается?
	    jsPath1 = temporaryFolder.newFile("4.js").toPath();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    @Test
    public void testExistExtension() {
	extensionOfFile = ".txt";
	assertThat(Search.search(rootPath, extensionOfFile), is(Arrays.asList(txtPath1, txtPath2)));
    }

    @Test
    public void testEmptyExtension() {
	extensionOfFile = "";
	assertThat(Search.search(rootPath, extensionOfFile), is(Arrays.asList(txtPath1, txtPath2, jsPath1)));
    }

    @Test
    public void testExtensionWhichDoesntExitInFolder() {
	extensionOfFile = ".docx";
	assertThat(Search.search(rootPath, extensionOfFile), is(Collections.emptyList()));
    }
}