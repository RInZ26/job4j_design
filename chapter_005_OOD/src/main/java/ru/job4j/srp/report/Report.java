package ru.job4j.srp.report;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Class which represent Employee as Report with selected options in builder
 */
public class Report {
    /**
     * Dispatcher for generate
     */
    private static Map<String, Function<Employee, Object>> fieldsDispatcher = new HashMap<>() {
        {
            put("name", (em) -> em.getName());
          /*  put("hired", (em) -> em.getHired());
            put("fired", (em) -> em.getFired());*/
            put("salary", (em) -> em.getSalary());
            put("default", (em) -> null);
        }
    };
    /**
     * Fields what will be used in report. The key - name of employee field,
     * the value - how it will be written in report. If value is null - then
     * will be used key instead
     */
    private Map<String, String> fields = new HashMap<>() {
        {
            put("name", "name");
       /*     put("hired", "hired");
            put("fired", "fired");*/
            put("salary", "salary");
        }
    };
    /**
     * Default filter for Store
     */
    private Predicate<Employee> filter = e -> true;
    /**
     * Default comparator for sort
     */
    private Comparator<Employee> comparator = Comparator.comparing(
            Employee::getName);
    /**
     * Default delimiter for report
     */
    private String delimiter = " ";

    /**
     * Private constructor for inner builder
     */
    private Report() {
    }

    /**
     * Start ReportBuilder chain creating
     */
    public static ReportBuilder create() {
        return new ReportBuilder();
    }

    public static Map<String, Function<Employee, Object>> getFieldsDispatcher() {
        return fieldsDispatcher;
    }

    public Map<String, String> getFields() {
        return fields;
    }

    public Predicate<Employee> getFilter() {
        return filter;
    }

    public Comparator<Employee> getComparator() {
        return comparator;
    }

    public String getDelimiter() {
        return delimiter;
    }

    /**
     * Class- builder for ReportEngine
     */
    public static class ReportBuilder {
        private Report report;

        private ReportBuilder() {
            report = new Report();
        }

        public ReportBuilder withFields(Map<String, String> fields) {
            report.fields = fields;
            return this;
        }

        public ReportBuilder withFilter(
                java.util.function.Predicate<Employee> filter) {
            report.filter = filter;
            return this;
        }

        public ReportBuilder withComparator(Comparator<Employee> comparator) {
            report.comparator = comparator;
            return this;
        }

        public ReportBuilder withDelimiter(String delimiter) {
            report.delimiter = delimiter;
            return this;
        }

        /**
         * finish of building
         */
        public Report build() {
            return report;
        }
    }
}