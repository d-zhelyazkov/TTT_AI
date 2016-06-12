package com.xrc7331.game.implementation;

import com.xrc7331.game.*;
import com.xrc7331.tools.Coordinate;

/**
 * Created by XRC_7331 on 6/10/2016.
 */
public class DumbPlayer implements TicTacToePlayer {

    private String name;

    public DumbPlayer(String name) {
        this.name = name;
    }

    @Override
    public void makeATurn(TicTacToe ticTacToeGame) {
        final Elem[][] board = ticTacToeGame.getBoardState();
        for (short y = 0; y <TicTacToe.BOARD_SIZE; y++) {
            for (short x = 0; x < TicTacToe.BOARD_SIZE; x++) {
                if (board[y][x] == Elem.BLANK) {
                    try {
                        ticTacToeGame.makeATurn(new Coordinate(x, y));
                        return;
                    } catch (CellTakenException e) {
                        e.printStackTrace();
                    } catch (GameFinishedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        return "DumbPlayer{" +
                "name='" + name + '\'' +
                '}';
    }
}
