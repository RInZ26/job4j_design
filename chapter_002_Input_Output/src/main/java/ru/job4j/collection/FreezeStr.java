package ru.job4j.collection;

/**
 * Класс для проверок строк
 */
public class FreezeStr {
    public static boolean eq(String left, String right) {
	if (left.length() != right.length()) {
	    return false;
	}
	StringBuilder builderForLeftString = new StringBuilder(left);
	for (int c = 0; c < builderForLeftString.length(); c++) {
	    if (builderForLeftString.indexOf(String.valueOf(right.charAt(c))) == -1) {
		return false;
	    }
	    builderForLeftString.deleteCharAt(c);
	}
	return true;
    }
}
