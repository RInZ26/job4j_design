package ru.job4j.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * Класс для работы с логами и записи в файлы
 */
public class Analizy {
    /**
     * Максимально неэлегатная реализация поиска групп по условию до маркера остановки включительно в группу.
     * Сначала смотрим, является ли запись ликвидной строкой лога - а не комментарием, пустой или ещё чем
     * Далее смотрим, что это за запись лога. С ошибкой или без?
     * Если с ошибкой, тогда - отслеживаем ли мы уже какой-то интервал ? идём дальше : метим старт
     * Если запись без ошибки - отслеживаем ли мы уже какой-то интервал? идём дальше : метим финиш
     * Учитывается возможность, что ошибка началась, но не закончилась
     * @param source откуда берем лог
     * @param target куда отправляем результат
     * @return true - чтение создание списка прошло успешно (НО НЕ СОХРАНЕНИЕ) false - фиаско
     */
    public static boolean unavailable(String source, String target) {
	List<Holder> listOfPeriodsOfError = new ArrayList<>();
	try (BufferedReader in = new BufferedReader(new FileReader(source))) {
	    Predicate<String> isError = (o -> o.equals("400") || o.equals("500"));
	    Pattern logStringPattern = Pattern.compile("\\d{3}\\s\\d+"); // именно 3 циферки + пробел + что-то похожее на дату
	    String inNext; // проверка на EOF
	    Holder periodWhenErrorWas = Holder.EMPTY_HOLDER;
	    while ((inNext = in.readLine()) != null) {
		if (logStringPattern.matcher(inNext).find()) { //проверка что строка ликвидная, а не мусор
		    if (isError.test(Holder.parseCode(inNext))) { //проверка что запись является записью с ошибкой
			if (periodWhenErrorWas.equals(Holder.EMPTY_HOLDER)) { //если поймали начало периода
			    periodWhenErrorWas = new Holder();
			    periodWhenErrorWas.startOfTrackingPeriod = inNext;
			}
		    } else if (!periodWhenErrorWas.equals(Holder.EMPTY_HOLDER)) { // если период закончился, но начинался
			periodWhenErrorWas.finishOfTrackingPeriod = inNext;
			listOfPeriodsOfError.add(periodWhenErrorWas);
			periodWhenErrorWas = Holder.EMPTY_HOLDER;
		    }
		}
	    }
	    if (!periodWhenErrorWas.equals(Holder.EMPTY_HOLDER)) { // ловим неполный случай (началось - не закончилось)
		listOfPeriodsOfError.add(periodWhenErrorWas);
	    }
	    saveToFile(listOfPeriodsOfError, target);
	} catch (Exception e) {
	    e.printStackTrace();
	    return false;
	}
	return true;
    }

    /**
     * Да, у меня уже есть такой же метод в LogFilter, но можно понабивать руки!
     *
     * @param listOfObjectsForSavingInFile что сохраняем
     * @param target                   куда ~
     * @param <K>                          для красоты
     */
    public static <K> boolean saveToFile(List<K> listOfObjectsForSavingInFile, String target) {
	try (PrintWriter out = new PrintWriter(new FileWriter(target))) {
	    listOfObjectsForSavingInFile.forEach(o -> out.write(o.toString() + "\r"));
	} catch (Exception e) {
	    e.printStackTrace();
	    return false;
	}
	return true;
    }

    /**
     * Класс-структура для хранения интервалов
     */
    private static class Holder {
	private static final Holder EMPTY_HOLDER = new Holder("Infinity", "Infinity");
	String startOfTrackingPeriod;
	String finishOfTrackingPeriod;

	private Holder(String startOfTrackingPeriod, String finishOfTrackingPeriod) {
	    this.startOfTrackingPeriod = startOfTrackingPeriod;
	    this.finishOfTrackingPeriod = finishOfTrackingPeriod;
	}

	private Holder() {
	    this.startOfTrackingPeriod = "";
	    this.finishOfTrackingPeriod = "neverEnd";
	}

	/**
	 * Парсим запись, чтобы извлечь code ошибки
	 */
	private static String parseCode(String line) {
	    return line.split(" ")[0];
	}

	@Override
	public boolean equals(Object o) {
	    if (this == o) {
		return true;
	    }
	    if (o == null || getClass() != o.getClass()) {
		return false;
	    }
	    Holder holder = (Holder) o;
	    return Objects.equals(startOfTrackingPeriod, holder.startOfTrackingPeriod)
		    && Objects.equals(finishOfTrackingPeriod, holder.finishOfTrackingPeriod);
	}

	@Override
	public int hashCode() {
	    return Objects.hash(startOfTrackingPeriod, finishOfTrackingPeriod);
	}

	@Override
	public String toString() {
	    return String.format("%s %s", startOfTrackingPeriod, finishOfTrackingPeriod);
	}
    }
}