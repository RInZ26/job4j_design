package ru.job4j.io;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class AnalizyTest {
    String sourceFileName = "AnalyzeLog.txt";
    String targetFileName = "unavailable.txt";
    List<String> listOfTestingRecords;
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    File sourceFile;
    File targetFile;

    @Before
    public void setUp() throws IOException {
	sourceFile = folder.newFile(sourceFileName);
	targetFile = folder.newFile(targetFileName);
    }

    @Test
    public void whenLogHasTrashString() {
	listOfTestingRecords = Arrays.asList("### 32200 10:56:01", " 200 10:56:01", "200 10:56:01",
		"500 10:57:01",
		"400 10:58:01",
		"200 10:59:01",
		"ups 400 34:23:11",
		"500 11:01:02",
		"200 11:02:02");
	assertThat(checkThisData(listOfTestingRecords),
		is("500 10:57:01 200 10:59:01 500 11:01:02 200 11:02:02"));
    }

    @Test
    public void whenLogIsEmpty() {
	assertThat(checkThisData(Collections.EMPTY_LIST), is(""));
    }

    @Test
    public void whenLogHasStartAndHasntFinishOfTroublePeriod() {
	listOfTestingRecords = Arrays.asList("200 12:02:12",
		"500 11:01:02",
		"400 11:22:11",
		"300 12:02:12",
		"500 11:01:02",
		"400 11:22:11");
	assertThat(checkThisData(listOfTestingRecords), is("500 11:01:02 300 12:02:12 500 11:01:02 neverEnd"));
    }

    @Test
    public void whenLogHasAndOnlyErrorRecords() {
	listOfTestingRecords = Arrays.asList("400 12:02:12",
		"500 11:01:02",
		"400 11:22:11");
	assertThat(checkThisData(listOfTestingRecords), is("400 12:02:12 neverEnd"));
    }

    @Test
    public void whenLogHasZeroErrorRecords() {
	listOfTestingRecords = Arrays.asList("200 12:02:12",
		"300 11:01:02",
		"100 11:22:11",
		"000 12:02:12",
		"800 11:01:02",
		"900 11:22:11");
	assertThat(checkThisData(listOfTestingRecords), is(""));
    }

    /**
     * Следуем DRY и не пишем одно и то же миллион раз.
     * <p>
     * Внутри обязательно флашим данные, так как у нас в одном и том же трае идёт ещё и чтение того, что мы записали, а
     * FLUSH АВТОМАТИЧЕСКИ СРАБОТАЕТ ТОЛЬКО ПОСЛЕ ТРАЯ!!!!
     * Поэтому нужно ручками
     *
     * @param testedData различный набор данных
     */
    private String checkThisData(List<String> testedData) {
	StringBuilder allTextFromFile = new StringBuilder();
	try (PrintWriter out = new PrintWriter(sourceFile);
	     BufferedReader in = new BufferedReader(new FileReader(targetFile))) {
	    testedData.forEach(line -> out.print(line + "\r"));
	    out.flush();
	    Analizy.unavailable(sourceFile.getAbsolutePath(), targetFile.getAbsolutePath());
	    in.lines().forEach(word -> allTextFromFile.append(word).append(" "));
	    allTextFromFile.deleteCharAt(allTextFromFile.length() - 1); // удаление лишнего пробела в конце
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return allTextFromFile.toString();
    }
}