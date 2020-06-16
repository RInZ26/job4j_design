package ru.job4j.collection;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class FreezeStrTest {

    @Test
    public void whenEq() {
        assertThat(FreezeStr.eq("Hello", "Hlloe"), is(true));
    }

    @Test
    public void whenNotEq() {
        assertThat(FreezeStr.eq("Hello", "Halle"), is(false));
    }

    @Test
    public void whenNotMultiEq() {
        assertThat(FreezeStr.eq("heloo", "hello"), is(false));
    }

    @Test
    public void moreMultiChars() {
        assertTrue(FreezeStr.eq("aabccdfde", "bdcaafdce"));
    }

    @Test
    public void whenDifferentLengths() {
        assertFalse(FreezeStr.eq("aaaa", "aaa"));
    }
}