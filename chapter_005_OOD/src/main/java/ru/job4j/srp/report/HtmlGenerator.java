package ru.job4j.srp.report;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HtmlGenerator implements ReportGenerator {
    /**
     * Сгенированный где-то отчет
     */
    private Report report;
    /**
     * Связь с бд
     */
    private Store store;

    public HtmlGenerator(Report report, Store store) {
        this.report = report;
        this.store = store;
    }

    /**
     * В таком варианте генерирует табличку в виде html
     */
    @Override
    public String generateReport() {
        StringBuilder reportBody = new StringBuilder("");
        List<Employee> employees = store.findBy(report.getFilter());
        sortByComparator(employees);
        StringBuilder meta = new StringBuilder("");
        fillMetaData(meta);
        reportBody.append(meta);
        reportBody.append(System.lineSeparator());
        StringBuilder columns = new StringBuilder("");
        fillTitles(columns);
        columns.append(System.lineSeparator());
        StringBuilder rows = new StringBuilder("");
        fillEmployees(employees, rows);
        surroundByTag(columns.append(rows), "table");
        surroundByTag(columns, "body");
        reportBody.append(columns);
        surroundByTag(reportBody, "html");
        return reportBody.toString();
    }

    private void sortByComparator(List<Employee> employees) {
        Collections.sort(employees, report.getComparator());
    }

    private void fillTitles(StringBuilder titles) {
        for (Map.Entry<String, String> entry : report.getFields()
                                                     .entrySet()) {
            titles.append(fillTitle(Optional.ofNullable(entry.getValue())
                                            .orElseGet(entry::getKey)));
            titles.append(System.lineSeparator());
        }
        erase(titles, System.lineSeparator());
        surroundByTag(titles, "tr");
    }

    private void fillEmployees(List<Employee> employees, StringBuilder body) {
        for (Employee employee : employees) {
            body.append(fillEmployee(employee));
            body.append(System.lineSeparator());
        }
        erase(body, System.lineSeparator());
    }

    /**
     * заполнение метаданных
     */
    private void fillMetaData(StringBuilder meta) {
        meta.append("<meta charset=\"utf=8\">");
        surroundByTag(meta, "head");
    }

    /**
     * Порнографический метод для удаления последнего System.lineSeparatora
     */
    private void erase(StringBuilder stringBuilder, String erased) {
        int index;
        if ((index = stringBuilder.lastIndexOf(erased)) != -1) {
            stringBuilder.delete(index, stringBuilder.length());
        }
    }

    private StringBuilder fillTitle(String title) {
        StringBuilder titleLine = new StringBuilder(title);
        surroundByTag(titleLine, "th");
        return titleLine;
    }

    private StringBuilder fillEmployee(Employee employee) {
        StringBuilder employeeRow = new StringBuilder("");
        for (String key : report.getFields()
                                .keySet()) {
            StringBuilder field = new StringBuilder(Report.getFieldsDispatcher()
                                                          .getOrDefault(key,
                                                                        Report.getFieldsDispatcher()
                                                                              .get("default"))
                                                          .apply(employee)
                                                          .toString());
            surroundByTag(field, "td");
            employeeRow.append(field);
            employeeRow.append(System.lineSeparator());
        }
        erase(employeeRow, System.lineSeparator());
        surroundByTag(employeeRow, "tr");
        return employeeRow;
    }

    private void surroundByTag(StringBuilder tagBuilder, String tag) {
        tagBuilder.insert(0, "<" + tag + ">" + System.lineSeparator());
        tagBuilder.append(System.lineSeparator() + "</" + tag + ">");
    }
}
