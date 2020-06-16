package ru.job4j.generic;

import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class SimpleArrayTest {
    SimpleArray<Integer> integerSimpleArray;
    SimpleArray<String> stringSimpleArray;

    @Before
    public void setUp() {
        integerSimpleArray = new SimpleArray<>(5);
        stringSimpleArray = new SimpleArray<>(0);
    }

    @Test
    public void add() {
        integerSimpleArray.add(3);
        assertThat(integerSimpleArray.get(0), is(3));
    }

    @Test
    public void wrongIndexWhenAdd() {
        assertFalse(stringSimpleArray.add("blabla"));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void wrongIndexWithGet() {
        assertFalse(stringSimpleArray.remove(stringSimpleArray.size() + 1));
    }

    @Test
    public void set() {
        integerSimpleArray.add(1);
        integerSimpleArray.set(0, 2);
        assertThat(integerSimpleArray.get(0), is(2));
    }

    @Test
    public void remove() {
        integerSimpleArray.add(5);
        integerSimpleArray.remove(0);
        assertNull(integerSimpleArray.get(0));
    }

    @Test
    public void iterator() {
        integerSimpleArray.add(3);
        integerSimpleArray.add(2);
        Iterator iterator = integerSimpleArray.iterator();
        assertTrue(iterator.hasNext());
        assertThat(iterator.next(), is(3));
    }
}