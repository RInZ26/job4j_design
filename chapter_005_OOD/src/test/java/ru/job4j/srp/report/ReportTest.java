package ru.job4j.srp.report;

import org.junit.Test;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ReportTest {
    @Test
    public void checkFillTitles() {
        Map<String, String> fields = new HashMap<>() {
            {
                put("name", null);
                put("salary", "money");
            }
        };
        Report report = Report.create()
                              .withFields(fields)
                              .withDelimiter(" ")
                              .build();
        String generated = ReportFabric.createReport(ReportFabric.Type.DEFAULT,
                                                     report, new MemStore())
                                       .generateReport();
        StringJoiner expected = new StringJoiner(System.lineSeparator());
        expected.add("name money ")
                .add("");
        assertThat(generated, is(expected.toString()));
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
        Report report = Report.create()
                              .withDelimiter(" ")
                              .build();
        StringJoiner expected = new StringJoiner(System.lineSeparator());
        expected.add("name salary ")
                .add("Anna 500.0 ")
                .add("Katya 300.0 ")
                .add("Nastya 200.0 ")
                .add("");
        String generated = ReportFabric.createReport(ReportFabric.Type.DEFAULT,
                                                     report, store)
                                       .generateReport();
        assertThat(generated, is(expected.toString()));
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
        Report report = Report.create()
                              .withDelimiter(" ")
                              .withComparator(
                                      Comparator.comparing(Employee::getSalary))
                              .build();
        StringJoiner expected = new StringJoiner(System.lineSeparator());
        expected.add("name salary ")
                .add("Nastya 0.0 ")
                .add("Katya 100.0 ")
                .add("Anna 200.0 ")
                .add("");
        assertThat(ReportFabric.createReport(ReportFabric.Type.DEFAULT, report,
                                             store)
                               .generateReport(), is(expected.toString()));
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
        Report report = Report.create()
                              .withDelimiter(" ")
                              .withFilter(e -> e.getName()
                                                .contains("ya"))
                              .build();
        StringJoiner expected = new StringJoiner(System.lineSeparator());
        expected.add("name salary ")
                .add("Katya 100.0 ")
                .add("Nastya 0.0 ")
                .add("");
        assertThat(ReportFabric.createReport(ReportFabric.Type.DEFAULT, report,
                                             store)
                               .generateReport(), is(expected.toString()));
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
        Report report = Report.create()
                              .withDelimiter(" ")
                              .build();
        StringJoiner expected = new StringJoiner(System.lineSeparator());
        expected.add("<html>")
                .add("<head>")
                .add("<meta charset=\"utf=8\">")
                .add("</head>")
                .add("<body>")
                .add("<table>")
                .add("<tr>")
                .add("<th>")
                .add("name")
                .add("</th>")
                .add("<th>")
                .add("salary")
                .add("</th>")
                .add("</tr>")
                .add("<tr>")
                .add("<td>")
                .add("Anna")
                .add("</td>")
                .add("<td>")
                .add("200.0")
                .add("</td>")
                .add("</tr>")
                .add("<tr>")
                .add("<td>")
                .add("Katya")
                .add("</td>")
                .add("<td>")
                .add("100.0")
                .add("</td>")
                .add("</tr>")
                .add("<tr>")
                .add("<td>")
                .add("Nastya")
                .add("</td>")
                .add("<td>")
                .add("0.0")
                .add("</td>")
                .add("</tr>")
                .add("</table>")
                .add("</body>")
                .add("</html>");

        assertThat(
                ReportFabric.createReport(ReportFabric.Type.HTML, report, store)
                            .generateReport(), is(expected.toString()));
    }

    @Test
    public void whenHTMLTypeWithOptions() {
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
        Map<String, String> fields = new HashMap<>() {
            {
                put("name", "ФИО");
                put("salary", "Заработная плата");
            }
        };
        Report report = Report.create()
                              .withFields(fields)
                              .withDelimiter(" ")
                              .withFilter(e -> e.getSalary() < 150)
                              .withComparator(
                                      Comparator.comparing(Employee::getName)
                                                .reversed())
                              .build();
        String generated = ReportFabric.createReport(ReportFabric.Type.HTML,
                                                     report, store)
                                       .generateReport();
        StringJoiner expected = new StringJoiner(System.lineSeparator());
        expected.add("<html>")
                .add("<head>")
                .add("<meta charset=\"utf=8\">")
                .add("</head>")
                .add("<body>")
                .add("<table>")
                .add("<tr>")
                .add("<th>")
                .add("ФИО")
                .add("</th>")
                .add("<th>")
                .add("Заработная плата")
                .add("</th>")
                .add("</tr>")
                .add("<tr>")
                .add("<td>")
                .add("Nastya")
                .add("</td>")
                .add("<td>")
                .add("0.0")
                .add("</td>")
                .add("</tr>")
                .add("<tr>")
                .add("<td>")
                .add("Katya")
                .add("</td>")
                .add("<td>")
                .add("100.0")
                .add("</td>")
                .add("</tr>")
                .add("</table>")
                .add("</body>")
                .add("</html>");
        assertThat(generated, is(expected.toString()));
    }
}