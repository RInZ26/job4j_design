package ru.job4j.srp.report;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static ru.job4j.srp.report.ReportFabric.Type.*;

/**
 * Что-то типа зачатков фабрики
 */
public class ReportFabric {
    /**
     * Диспатчер генераторов
     */
    private static Map<Type, Function<Holder, ReportGenerator>> genDispatcher = new HashMap<>() {
        {
            put(HTML,
                (holder) -> new HtmlGenerator(holder.report, holder.store));
            put(JSON,
                (holder) -> new JsonGenerator(holder.report, holder.store));
            put(XML, (holder) -> new XmlGenerator(holder.report, holder.store));
            put(DEFAULT,
                (holder) -> new DefaultGenerator(holder.report, holder.store));
        }
    };

    public static ReportGenerator createReport(Type type, Report report,
                                               Store store) {
        return genDispatcher.get(type)
                     .apply(new Holder(report, store));
    }

    private static class Holder {
        private Store store;
        private Report report;

        public Holder(Report report, Store store) {
            this.store = store;
            this.report = report;
        }
    }

    /**
     * Различные типы генераторов для диспатчера
     */
    public enum Type {
        HTML, DEFAULT, JSON, XML
    }
}
