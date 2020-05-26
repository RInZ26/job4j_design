package ru.job4j.exam;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import ru.job4j.exam.Analize.*;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class AnalizeTest {
    User nastya, dima, matvey, masha, natasha, ane4ka;

    @Before
    public void setUp() {
	nastya = new User(13, "nastya");
	dima = new User(4, "dima");
	matvey = new User(2, "matvey");
	masha = new User(-9, "masha");
	natasha = new User(0, "natasha");
	ane4ka = new User(12, "anya");
    }

    @Test
    public void testAdded2Changed2Delete2() {
	List<User> previousList = Arrays.asList(nastya, dima, masha, ane4ka);
	User newNastya = new User(nastya.id, "fu");
	User newDima = new User(dima.id, "balda");
	List<User> currentList = Arrays.asList(matvey, natasha, newNastya, newDima);
	Info actual = Analize.diff(previousList, currentList);
	assertThat(actual.added, is(2));
	assertThat(actual.changed, is(2));
	assertThat(actual.deleted, is(2));
    }
    @Test
    public void testAddedSameLists() {
	List<User> previousList = Arrays.asList(nastya, dima, masha, ane4ka);
	Info actual = Analize.diff(previousList, previousList);
	assertThat(actual.added, is(0));
	assertThat(actual.changed, is(0));
	assertThat(actual.deleted, is(0));
    }
}