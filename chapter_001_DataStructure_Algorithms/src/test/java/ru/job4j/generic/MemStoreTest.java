package ru.job4j.generic;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class MemStoreTest {
    MemStore<User> userMemStore;
    User nastya, suno;

    @Before
    public void setUp() throws Exception {
	userMemStore = new MemStore<>();
	nastya = new User("Nastya");
	suno = new User("Liza");
    }

    @Test
    public void add() {
	userMemStore.add(nastya);
	assertThat(nastya, is(userMemStore.findById(nastya.getId())));
    }

    @Test
    public void replace() {
	userMemStore.add(nastya);
	userMemStore.replace(nastya.getId(), suno);
	assertNull(userMemStore.findById(nastya.getId()));
	assertThat(userMemStore.findById(suno.getId()), is(suno));
    }

    @Test
    public void delete() {
	userMemStore.add(nastya);
	userMemStore.delete(nastya.getId());
	assertNull(userMemStore.findById(nastya.getId()));
    }

    @Test
    public void findById() {
	userMemStore.add(nastya);
	assertThat(userMemStore.findById(nastya.getId()), is(nastya));
    }
}