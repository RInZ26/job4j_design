package ru.job4j.template;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class GeneratorTest {
    @Test
    public void whenConditionsAreOk() {
        Generator generator = new SimpleGenerator();
        Map<String, String> map = new HashMap<>();
        map.put("name", "Batman");
        map.put("subject", "Detonator");
        assertThat(
                generator.produce("I am a ${name}, Where is ${subject}?", map),
                is("I am a Batman, Where is Detonator?"));
    }

    @Test(expected = BatException.class)
    public void whenKeyDoesntExist() {
        Generator generator = new SimpleGenerator();
        Map<String, String> map = new HashMap<>();
        map.put("name", "Batman");
        generator.produce(
                "I am a ${name}, Where is ${subject} and evil ${enemy}?", map);
    }

    @Test(expected = BatException.class)
    public void whenKeyWasUnused() {
        Generator generator = new SimpleGenerator();
        Map<String, String> map = new HashMap<>();
        map.put("name", "Batman");
        generator.produce(
                "I am a Petr Arsentev.", map);
    }
}

class BatException extends Exception {
}
