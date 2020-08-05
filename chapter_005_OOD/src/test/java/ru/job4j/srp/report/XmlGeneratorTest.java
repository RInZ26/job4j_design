package ru.job4j.srp.report;

import org.junit.Test;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class XmlGeneratorTest {
    @Test
    public void simpleTest() {
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
                put("name", null);
                put("salary", null);
            }
        };
        Report report = Report.create()
                              .withFields(fields)
                              .withFilter(e -> e.getSalary() < 150)
                              .withComparator(
                                      Comparator.comparing(Employee::getName)
                                                .reversed())
                              .build();
        String generated = ReportFabric.createReport(ReportFabric.Type.XML,
                                                     report, store)
                                       .generateReport();
        StringJoiner expected = new StringJoiner(System.lineSeparator());
        expected
                .add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                .add("<employeeList>")
                .add("<employee>")
                .add("<name>")
                .add("Nastya")
                .add("</name>")
                .add("<salary>")
                .add("0.0")
                .add("</salary>")
                .add("</employee>")
                .add("<employee>")
                .add("<name>")
                .add("Katya")
                .add("</name>")
                .add("<salary>")
                .add("100.0")
                .add("</salary>")
                .add("</employee>")
                .add("</employeeList>");
        assertThat(generated, is(expected.toString()));
    }
}
