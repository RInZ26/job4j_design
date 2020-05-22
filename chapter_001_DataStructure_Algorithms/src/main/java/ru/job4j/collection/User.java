package ru.job4j.collection;

import java.util.*;

/**
 * Класс - модель данных
 */
public class User {
    private String name;
    private int age;
    private Calendar birthday;
    public String iq;

    /**
     * Понтовый конструктор через сеттеры
     *
     * @param name     ~
     * @param age      ~
     * @param birthday ~
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

    @Override
    public int hashCode() {
	return Objects.hash(name, age, birthday);
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
	return age == user.age
		&& Objects.equals(name, user.name)
		&& Objects.equals(birthday, user.birthday);
    }


}

class Test {
    public static void main(String[] args) {
	Calendar omg = new GregorianCalendar();
	User loh1 = new User("Matvey", 10, omg);
	loh1.iq = "200";
	User loh2 = new User("Matvey", 10, omg);
	loh2.iq = "322";
	HashSet<User> map = new HashSet<User>();
	map.add(loh1);
	map.add(loh2);
	map.forEach(o -> System.out.println(o.iq));
    }
}