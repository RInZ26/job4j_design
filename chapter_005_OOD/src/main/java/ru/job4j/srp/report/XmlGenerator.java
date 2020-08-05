package ru.job4j.srp.report;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * генератор в xml для Report.java
 */
public class XmlGenerator implements ReportGenerator {
    /**
     * логгер
     */
    private static final Logger LOG = LoggerFactory.getLogger(
            XmlGenerator.class.getName());
    /**
     * Сгенированный где-то отчет
     */
    private Report report;
    /**
     * Связь с бд
     */
    private Store store;

    /**
     * Тэги, используемые для форматирования xml, в теории их нужно
     * передавать также через какой-нибудь xmlgenerator билдер, но, пока что
     * хватит и сета обычного
     */
    private Map<String, String> xmlTags = new HashMap<>() {
        {
            put("name", "name");
            put("salary", "salary");
            put("employee", "employee");
            put("employees", "employeeList");
        }
    };

    public XmlGenerator(Report report, Store store) {
        this.report = report;
        this.store = store;
    }

    public Map<String, String> getXmlTags() {
        return xmlTags;
    }

    public void setXmlTags(Map<String, String> xmlTags) {
        this.xmlTags = xmlTags;
    }

    @Override
    public String generateReport() {
        StringBuilder reportBody = new StringBuilder("");
        List<Employee> employees = store.findBy(report.getFilter());
        sortByComparator(employees);
        fillEmployees(employees, reportBody);
        StringBuilder meta = new StringBuilder("");
        fillMetaData(meta);
        reportBody.insert(0, meta.append(System.lineSeparator()));
        return reportBody.toString();
    }

    private void sortByComparator(List<Employee> employees) {
        Collections.sort(employees, report.getComparator());
    }

    private void fillEmployees(List<Employee> employees, StringBuilder body) {
        for (Employee employee : employees) {
            body.append(fillEmployee(employee));
            body.append(System.lineSeparator());
        }
        erase(body, System.lineSeparator());
        surroundByTag(body, safeGetTag("employees"));
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

            surroundByTag(field, safeGetTag(key));
            employeeRow.append(field);
            employeeRow.append(System.lineSeparator());
        }
        erase(employeeRow, System.lineSeparator());
        surroundByTag(employeeRow, safeGetTag("employee"));
        return employeeRow;
    }

    /**
     * В xml теги гораздо критичнее чем в Html, поэтому, если какого-то тега не
     * будет -
     * такая xml автматически неликвидна, но Exception слишком круто для
     * такого, поэтому можно обойтись логгером и возвращать неликвидный tag
     *
     * @param key ключ, который мы ищем в мапе тэгоы
     *
     * @return значение/фикитивный тэг
     */
    private String safeGetTag(String key) {
        String value = xmlTags.get(key);
        if (Objects.isNull(value)) {
            LOG.error("none-existent tag for class " + "employee {}", key);
            return "ERROR_TAG";
        } else {
            return value;
        }
    }

    /**
     * Обводочка тэгом
     *
     * @param tagBuilder для удобства
     * @param tag        тэг без скобок
     */
    private void surroundByTag(StringBuilder tagBuilder, String tag) {
        tagBuilder.insert(0, "<" + tag + ">" + System.lineSeparator());
        tagBuilder.append(System.lineSeparator() + "</" + tag + ">");
    }

    /**
     * заполнение метаданных
     */
    private void fillMetaData(StringBuilder meta) {
        meta.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
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
}
