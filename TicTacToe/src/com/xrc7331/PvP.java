package com.xrc7331;

import com.xrc7331.game.Game;

import javax.swing.*;

public class PvP {

    private static final Game GAME = Game.getInstance();

    public static void main(String[] args) throws InterruptedException {
        final SwingPlayer player1 = new SwingPlayer("Bob");
        player1.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        final SwingPlayer player2 = new SwingPlayer("Alice");
        player2.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        while (true) {
            play(player1, player2);
            play(player2, player1);

        }
    }

    private static void play(SwingPlayer player1, SwingPlayer player2) throws InterruptedException {
        final Game.Result result = GAME.play(player1, player2);
        System.out.println(result);
        Thread.sleep(5000);
    }
}
