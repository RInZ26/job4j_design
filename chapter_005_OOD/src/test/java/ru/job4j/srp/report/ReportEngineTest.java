package ru.job4j.srp.report;

import org.junit.Test;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ReportEngineTest {
    @Test
    public void checkFillTitles() {
        Map<String, String> fields = new HashMap<>() {
            {
                put("name", null);
                put("salary", "money");
            }
        };
        ReportEngine report = ReportEngine.create(new MemStore())
                                          .withFields(fields)
                                          .withDelimiter(" ")
                                          .build();
        StringJoiner expected = new StringJoiner(System.lineSeparator());
        expected.add("name money ")
                .add("");
        assertThat(report.generate(), is(expected.toString()));
    }

    @Test
    public void checkFillEmployee() {
        Employee nastya = new Employee("Nastya", 200);
        Employee katya = new Employee("Katya", 300);
        Employee anya = new Employee("Anna", 500);
        Store store = new MemStore() {
            {
                add(nastya);
                add(katya);
                add(anya);
            }
        };
        ReportEngine report = ReportEngine.create(store)
                                          .withDelimiter(" ")
                                          .build();
        StringJoiner expected = new StringJoiner(System.lineSeparator());
        expected.add("name salary ")
                .add("Anna 500.0 ")
                .add("Katya 300.0 ")
                .add("Nastya 200.0 ")
                .add("");
        assertThat(report.generate(), is(expected.toString()));
    }

    @Test
    public void checkComparator() {
        Employee nastya = new Employee("Nastya", 0);
        Employee katya = new Employee("Katya", 100);
        Employee anya = new Employee("Anna", 200);
        Store store = new MemStore() {
            {
                add(nastya);
                add(anya);
                add(katya);
            }
        };
        ReportEngine report = ReportEngine.create(store)
                                          .withDelimiter(" ")
                                          .withComparator(Comparator.comparing(
                                                  Employee::getSalary))
                                          .build();
        StringJoiner expected = new StringJoiner(System.lineSeparator());
        expected.add("name salary ")
                .add("Nastya 0.0 ")
                .add("Katya 100.0 ")
                .add("Anna 200.0 ")
                .add("");
        assertThat(report.generate(), is(expected.toString()));
    }

    @Test
    public void checkFilter() {
        Employee nastya = new Employee("Nastya", 0);
        Employee katya = new Employee("Katya", 100);
        Employee anya = new Employee("Anna", 200);
        Store store = new MemStore() {
            {
                add(nastya);
                add(anya);
                add(katya);
            }
        };
        ReportEngine report = ReportEngine.create(store)
                                          .withDelimiter(" ")
                                          .withFilter(e -> e.getName()
                                                            .contains("ya"))
                                          .build();
        StringJoiner expected = new StringJoiner(System.lineSeparator());
        expected.add("name salary ")
                .add("Katya 100.0 ")
                .add("Nastya 0.0 ")
                .add("");
        assertThat(report.generate(), is(expected.toString()));
    }

    @Test
    public void whenHTMLType() {
        Employee nastya = new Employee("Nastya", 0);
        Employee katya = new Employee("Katya", 100);
        Employee anya = new Employee("Anna", 200);
        Store store = new MemStore() {
            {
                add(nastya);
                add(anya);
                add(katya);
            }
        };
        ReportEngine report = ReportEngine.create(store)
                                          .withDelimiter(" ")
                                          .withType(
                                                  ReportEngine.ReportTypes.HTML)
                                          .build();
        StringJoiner expected = new StringJoiner(System.lineSeparator());
        expected.add("<html>")
                .add("<head>")
                .add("<meta charset=\"utf=8\">")
                .add("<Employee report>")
                .add("</head>")
                .add("<body>")
                .add("<table>")
                .add("<tr>")
                .add("<th>name</th>")
                .add("<th>salary</th>")
                .add("</tr>")
                .add("<tr>")
                .add("<td>Anna</td>")
                .add("<td>200</td>")
                .add("</tr>")
                .add("<tr>")
                .add("<td>Katya</td>")
                .add("<td>100</td>")
                .add("</tr>")
                .add("<tr>")
                .add("<td>Nastya</td>")
                .add("<td>0</td>")
                .add("</tr>")
                .add("</table>")
                .add("</body>")
                .add("</html>")
                .add("");
        System.out.println(expected);
        assertThat(report.generate(), is(expected.toString()));
    }
}