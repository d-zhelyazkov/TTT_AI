package com.xrc7331;

import com.xrc7331.game.*;
import com.xrc7331.tools.Coordinate;
import com.xrc7331.tools.observer.Observer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Semaphore;

/**
 * Created by XRC_7331 on 6/10/2016.
 */
public class SwingPlayer extends JFrame implements
        TicTacToePlayer,
        ActionListener,
        Observer<TicTacToe> {
    private static final String WAITING = "Waiting";

    private JButton[][] buttons = new JButton[TicTacToe.BOARD_SIZE][TicTacToe.BOARD_SIZE];
    private JLabel messageLabel = new JLabel(WAITING);
    private TicTacToe currentGame;
    private Elem currentElem;

    private boolean waitingForInput = false;
    private Semaphore coordinateLock = new Semaphore(0);
    private Coordinate selected = null;

    public SwingPlayer(final String title) {
        super(title);
        super.setLayout(new BorderLayout());
        super.add(messageLabel, BorderLayout.NORTH);
        JPanel panel = new JPanel(new GridLayout(TicTacToe.BOARD_SIZE, TicTacToe.BOARD_SIZE));
        for (short y = 0; y < TicTacToe.BOARD_SIZE; y++) {
            for (short x = 0; x < TicTacToe.BOARD_SIZE; x++) {
                JButton button = new ButtonWithCoordinate(new Coordinate(x, y));
                button.addActionListener(this);
                panel.add(button);
                buttons[y][x] = button;
            }
        }
        super.add(panel, BorderLayout.CENTER);

        super.setVisible(true);
//        super.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        super.pack();
    }

    @Override
    public void makeATurn(TicTacToe ticTacToeGame) {
        super.requestFocus();

        checkGame(ticTacToeGame);

        messageLabel.setText("Put a " + currentElem.toString());
        do {
            waitingForInput = true;
            try {
                coordinateLock.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                ticTacToeGame.makeATurn(selected);
            } catch (CellTakenException e) {
                continue;
            } catch (GameFinishedException e) {
                e.printStackTrace();
            }
            break;
        } while (true);

    }

    private void checkGame(TicTacToe ticTacToeGame) {
        if (currentGame == ticTacToeGame)
            return;

        currentGame = ticTacToeGame;
        currentGame.addObserver(this);
        showBoardState(currentGame.getBoardState());
        currentElem = currentGame.getNextTurn();
    }

    public void showBoardState(final Elem[][] boardState) {
        for (short y = 0; y < TicTacToe.BOARD_SIZE; y++) {
            for (short x = 0; x < TicTacToe.BOARD_SIZE; x++) {
                buttons[y][x].setText(boardState[y][x].toString());
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!waitingForInput)
            return;

        ButtonWithCoordinate button = (ButtonWithCoordinate) e.getSource();
        selected = button.coordinate;

        coordinateLock.release();
        waitingForInput = false;
    }

    @Override
    public String toString() {
        return "SwingPlayer{" + super.getTitle() + "}";
    }

    @Override
    public void updated(TicTacToe caller) {
        showBoardState(caller.getBoardState());

        if (!caller.isFinished()) {
            messageLabel.setText(WAITING);
            return;
        }

        Elem winner = caller.getWinnerElement();
        if (winner == Elem.BLANK) {
            messageLabel.setText("Draw.");
        } else if (winner == currentElem) {
            messageLabel.setText("You won!");
        } else {
            messageLabel.setText("You lost...");
        }
    }
}

class ButtonWithCoordinate extends JButton {

    Coordinate coordinate;

    public ButtonWithCoordinate(Coordinate coordinate) {
        super("   ");
        this.coordinate = coordinate;
    }
}
