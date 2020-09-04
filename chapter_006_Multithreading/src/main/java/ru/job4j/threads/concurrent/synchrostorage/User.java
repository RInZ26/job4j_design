package ru.job4j.threads.concurrent.synchrostorage;

import net.jcip.annotations.NotThreadSafe;

import java.util.Objects;

/**
 * Класс - модель данных, который мы будем просто использовать в UserStorage
 * По ТЗ потокобезопасность не указана, поэтому будем считать, что она
 * отсутствует
 */
@NotThreadSafe
public class User {
    private int id;
    private int amount;

    public User(int id, int amount) {
        this.id = id;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("amount must be positive");
        }
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
