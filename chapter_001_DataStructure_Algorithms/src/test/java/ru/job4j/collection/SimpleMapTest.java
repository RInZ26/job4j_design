package ru.job4j.collection;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class SimpleMapTest {
    SimpleMap<String, String> stringStringSimpleMap;
    String a, b, c, d;

    @Before
    public void setUp() throws Exception {
	stringStringSimpleMap = new SimpleMap<>();
	a = "matvey";
	b = "nastya";
	c = "suno";
	d = "prince";
    }

    @Test
    public void whenPutAndGet() {
	stringStringSimpleMap.insert(a, c);
	stringStringSimpleMap.insert(b, d);
	assertThat(stringStringSimpleMap.get(a), is(c));
    }
}