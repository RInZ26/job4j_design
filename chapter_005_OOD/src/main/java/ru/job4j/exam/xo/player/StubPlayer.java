package ru.job4j.exam.xo.player;

import ru.job4j.exam.xo.game.Game;
import ru.job4j.exam.xo.map.GameCell;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class StubPlayer implements Player {
    private char label;
    private BufferedReader reader = new BufferedReader(
            new InputStreamReader(System.in));

    public StubPlayer() {
    }

    public StubPlayer(char label) {
        this.label = label;
    }

    @Override
    public char getLabel() {
        return label;
    }

    @Override
    public GameCell turn(Game game) {
        int x = -1, y = -1;
        try {
            System.out.println("type x");
            x = Integer.parseInt(reader.readLine());
            System.out.println("type y");
            y = Integer.parseInt(reader.readLine());
        } catch (Exception io) {
            io.printStackTrace();
        }
        return game.getMap()
                   .getCell(x, y);
    }

    @Override
    public String toString() {
        return Character.toString(label);
    }
}
