package ru.job4j.srp.report;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class JsonGeneratorTest {
    @Test
    public void simpleTest() {
        Employee nastya = new Employee("Nastya", 0);
        Employee katya = new Employee("Katya", 100);
        Store store = new MemStore() {
            {
                add(nastya);
                add(katya);
            }
        };
        Map<String, String> fields = new HashMap<>() {
            {
                put("name", "ФИО");
                put("salary", "Заработная плата");
            }
        };
        Report report = Report.create().withFields(fields)
                              .build();
        StringJoiner expected = new StringJoiner(System.lineSeparator());
        expected.add("{")
                .add("\"employees\" : [")
                .add("{")
                .add("\"name\" : \"Katya\",")
                .add("\"salary\" : 100.0")
                .add("},")
                .add("{")
                .add("\"name\" : \"Nastya\",")
                .add("\"salary\" : 0.0")
                .add("}")
                .add("]")
                .add("}");

        assertThat(
                ReportFabric.createReport(ReportFabric.Type.JSON, report, store)
                            .generateReport(), is(expected.toString()));
    }

}
