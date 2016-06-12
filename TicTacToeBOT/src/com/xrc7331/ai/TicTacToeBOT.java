package com.xrc7331.ai;

import com.xrc7331.ec.Individual;
import com.xrc7331.game.*;
import com.xrc7331.tools.Coordinate;
import com.xrc7331.tools.observer.Observer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.nnet.MultiLayerPerceptron;

import java.math.BigInteger;
import java.util.*;

/**
 * Bot that is capable of playing the game Tic tac toe
 * and logging its games' results.
 */
public class TicTacToeBOT implements
        TicTacToePlayer
        , Individual<Double>
        , Observer<TicTacToe> {

    private static final int[] ANN_TOPOLOGY = {9, 10, 9};

    private NeuralNetwork neuralNetwork;
    private BigInteger wins;
    private BigInteger gamesPlayed;
    private BigInteger draws;
    private HashMap<TicTacToe, Elem> games = new HashMap<>();

    public TicTacToeBOT() {
        wins = BigInteger.ZERO;
        gamesPlayed = BigInteger.ZERO;
        draws = BigInteger.ZERO;

        neuralNetwork = new MultiLayerPerceptron(ANN_TOPOLOGY);
        neuralNetwork.randomizeWeights();
    }

    public TicTacToeBOT(final Double[] weights) {
        this(BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO, weights);
    }

    public TicTacToeBOT(BigInteger wins, BigInteger draws, BigInteger gamesPlayed, final Double[] weights) {

        this.wins = wins;
        this.draws = draws;
        this.gamesPlayed = gamesPlayed;

        neuralNetwork = new MultiLayerPerceptron(ANN_TOPOLOGY);
        neuralNetwork.setWeights(Arrays.stream(weights).mapToDouble(Double::doubleValue).toArray());
    }

    public TicTacToeBOT(final TicTacToeBOT bot) {
        this(bot.wins, bot.draws, bot.gamesPlayed, bot.neuralNetwork.getWeights());
//        this(bot.neuralNetwork.getWeights());
    }

    @Override
    public void makeATurn(TicTacToe ticTacToeGame) {
        checkGame(ticTacToeGame);

        //maps the board's cells - BLANK - 0.5, Self - 1, Opponent - 0
        final Elem ownElement = ticTacToeGame.getNextTurn();
        final Elem[][] boardState = ticTacToeGame.getBoardState();
        final double[] inputs = new double[ANN_TOPOLOGY[0]];
        final Set<Short> emptyCellIXs = new HashSet<>();
        short i = 0;
        for (short y = 0; y < TicTacToe.BOARD_SIZE; y++) {
            for (short x = 0; x < TicTacToe.BOARD_SIZE; x++) {
                final Elem boardCell = boardState[y][x];
                if (boardCell == Elem.BLANK) {
                    inputs[i] = 0.5;
                    emptyCellIXs.add(i);
                } else {
                    inputs[i] = (boardCell == ownElement) ? 1 : 0;
                }
                i++;
            }
        }

        //executes the neural network with the given inputs
        neuralNetwork.setInput(inputs);
        neuralNetwork.calculate();
        double[] outputs = neuralNetwork.getOutput();

        //finds the index of the max output value from an empty cell
        short maxIX = -1;
        for (i = 0; i < ANN_TOPOLOGY[0]; i++) {
            if (emptyCellIXs.contains(i)
                    && ((maxIX == -1) || (outputs[maxIX] < outputs[i])))
                maxIX = i;
        }
        if (maxIX == -1)
            return;

        try {
            //plays a turn with the cell indicated by the found index
            ticTacToeGame.makeATurn(new Coordinate(maxIX % TicTacToe.BOARD_SIZE, maxIX / TicTacToe.BOARD_SIZE));
        } catch (CellTakenException e) {
            e.printStackTrace();
        } catch (GameFinishedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Marks a game and its element.
     * Also registers an interest in order to snoop game updates.
     * @param ticTacToeGame the processed game
     */
    private void checkGame(final TicTacToe ticTacToeGame) {
        if (games.containsKey(ticTacToeGame))
            return;

        games.put(ticTacToeGame, ticTacToeGame.getNextTurn());
        ticTacToeGame.addObserver(this);
    }

    /**
     * Receives an update from a game.
     * If the game has finished, the bot updates its achievements.
     *
     * @param caller the updated game
     */
    @Override
    public void updated(TicTacToe caller) {
        if (!caller.isFinished())
            return;

        final Elem ownElement = games.get(caller);
        if (ownElement == null)
            return;

        gamesPlayed = gamesPlayed.add(BigInteger.ONE);
        games.remove(caller);
        final Elem winner = caller.getWinnerElement();
        if (winner == Elem.BLANK) {
            draws = draws.add(BigInteger.ONE);
        } else if (ownElement == winner) {
            wins = wins.add(BigInteger.ONE);
        }
    }

    @Override
    public Collection<Double> getGenes() {
        return Arrays.asList(neuralNetwork.getWeights());
    }

    @Override
    public Individual<Double> clone() {
        return new TicTacToeBOT(this);
    }

    public BigInteger getWins() {
        return wins;
    }

    public BigInteger getGamesPlayed() {
        return gamesPlayed;
    }

    public BigInteger getDraws() {
        return draws;
    }

    @Override
    public String toString() {
        return "Bot{" +
                "wins=" + wins +
                ", draws=" + draws +
                ", gamesPlayed=" + gamesPlayed +
                '}';
    }
}
