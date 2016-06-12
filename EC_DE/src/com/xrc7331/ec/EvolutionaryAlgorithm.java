package com.xrc7331.ec;

import java.util.Collection;
import java.util.function.Function;

public interface EvolutionaryAlgorithm<GenesType, IndividualType extends Individual<GenesType>, FitnessResult extends Comparable<FitnessResult>> {

    /**
     * Evolves a population with one generation.
     * Should implement a crossover and evaluation functionality here.
     *
     * @param generation the population that is going to be evolved
     * @param fitness    the fitness function used for evaluation
     * @return the new generation
     */
    Collection<IndividualType> evolve(Collection<IndividualType> generation, Function<IndividualType, FitnessResult> fitness, IndividualsCreator<GenesType, IndividualType> creator);
}
