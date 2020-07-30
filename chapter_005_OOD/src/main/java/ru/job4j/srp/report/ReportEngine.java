package ru.job4j.srp.report;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class ReportEngine {
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
     * Data supplier
     */
    private Store store;
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
    private ReportEngine(Store store) {
        this.store = store;
    }

    /**
     * Start ReportBuilder chain creating
     */
    public static ReportBuilder create(Store store) {
        return new ReportBuilder(store);
    }

    /**
     * Generate report by selected or default options which were chosen in
     * ReportEngineBuilder
     */
    public String generate() {
        StringBuilder report = new StringBuilder("");
        fillTitles(report);
        List<Employee> employees = store.findBy(filter);
        sortByComparator(employees);
        fillEmployees(employees, report);
        return report.toString();
    }

    /**
     * Fill titles into StringBuilder accordding to map fields
     */
    private void fillTitles(StringBuilder report) {
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            report.append(Optional.ofNullable(entry.getValue())
                                  .orElseGet(entry::getKey))
                  .append(delimiter);
        }
        report.append(System.lineSeparator());
    }

    /**
     * Sorting list of Employees by Comparator
     */
    private void sortByComparator(List<Employee> employees) {
        Collections.sort(employees, comparator);
    }

    /**
     * Fill employees into StringBuilder according to selected fields in map
     * fields. In case wrong key - return default
     */
    private void fillEmployees(List<Employee> employees, StringBuilder report) {
        for (Employee employee : employees) {
            for (String key : fields.keySet()) {
                report.append(fieldsDispatcher.getOrDefault(key,
                                                            fieldsDispatcher.get(
                                                                    "default"))
                                              .apply(employee))
                      .append(delimiter);
            }
            report.append(System.lineSeparator());
        }
    }

    private void modeHTML(StringBuilder report) {
        report.insert(0, "<html>\n" + "<head>\n" + "<meta charset=\"utf=8\">\n"
                + "<Employee report>\n" + "</head>\n" + "<body>\n" + "<table>");
    }

    private String surroundByTag(String line, String tag) {
        StringBuilder tagBuilder = new StringBuilder(line);
        tagBuilder.insert(0, "<" + tag + ">");
        tagBuilder.append("</" + tag + ">");
        return tagBuilder.toString();
    }

    /**
     * Class- builder for ReportEngine
     */
    public static class ReportBuilder {
        private ReportEngine report;

        private ReportBuilder(Store store) {
            report = new ReportEngine(store);
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

        public ReportBuilder withType(ReportTypes type) {
            return this;
        }

        /**
         * finish of building
         */
        public ReportEngine build() {
            return report;
        }
    }

    public enum ReportTypes {
        HTML, DEFAULT;
    }
}


