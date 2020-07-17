package ru.job4j.gc;

public interface Cache<K, V> {
    public V get(K key);
}