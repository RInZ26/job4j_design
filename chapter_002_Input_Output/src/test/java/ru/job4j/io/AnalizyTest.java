package ru.job4j.io;

import org.junit.Test;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class AnalizyTest {
    List<String> listOfTestingRecords;
    String source = "AnalyzeLog.txt";
    String target = "unavailable.txt";

    /**
     * TODO Считать ли пробелы в исходном файле как мою проблему?
     * Нужны ли все эти проверки на то, что сперва идут пробелы и если есть съедать их через trim?
     * Или это не моя проблема и я просто отбрасываю эту строку
     * В текущей версии они отбрасываются
     */
    @Test
    public void whenLogHasTrashString() {
	listOfTestingRecords = Arrays.asList("### 32200 10:56:01", " 200 10:56:01", "200 10:56:01",
		"500 10:57:01",
		"400 10:58:01",
		"200 10:59:01",
		"ups 400 34:23:11",
		"500 11:01:02",
		"200 11:02:02");
	fillTheTestFile(listOfTestingRecords, source);
	Analizy.unavailable(source, target);
	assertThat(readFile(target), is(Arrays.asList("500 10:57:01 200 10:59:01", "500 11:01:02 200 11:02:02")));
    }

    @Test
    public void whenLogIsEmpty() {
	fillTheTestFile(Collections.EMPTY_LIST, source);
	Analizy.unavailable(source, target);
	assertThat(readFile(target), is(Collections.EMPTY_LIST));
    }

    @Test
    public void whenLogHasStartAndHasntFinishOfTroublePeriod() {
	listOfTestingRecords = Arrays.asList("200 12:02:12",
		"500 11:01:02",
		"400 11:22:11",
		"300 12:02:12",
		"500 11:01:02",
		"400 11:22:11");
	fillTheTestFile(listOfTestingRecords, source);
	Analizy.unavailable(source, target);
	assertThat(readFile(target), is(Arrays.asList("500 11:01:02 300 12:02:12", "500 11:01:02 neverEnd")));
    }

    @Test
    public void whenLogHasAndOnlyErrorRecords() {
	listOfTestingRecords = Arrays.asList("400 12:02:12",
		"500 11:01:02",
		"400 11:22:11");
	fillTheTestFile(listOfTestingRecords, source);
	Analizy.unavailable(source, target);
	assertThat(readFile(target), is(Arrays.asList("400 12:02:12 neverEnd")));
    }

    @Test
    public void whenLogHasZeroErrorRecords() {
	listOfTestingRecords = Arrays.asList("200 12:02:12",
		"300 11:01:02",
		"100 11:22:11",
		"000 12:02:12",
		"800 11:01:02",
		"900 11:22:11");
	fillTheTestFile(listOfTestingRecords, source);
	Analizy.unavailable(source, target);
	assertThat(readFile(target), is(Collections.EMPTY_LIST));
    }

    //** Некоторые удобные методы
    private void fillTheTestFile(List<String> listOfTestingRecords, String target) {
	try (PrintWriter out = new PrintWriter(new FileWriter(target))) {
	    listOfTestingRecords.forEach(o -> out.write(o + "\r"));
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private List<String> readFile(String source) {
	try (BufferedReader in = new BufferedReader(new FileReader(source))) {
	    return in.lines().collect(Collectors.toList());
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return Collections.EMPTY_LIST;
    }
}