package ru.job4j.srp.report;

import java.util.List;
import java.util.function.Predicate;

//todo разобраться с дженериками потом

/**
 * Общий класс для генераторов, идея которого в том, чтобы передать
 * наследникам НЕПУБЛИЧНЫЕ методы
 */
public abstract class ReportGenerator {
    /**
     * Главный метод, создающий отчет
     */
    public abstract String generateReport();

    /**
     * Сортировска данных по компаратору
     *
     * @param list
     */
    protected abstract void sortByComparator(List<Employee> list);

    /**
     * Получение данных из Store с фильтром
     */
    protected List<Employee> getData(Store store, Predicate<Employee> filter) {
        return store.findBy(filter);
    }

    /**
     * Заполнение заголовков
     */
    protected abstract void fillTitles(StringBuilder reportBody);

    /**
     * Заполнение самих пользователей
     */
    protected abstract void fillEmployees(List<Employee> employees,
                                          StringBuilder reportBody);
}
