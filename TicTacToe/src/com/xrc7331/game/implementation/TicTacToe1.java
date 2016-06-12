package com.xrc7331.game.implementation;

import com.xrc7331.game.*;
import com.xrc7331.tools.Coordinate;
import com.xrc7331.tools.iterator.ArrayCircularIterator;
import com.xrc7331.tools.observer.*;
import com.xrc7331.tools.observer.Observer;

import java.util.*;

/**
 * Created by XRC_7331 on 6/10/2016.
 */
public class TicTacToe1 implements TicTacToe {

    private static final short TURNS = 9;
    private static final Cell[][] CELLS = {
            {new Cell(0, 0), new Cell(1, 0), new Cell(2, 0)},
            {new Cell(0, 1), new Cell(1, 1), new Cell(2, 1)},
            {new Cell(0, 2), new Cell(1, 2), new Cell(2, 2)}
    };

    static {
        //horizontal lines
        new Line(CELLS[0][0], CELLS[0][1], CELLS[0][2]);
        new Line(CELLS[1][0], CELLS[1][1], CELLS[1][2]);
        new Line(CELLS[2][0], CELLS[2][1], CELLS[2][2]);

        //vertical lines
        new Line(CELLS[0][0], CELLS[1][0], CELLS[2][0]);
        new Line(CELLS[0][1], CELLS[1][1], CELLS[2][1]);
        new Line(CELLS[0][2], CELLS[1][2], CELLS[2][2]);

        //diagonals
        new Line(CELLS[0][0], CELLS[1][1], CELLS[2][2]);
        new Line(CELLS[0][2], CELLS[1][1], CELLS[2][0]);
    }

    private final Elem[][] board = {
            {Elem.BLANK, Elem.BLANK, Elem.BLANK},
            {Elem.BLANK, Elem.BLANK, Elem.BLANK},
            {Elem.BLANK, Elem.BLANK, Elem.BLANK}
    };
    private short turnsMade = 0;
    private Elem winner = Elem.BLANK;
    private final ArrayCircularIterator<Elem> turns = new ArrayCircularIterator<>(Elem.X, Elem.O);
    private Collection<Observer<TicTacToe>> observers = new HashSet<>();

    @Override
    public boolean makeATurn(Coordinate coordinate) throws CellTakenException, GameFinishedException {
        if (isFinished())
            throw new GameFinishedException();

        short x = (short) coordinate.getX();
        short y = (short) coordinate.getY();
        if (board[y][x] != Elem.BLANK)
            throw new CellTakenException();

        Elem element = turns.next();
        board[y][x] = element;
        turnsMade++;

        boolean win = checkLines(CELLS[y][x].lines, element);
        if (win)
            winner = element;

        this.notifyObservers();
        return win;
    }

    @Override
    public Elem[][] getBoardState() {
        return board.clone();
    }

    @Override
    public Elem getWinnerElement() {
        return winner;
    }

    @Override
    public Elem getNextTurn() {
        return turns.getCurrent();
    }

    @Override
    public boolean isFinished() {
        return ((TURNS <= turnsMade) || (winner != Elem.BLANK));
    }

    /**
     * Checks if at least one line is filled with the element
     * @param lines collection of lines that are being checked
     * @param element the element that is being searched for
     * @return true if there is a line that is filled with the element
     */
    private boolean checkLines(Collection<Line> lines, Elem element) {
        for (Line line : lines) {
            short matchedCells = 0;
            for (Cell cell : line.cells) {
                if (board[cell.getY()][cell.getX()] != element)
                    break;

                matchedCells++;
            }
            if (matchedCells == BOARD_SIZE)
                return true;
        }

        return false;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (short y = 0; y < BOARD_SIZE; y++) {
            for (short x = 0; x < BOARD_SIZE; x++)
                builder.append(board[y][x]);

            builder.append("\n");
        }

        return builder.toString();
    }

    @Override
    public void addObserver(Observer<TicTacToe> observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer<TicTacToe> observer : observers)
            observer.updated(this);
    }
}

class Line {
    Collection<Cell> cells = new HashSet<>();

    Line(Collection<Cell> cells) {
        this.cells.addAll(cells);

        for (Cell cell : this.cells) {
            cell.lines.add(this);
        }
    }

    Line(Cell... cells) {
        this(Arrays.asList(cells));
    }
}


class Cell extends Coordinate {

    Collection<Line> lines = new HashSet<>();

    public Cell(int x, int y) {
        super(x, y);
    }
}