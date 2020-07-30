package ru.job4j.tdd;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class CinemaTest {
    @Ignore
    @Test
    public void buy() {
        Account account = new AccountCinema();
        Cinema cinema = new Cinema3D();
        Calendar date = Calendar.getInstance();
        date.set(2020, 10, 10, 23, 00);
        Ticket ticket = cinema.buy(account, 1, 1, date);
        assertThat(ticket, is(new Ticket3D()));
    }

    @Ignore
    @Test
    public void find() {
        Cinema cinema = new Cinema3D();
        cinema.add(new Session3D());
        List<Session> sessions = cinema.find(session -> true);
        assertThat(sessions, is(Arrays.asList(new Session3D())));
    }

    @Ignore
    @Test(expected = IllegalArgumentException.class)
    public void whenCinemaBuyWrongAccountInputThenException() {
        Cinema cinema = new Cinema3D();
        Ticket ticket = cinema.buy(null, 1, 1, Calendar.getInstance());
    }

    @Ignore
    @Test(expected = IllegalArgumentException.class)
    public void whenCinemaBuyWrongDateInputThenException() {
        Cinema cinema = new Cinema3D();
        Ticket ticket = cinema.buy(new AccountCinema(), 1, 1, null);
    }
}