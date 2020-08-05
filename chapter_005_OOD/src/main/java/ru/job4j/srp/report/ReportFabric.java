package ru.job4j.srp.report;

/**
 * Что-то типа зачатков фабрики
 */
public class ReportFabric {
    public static ReportGenerator createReport(Type type, Report report,
                                               Store store) {
        switch (type) {
            case HTML:
                return new HtmlGenerator(report, store);
            case JSON:
                return new JsonGenerator(report, store);
            case XML:
                return new XmlGenerator(report, store);
            case DEFAULT:
            default:
                return new DefaultGenerator(report, store);
        }
    }

    public enum Type {
        HTML, DEFAULT, JSON, XML
    }
}
