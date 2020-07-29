package ru.job4j.kiss;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class MaxMinTest {

    @Test
    public void checkIntMaxMin() {
        MaxMin maxMin = new MaxMin();
        List<Integer> values = Arrays.asList(3, -1);
        assertThat(maxMin.<Integer>max(values, Integer::compareTo), is(3));
        assertThat(maxMin.<Integer>min(values, Integer::compareTo), is(-1));
    }

    @Test
    public void checkStringMaxMin() {
        MaxMin maxMin = new MaxMin();
        List<String> values = Arrays.asList("b", "a", "c");
        assertThat(maxMin.<String>max(values, String::compareTo), is("c"));
        assertThat(maxMin.<String>min(values, String::compareTo), is("a"));
    }

    @Test
    public void whenListIsEmptyThenNull() {
        MaxMin maxMin = new MaxMin();
        assertNull(maxMin.<Integer>max(Collections.emptyList(),
                                       Integer::compareTo));
    }
}