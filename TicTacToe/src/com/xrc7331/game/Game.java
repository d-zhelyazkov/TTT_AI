package com.xrc7331.game;

import com.xrc7331.game.implementation.TicTacToe1;
import com.xrc7331.tools.iterator.ArrayCircularIterator;

import java.util.Iterator;


/**
 * Created by XRC_7331 on 6/10/2016.
 */
public final class Game {
    private static final Game ourInstance = new Game();

    public static Game getInstance() {
        return ourInstance;
    }

    private Game() {
    }

    /**
     * Plays a tic tac toe game with two players
     * @param player1 the player, that is playing with the {@link Elem#X} element.
     * @param player2 the player, that is playing with the {@link Elem#O} element.
     * @return the game result
     */
    public Result play(TicTacToePlayer player1, TicTacToePlayer player2) {
        final TicTacToe ticTacToeGame = new TicTacToe1();
        final Iterator<TicTacToePlayer> players = new ArrayCircularIterator(player1,player2);

        while (!ticTacToeGame.isFinished()) {
            final TicTacToePlayer nextPlayer = players.next();
            nextPlayer.makeATurn(ticTacToeGame);
        }

        Result result = new Result();
        result.game = ticTacToeGame;
        switch (ticTacToeGame.getWinnerElement()) {
            case X:
                result.winner = player1;
                result.loser = player2;
                break;
            case O:
                result.winner = player2;
                result.loser = player1;
                break;
        }

        return result;
    }

    public class Result {
        TicTacToePlayer winner;
        TicTacToePlayer loser;
        TicTacToe game;

        Result() {
        }

        public TicTacToe getGame() {
            return game;
        }

        public TicTacToePlayer getWinner() {
            return winner;
        }

        public TicTacToePlayer getLoser() {
            return loser;
        }

        public boolean isDraw() {
            return (game.getWinnerElement() == Elem.BLANK);
        }

        @Override
        public String toString() {
            return "Result{" +
                    "\nwinner=" + winner +
                    "\nloser=" + loser +
                    "\n" + game +
                    "}";
        }
    }
}



