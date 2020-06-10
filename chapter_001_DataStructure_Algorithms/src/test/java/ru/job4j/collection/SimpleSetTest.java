package ru.job4j.collection;

import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

public class SimpleSetTest {
    SimpleSet<String> stringSimpleSet;
    String suno = "TL";
    String rin = "prince";

    @Before
    public void setUp() {
	stringSimpleSet = new SimpleSet<>();
    }

    @Test
    public void addAndIter() {
	stringSimpleSet.add(suno);
	Iterator<String> tempIterator = stringSimpleSet.iterator();
	assertThat(tempIterator.next(), is(suno));
	assertFalse(tempIterator.hasNext());
    }

    @Test
    public void addCopies() {
	stringSimpleSet.add(suno);
	stringSimpleSet.add(rin);
	stringSimpleSet.add(suno);
	Iterator<String> tempIterator = stringSimpleSet.iterator();
	assertThat(tempIterator.next(), is(suno));
	assertThat(tempIterator.next(), is(rin));
	assertFalse(tempIterator.hasNext());
    }
}