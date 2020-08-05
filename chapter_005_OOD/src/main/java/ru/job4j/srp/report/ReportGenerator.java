package ru.job4j.srp.report;

//todo разобраться с дженериками потом

/**
 * Общий интерфейс для генераторов, идея которого в том, чтобы передать
 * наследникам НЕПУБЛИЧНЫЕ методы
 */
public interface ReportGenerator {
    /**
     * Главный метод, создающий отчет
     */
    String generateReport();
}
