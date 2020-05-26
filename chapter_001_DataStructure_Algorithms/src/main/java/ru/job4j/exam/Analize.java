package ru.job4j.exam;

import java.util.*;
import java.util.stream.Collectors;

public class Analize {

    /**
     * План: Отсортировать элементы через стрим и сделать заодно КОПИИ коллекций.
     * Далее сравниваем через простенький while элементы по id, увеличивая счетчики, там где надо
     * Вероятнее всего будет ситуация, когда в одной из коллекций остались недопроверенные элементы, либо в старой, либо в новой из-за удалений и вставок
     * Тогда отрабатывает последний if, следуя простой логике - если раньше закончился currentList, значит разница это количество удаленных элементов,
     * если раньше закончился previousList, значит - количество добавленных
     * Великолепный план, Уолтер!...
     */
    public static Info diff(List<User> previous, List<User> current) {
	Info infoAboutDifferences = new Info();
	var sortedPrevious = previous.stream().sorted().collect(Collectors.toList());
	var sortedCurrent = current.stream().sorted().collect(Collectors.toList());
	int counterForPreviousUser = 0, counterForCurrentUser = 0;
	int minLengthOfArrays = Math.min(sortedPrevious.size(), sortedCurrent.size());
	int idPreviousUser, idCurrentUser;
	while (counterForCurrentUser < minLengthOfArrays || counterForPreviousUser < minLengthOfArrays) {
	    idPreviousUser = sortedPrevious.get(counterForPreviousUser).id;
	    idCurrentUser = sortedCurrent.get(counterForCurrentUser).id;
	    if (idPreviousUser < idCurrentUser) {
		infoAboutDifferences.deleted++;
		counterForPreviousUser++;
	    } else if (idPreviousUser > idCurrentUser) {
		infoAboutDifferences.added++;
		counterForCurrentUser++;
	    } else {
		if (!sortedPrevious.get(counterForPreviousUser).name.equals(sortedCurrent.get(counterForCurrentUser).name)) {
		    infoAboutDifferences.changed++;
		}
		counterForCurrentUser++;
		counterForPreviousUser++;
	    }
	}
	if (counterForPreviousUser < sortedPrevious.size()) {
	    infoAboutDifferences.deleted += sortedPrevious.size() - counterForCurrentUser;
	} else if (counterForCurrentUser < sortedCurrent.size()) {
	    infoAboutDifferences.added += sortedCurrent.size() - counterForCurrentUser;
	}
	return infoAboutDifferences;
    }

    /**
     * Подписали на comparable для сортировки по id
     */
    public static class User implements Comparable<User> {
	int id;
	String name;

	public User(int id, String name) {
	    this.id = id;
	    this.name = name;
	}

	@Override
	public int compareTo(User o) {
	    return Integer.compare(this.id, o.id);
	}
    }

    public static class Info {
	int added;
	int changed;
	int deleted;

	Info() {
	}

	Info(int added, int changed, int deleted) {
	    this.added = added;
	    this.changed = changed;
	    this.deleted = deleted;
	}

	@Override
	public boolean equals(Object o) {
	    if (this == o) {
		return true;
	    }
	    if (o == null || getClass() != o.getClass()) {
		return false;
	    }
	    Info info = (Info) o;
	    return added == info.added
		    && changed == info.changed
		    && deleted == info.deleted;
	}

	@Override
	public int hashCode() {
	    return Objects.hash(added, changed, deleted);
	}
    }

}
