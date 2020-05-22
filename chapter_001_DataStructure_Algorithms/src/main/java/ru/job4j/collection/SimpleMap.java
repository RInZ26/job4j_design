package ru.job4j.collection;

import java.util.Objects;

/**
 * Упрощенная Map
 */
public class SimpleMap<K, V> {
    /**
     * Храналище данных
     */
    private Object[] data;

    public SimpleMap() {
	data = new Object[1 << 15];
    }

    boolean insert(K key, V value) {
	int hashOfKey = superHash(key);
	if (hashOfKey >= data.length) {
	    resizeOfData(hashOfKey);
	}
	if (data[hashOfKey] != null) {
	    return false;
	}
	data[hashOfKey] = value;
	return true;
    }

    private void resizeOfData(int problemSize) {
	Object[] newData = new Object[problemSize + 1];
	System.arraycopy(data, 0, newData, 0, data.length);
	data = newData;
    }

    V get(K key) {
	int hashOfKey = superHash(key);
	if (hashOfKey < data.length) {
	    return (V) data[hashOfKey];
	}
	return null;
    }

    boolean delete(K key) {
	int hashOfKey = superHash(key);
	if (hashOfKey < data.length) {
	    data[hashOfKey] = null;
	    return true;
	}
	return false;
    }

    /**
     * Максимально убогая реализация hash, чтобы что-то хранить в Array
     *
     * @param o
     * @return
     */
    private int superHash(Object o) {
	return Math.abs(o.hashCode()) >> 2;
    }
}
