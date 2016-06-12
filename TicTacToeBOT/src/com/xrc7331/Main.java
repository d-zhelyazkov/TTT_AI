package com.xrc7331;

import com.xrc7331.ai.*;
import com.xrc7331.ec.de.DifferentialEvolution;
import com.xrc7331.game.Game;
import com.xrc7331.game.TicTacToePlayer;
import com.xrc7331.tools.observer.Observer;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by XRC_7331 on 6/11/2016.
 */
public class Main {
    private static final double CR = 0.3;
    private static final double F = 1;
    private static final int BOTS_COUNT = 101;
    private static final long EVOLUTION_INTERVAL = 500;

    private static final BOTConverter BOT_CONVERTER = BOTConverter.getInstance();
    private static final BOTDao BOT_DAO = BOTDao.getInstance();
    private static final Game GAME = Game.getInstance();

    private static final BOTFitnessFunction fitnessFunction = new BOTFitnessFunction();

    /**
     * A logging listener.
     * It also saves the generation state.
     */
    private static final Observer<BOTPopulation> populationListener = caller -> {
        try {
            BOT_DAO.saveAll(caller.getIndividuals());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("\n-------------Population evolved!-----------");
        System.out.printf("%d bots excluded...\n", caller.getExcludedBots().size());

        final TicTacToeBOT bestBot = caller.getBest();
        final BOTFitness botFitness = fitnessFunction.apply(bestBot);
        System.out.printf("Best %s\n", botFitness);
    };

    /**
     * A thread that is going to evolve the population over time
     */
    private static final Thread evolutionThread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                population.evolve();
                try {
                    Thread.sleep(EVOLUTION_INTERVAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    });


    private static BOTPopulation population;

    public static void main(String[] args) {
        final DifferentialEvolution<Double, TicTacToeBOT, BOTFitness> de = new DifferentialEvolution<>(CR, F);
        population = new BOTPopulation(fitnessFunction, de, BOT_CONVERTER);
        population.addObserver(populationListener);
        fitnessFunction.setPopulation(population);

        initPopulation();

        evolutionThread.start();

        final SwingPlayer player = new SwingPlayer("You");
        while (true) {
            final TicTacToeBOT bestBot = population.getBest();
            try {
                play(player, bestBot);
                play(bestBot, player);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }

        }
    }

    private static void play(TicTacToePlayer player1, TicTacToePlayer player2) throws InterruptedException {
        final Game.Result result = GAME.play(player1, player2);
        System.out.println(result);
        Thread.sleep(2000);
    }

    /**
     * Reads the saved bots and creates additional ones in order to form a population of {@value Main#BOTS_COUNT} individuals.
     */
    private static void initPopulation() {

        Collection<TicTacToeBOT> bots;
        try {
            bots = BOT_DAO.loadAll();
        } catch (IOException e) {
            e.printStackTrace();
            bots = new LinkedList<>();
        }

        int botsToBeCreated = BOTS_COUNT - bots.size();
        for (int i = 0; i < botsToBeCreated; i++)
            bots.add(new TicTacToeBOT());

        population.setIndividuals(bots);
    }
}
