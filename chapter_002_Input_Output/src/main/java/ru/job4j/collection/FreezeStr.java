package ru.job4j.collection;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс для проверок строк
 */
public class FreezeStr {
    /**
     * Записываем в hashmap по ключу символа количество повторений в одной строке
     * Во втором цикле в зависимости от совпадения снижаем количество повторений и по достижению ноля - ремуваем
     * Если размер хэшмэпа == 0 => строки равны
     */
    public static boolean eq(String left, String right) {
	Map<Character, Integer> mapOfCountOfCharactersInLeftString = new HashMap<>();
	for (Character character : left.toCharArray()) {
	    if (mapOfCountOfCharactersInLeftString.containsKey(character)) {
		mapOfCountOfCharactersInLeftString.put(character, mapOfCountOfCharactersInLeftString.get(character) + 1);
	    } else {
		mapOfCountOfCharactersInLeftString.put(character, 1);
	    }
	}
	for (Character character : right.toCharArray()) {
	    if (mapOfCountOfCharactersInLeftString.containsKey(character)) {
		int size;
		mapOfCountOfCharactersInLeftString.put(character, size = mapOfCountOfCharactersInLeftString.get(character) - 1);
		if (size == 0) {
		    mapOfCountOfCharactersInLeftString.remove(character);
		}
	    }
	}
	return mapOfCountOfCharactersInLeftString.size() == 0;
    }
}
