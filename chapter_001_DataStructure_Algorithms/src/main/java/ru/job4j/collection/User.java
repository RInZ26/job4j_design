package ru.job4j.collection;

import java.util.Calendar;

/**
 Класс - модель данных
 */
public class User {
    public String iq;
    private String name;
    private int age;
    private Calendar birthday;

    /**
     Понтовый конструктор через сеттеры

     @param name
     ~
     @param age
     ~
     @param birthday
     ~
     */
    public User(String name, int age, Calendar birthday) {
        setName(name);
        setAge(age);
        setBirthday(birthday);

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Calendar getBirthday() {
        return birthday;
    }

    public void setBirthday(Calendar birthday) {
        this.birthday = birthday;
    }
}
