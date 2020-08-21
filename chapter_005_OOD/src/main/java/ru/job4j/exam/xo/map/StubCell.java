package ru.job4j.exam.xo.map;

import ru.job4j.exam.xo.player.Player;

import java.util.Objects;

public class StubCell implements GameCell {
    private Player player;
    private int x;
    private int y;

    public StubCell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public boolean isFree() {
        return Objects.isNull(player);
    }

    @Override
    public int[] getCoordinates() {
        return new int[]{x, y};
    }

    @Override
    public boolean setPlayer(Player player) {
        boolean result = isFree();
        if (result) {
            this.player = player;
        }
        return result;
    }

    @Override
    public String toString() {
        return Objects.isNull(player.toString()) ? null : player.toString();
    }
}
