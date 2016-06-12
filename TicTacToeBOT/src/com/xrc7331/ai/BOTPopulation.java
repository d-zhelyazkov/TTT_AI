package com.xrc7331.ai;

import com.xrc7331.ec.EvolutionaryAlgorithm;
import com.xrc7331.ec.IndividualsCreator;
import com.xrc7331.ec.Population;
import com.xrc7331.tools.observer.Observable;
import com.xrc7331.tools.observer.Observer;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by XRC_7331 on 6/10/2016.
 */
public class BOTPopulation implements Population<TicTacToeBOT>, Observable<BOTPopulation> {

    private Collection<Observer<BOTPopulation>> observers = new HashSet<>();

    private LinkedList<TicTacToeBOT> bots;
    private Collection<TicTacToeBOT> excludedBots;
    //thread safe collection access
    private Semaphore botsLock = new Semaphore(1);

    private Function<TicTacToeBOT, BOTFitness> fitnessFunction;
    private EvolutionaryAlgorithm<Double, TicTacToeBOT, BOTFitness> evolutionaryAlgorithm;
    private IndividualsCreator<Double, TicTacToeBOT> botCreator;
    private final Comparator<TicTacToeBOT> comparator = new Comparator<TicTacToeBOT>() {
        @Override
        public int compare(TicTacToeBOT o1, TicTacToeBOT o2) {
            return fitnessFunction.apply(o1).compareTo(fitnessFunction.apply(o2));
        }
    };

    public BOTPopulation(
            final Function<TicTacToeBOT, BOTFitness> fitnessFunction,
            final EvolutionaryAlgorithm<Double, TicTacToeBOT, BOTFitness> evolutionaryAlgorithm,
            final IndividualsCreator<Double, TicTacToeBOT> botCreator) {
        this.fitnessFunction = fitnessFunction;
        this.evolutionaryAlgorithm = evolutionaryAlgorithm;
        this.botCreator = botCreator;
    }

    public void evolve() {
        final Collection<TicTacToeBOT> bots = getIndividuals();
        final Collection<TicTacToeBOT> newGeneration = evolutionaryAlgorithm.evolve(bots, fitnessFunction, botCreator);

        //keeps a track of bots that have been deleted by the last evolution algorithm's invoking
        excludedBots = new LinkedList<>();
        final Set<TicTacToeBOT> newBots = new HashSet<>(newGeneration);
        excludedBots.addAll(bots.stream().filter(bot -> !newBots.contains(bot)).collect(Collectors.toList()));

        this.setIndividuals(newGeneration);
        this.notifyObservers();
    }

    public TicTacToeBOT getBest() {
        return Collections.max(getIndividuals(), comparator);
    }

    @Override
    public Collection<TicTacToeBOT> getIndividuals() {
        Collection<TicTacToeBOT> clonedBotCollection = null;
        try {
            botsLock.acquire();
            clonedBotCollection = (Collection<TicTacToeBOT>) bots.clone();
            botsLock.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return clonedBotCollection;
    }

    @Override
    public void setIndividuals(Collection<TicTacToeBOT> individuals) {
        try {
            botsLock.acquire();
            bots = new LinkedList<>(individuals);
            botsLock.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addObserver(Observer<BOTPopulation> observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer<BOTPopulation> observer : observers)
            observer.updated(this);
    }

    public Collection<TicTacToeBOT> getExcludedBots() {
        return excludedBots;
    }
}
