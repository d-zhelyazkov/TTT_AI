package com.xrc7331.tests;

import com.xrc7331.game.Elem;
import com.xrc7331.game.Game;
import com.xrc7331.game.TicTacToe;
import com.xrc7331.game.TicTacToePlayer;
import com.xrc7331.game.implementation.DumbPlayer;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by XRC_7331 on 6/10/2016.
 */
public class TestTicTacToeGame {

    private static final Game GAME = Game.getInstance();

    @Test
    public void test() {
        TicTacToePlayer player1 = new DumbPlayer("Bob");
        TicTacToePlayer player2 = new DumbPlayer("Alice");

        Game.Result result = GAME.play(player1, player2);
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getWinner(),player1);
        Assert.assertEquals(result.getLoser(),player2);

        TicTacToe game = result.getGame();
        Assert.assertNotNull(game);
        Assert.assertEquals(game.getWinnerElement(), Elem.X);

        System.out.println(result);
    }
}
