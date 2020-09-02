package ru.job4j.threads.concurrent.ref;

import javax.annotation.concurrent.ThreadSafe;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@ThreadSafe
public class UserCache {
    private final ConcurrentHashMap<Integer, User> users = new ConcurrentHashMap<>();
    private final AtomicInteger id = new AtomicInteger();

    public void add(User user) {
        users.put(id.incrementAndGet(), User.of(user.getName()));
    }

    public User findById(int id) {
        return User.of(users.get(id)
                            .getName());
    }

    /**
     * Возвращаем по сути копии (но через наш кастомный вариант User.of, а не
     * clone) всех элементов на момент вызова метода, так как User не ThreadSafe
     */
    public List<User> findAll() {
        return users.values()
                    .parallelStream()
                    .map(user -> User.of(user.getName()))
                    .collect(Collectors.toList());
    }
}