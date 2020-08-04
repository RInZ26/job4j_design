package ru.job4j.srp.report;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DefaultGenerator extends ReportGenerator {
    private Report report;
    private Store store;

    public DefaultGenerator(Report report, Store store) {
        this.report = report;
        this.store = store;
    }

    @Override
    public String generateReport() {
        StringBuilder reportBody = new StringBuilder("");
        fillTitles(reportBody);
        List<Employee> employees = getData(store, report.getFilter());
        sortByComparator(employees);
        fillEmployees(employees, reportBody);
        return reportBody.toString();
    }

    @Override
    protected void fillTitles(StringBuilder reportBody) {
        for (Map.Entry<String, String> entry : report.getFields()
                                                     .entrySet()) {
            reportBody.append(Optional.ofNullable(entry.getValue())
                                      .orElseGet(entry::getKey))
                      .append(report.getDelimiter());
        }
        reportBody.append(System.lineSeparator());
    }

    @Override
    protected void sortByComparator(List<Employee> employees) {
        Collections.sort(employees, report.getComparator());
    }

    @Override
    protected void fillEmployees(List<Employee> employees,
                                 StringBuilder reportBody) {
        for (Employee employee : employees) {
            for (String key : report.getFields()
                                    .keySet()) {
                reportBody.append(Report.getFieldsDispatcher()
                                        .getOrDefault(key,
                                                      Report.getFieldsDispatcher()
                                                            .get("default"))
                                        .apply(employee))
                          .append(report.getDelimiter());
            }
            reportBody.append(System.lineSeparator());
        }
    }
}
