package ru.job4j.srp;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public class ReportEngine {
    /**
     * Dispatcher for generate
     */
    private static Map<String, Function<Employee, Object>> dispatcher = new HashMap<>() {
        {
            put("name", (em) -> em.getName());
            put("hired", (em) -> em.getHired());
            put("fired", (em) -> em.getFired());
            put("salary", (em) -> em.getSalary());
            put("default", (em) -> null);
        }
    };

    private Store store;

    public ReportEngine(Store store) {
        this.store = store;
    }

    /**
     * Generate report by fields in map because sometimes we don't
     * need all fields - using Dispatcher for this case.
     * Titles in report associate with the same Map key - field value - new
     * Title. if value = null will be used key instead.
     *
     * @return
     */
    public String generate(Map<String, String> fieldsMap,
                           Predicate<Employee> filter) {
        StringBuilder report = new StringBuilder("");
        for (Map.Entry<String, String> entry : fieldsMap.entrySet()) {
            String value = entry.getValue();
            String key = entry.getKey();
            report.append(Objects.isNull(value) ? key : value)
                  .append(" ");
            //todo fix delimetr
        }
        report.append(System.lineSeparator());
        for (Employee employee : store.findBy(filter)) {
            for (String key : fieldsMap.keySet()) {
                report.append(
                        dispatcher.getOrDefault(key, dispatcher.get("default"))
                                  .apply(employee))
                      .append("  ");
            }
            report.append(System.lineSeparator());
        }
        return report.toString();
    }

    public static void main(String[] args) {
        MemStore memStore = new MemStore();
        memStore.add(new Employee("Nastya", null, null, 100));
        memStore.add(new Employee("Katya", null, null, 300));
        memStore.add(new Employee("Matvey", null, null, 700));
        Map<String, String> map = new HashMap<>() {{
            put("name", "your majesty");
            put("salary", null);
            put("Hello", null);
        }};
        System.out.println(new ReportEngine(memStore).generate(map,
                                                               (e) -> e.getSalary()
                                                                       < 700));
    }


}

