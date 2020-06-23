package ru.job4j.collection;

import org.junit.Before;
import org.junit.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class SimpleLinkedListTest {
    private SimpleLinkedList<String> stringSimpleLinkedList;
    private String suno;
    private String rin;
    private Iterator<String> stringIterator;

    @Before
    public void setUp() {
        stringSimpleLinkedList = new SimpleLinkedList<>();
        suno = "Sunokasuri";
        rin = "Prince";
    }

    @Test
    public void add() {
        stringSimpleLinkedList.add(suno);
        assertThat(stringSimpleLinkedList.get(0), is(suno));
    }

    @Test
    public void get() {
        stringSimpleLinkedList.add(suno);
        stringSimpleLinkedList.add(rin);
        assertThat(stringSimpleLinkedList.get(0), is(suno));
        assertThat(stringSimpleLinkedList.get(1), is(rin));
    }

    @Test(expected = ConcurrentModificationException.class)
    public void iteratorConcurrentModifException() {
        stringIterator = stringSimpleLinkedList.iterator();
        stringSimpleLinkedList.add(suno);
        stringIterator.hasNext();
    }

    @Test
    public void iteratorNormalRunWithoutExceptions() {
        stringSimpleLinkedList.add(suno);
        stringSimpleLinkedList.add(rin);
        stringIterator = stringSimpleLinkedList.iterator();
        assertTrue(stringIterator.hasNext());
        assertThat(stringIterator.next(), is(suno));
        assertThat(stringIterator.next(), is(rin));
        assertFalse(stringIterator.hasNext());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getWithWrongIndex() {
        stringSimpleLinkedList.add(suno);
        stringSimpleLinkedList.get(1);
    }
}