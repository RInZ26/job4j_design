package ru.job4j.srp.report;

import java.util.Collections;
import java.util.List;

public class JsonGenerator implements ReportGenerator {
    /**
     * Сгенированный где-то отчет
     */
    private Report report;
    /**
     * Связь с бд
     */
    private Store store;

    public JsonGenerator(Report report, Store store) {
        this.report = report;
        this.store = store;
    }

    @Override
    public String generateReport() {
        StringBuilder reportBody = new StringBuilder("{").append(
                System.lineSeparator());
        List<Employee> employeeList = store.findBy(report.getFilter());
        sortByComparator(employeeList);
        reportBody.append(fillEmployees(employeeList))
                  .append(System.lineSeparator())
                  .append("}");
        return reportBody.toString();
    }

    private void sortByComparator(List<Employee> employees) {
        Collections.sort(employees, report.getComparator());
    }

    private StringBuilder fillEmployees(List<Employee> employees) {
        StringBuilder body = new StringBuilder("");
        body.append(shielding("employees"))
            .append(" : ")
            .append("[")
            .append(System.lineSeparator());
        for (Employee employee : employees) {
            body.append(fillEmployee(employee));
            body.append(",");
            body.append(System.lineSeparator());
        }
        erase(body, ",");
        body.append("]");
        return body;
    }

    private StringBuilder fillEmployee(Employee employee) {
        StringBuilder emStrb = new StringBuilder("{").append(
                System.lineSeparator());
        for (String key : report.getFields()
                                .keySet()) {
            emStrb.append(shielding(key))
                  .append(" : ");
            emStrb.append(shielding(Report.getFieldsDispatcher()
                                          .get(key)
                                          .apply(employee)));
            emStrb.append(",")
                  .append(System.lineSeparator());
        }
        erase(emStrb, ",");
        emStrb.append("}");
        return emStrb;
    }

    /**
     * Порнографический метод перекочевавший из html для удаления последнего
     * System .lineSeparatora и запятой. Да, можно обойтись и без него, если
     * проходить коллекцию итератором и перед последним элементом это не
     * ставить, но это долго
     */
    private void erase(StringBuilder stringBuilder, String erased) {
        int index;
        if ((index = stringBuilder.lastIndexOf(erased)) != -1) {
            stringBuilder.delete(index,
                                 stringBuilder.length() - erased.length() - 1);
        }
    }

    /**
     * Экранирование/обёртка для поля в зависимости от того какое оно
     * Например, для int кавычки не нужны, для названия полей и всего
     * остального - нужны
     */
    private String shielding(Object field) {
        if (field instanceof CharSequence) {
            StringBuilder strB = new StringBuilder((CharSequence) field);
            return strB.insert(0, "\"")
                       .append("\"")
                       .toString();
        } else {
            return field.toString();
        }
    }
}
