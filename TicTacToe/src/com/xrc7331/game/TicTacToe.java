package com.xrc7331.game;

import com.xrc7331.tools.Coordinate;
import com.xrc7331.tools.observer.Observable;

/**
 * Created by XRC_7331 on 6/10/2016.
 */
public interface TicTacToe extends Observable<TicTacToe> {

    short BOARD_SIZE = 3;

    boolean makeATurn(Coordinate coordinate) throws CellTakenException, GameFinishedException;

    Elem[][] getBoardState();

    Elem getWinnerElement();

    Elem getNextTurn();

    boolean isFinished();
}
