package com.xrc7331.ec.de;

import com.xrc7331.ec.CreationException;
import com.xrc7331.ec.EvolutionaryAlgorithm;
import com.xrc7331.ec.Individual;
import com.xrc7331.ec.IndividualsCreator;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @see <a href="https://en.wikipedia.org/wiki/Differential_evolution">Differential evolution</a>
 */
public class DifferentialEvolution<GenesType extends Number, IndividualType extends Individual<GenesType>, FitnessResult extends Comparable<FitnessResult>>
        implements EvolutionaryAlgorithm<GenesType, IndividualType, FitnessResult> {

    private static final int MIN_POPULATION = 4;

    private static final Random RANDOM = new Random();
    private static final double MIN_CR = 0;
    private static final double MAX_CR = 1;
    private static final double MIN_F = 0;
    private static final double MAX_F = 2;

    private double CR;
    private double F;

    /**
     * @param CR crossover probability - [{@value DifferentialEvolution#MIN_CR},{@value DifferentialEvolution#MAX_CR}]
     * @param F  differential weight - [{@value DifferentialEvolution#MIN_F}, {@value DifferentialEvolution#MAX_F}]
     * @throws CRConstraintException if CR not in the defined interval
     * @throws FConstraintException  if F not in the defined interval
     */
    public DifferentialEvolution(double CR, double F) {
        if (CR < MIN_CR || MAX_CR < CR)
            throw new CRConstraintException();
        if (F < MIN_F || MAX_F < F)
            throw new FConstraintException();

        this.CR = CR;
        this.F = F;
    }

    /**
     * Evolves a generation using the famous Differential evolution algorithm
     *
     * @param generation the generation that is going to be evolved
     * @param fitness    the fitness function used for evaluation
     * @return the new evolved generation
     * @throws PopulationConstraintException if generation size is less than {@value DifferentialEvolution#MIN_POPULATION}
     */
    @Override
    public Collection<IndividualType> evolve(Collection<IndividualType> generation, Function<IndividualType, FitnessResult> fitness, IndividualsCreator<GenesType, IndividualType> creator) {
        final List<IndividualType> individuals = new ArrayList(generation);
        final int individualsCount = individuals.size();

        if (individualsCount < MIN_POPULATION)
            throw new PopulationConstraintException();

        //makes a list of numbers from 0 to individualsCount - 1
        final List<Integer> indexes = new LinkedList(IntStream.range(0, individualsCount).boxed().collect(Collectors.toList()));
        final Collection<IndividualType> newGeneration = new LinkedList();
        for (int i = 0; i < individualsCount; i++) {

            final IndividualType original = individuals.get(i);
            final Collection<GenesType> originalGenesCollection = original.getGenes();

            indexes.remove((Object) i);
            //shuffles the list and gets first three elements - the indexes of the used agents
            Collections.shuffle(indexes);
            final Iterator<Integer> iterator = indexes.iterator();
            final Iterator<GenesType> aGenes = individuals.get(iterator.next()).getGenes().iterator();
            final Iterator<GenesType> bGenes = individuals.get(iterator.next()).getGenes().iterator();
            final Iterator<GenesType> cGenes = individuals.get(iterator.next()).getGenes().iterator();
            indexes.add(i);

            int genesCount = originalGenesCollection.size();
            int R = RANDOM.nextInt(genesCount);
            Collection<Double> candidateGenes = new LinkedList();
            final Iterator<GenesType> originalGenes = originalGenesCollection.iterator();
            for (int j = 0; j < genesCount; j++) {
                final GenesType originalGene = originalGenes.next();
                final GenesType aGene = aGenes.next();
                final GenesType bGene = bGenes.next();
                final GenesType cGene = cGenes.next();

                //if(i==R | i<CR)
                //candidate=a+f*(b-c)
                Double newGene = originalGene.doubleValue();
                if ((j == R) || (RANDOM.nextDouble() < CR)) {
                    newGene = aGene.doubleValue() + F * (bGene.doubleValue() - cGene.doubleValue());
                    if (newGene.isNaN() || newGene.isInfinite())
                        newGene = originalGene.doubleValue();
                }
                assert (!newGene.isNaN() && !newGene.isInfinite());
                candidateGenes.add(newGene);

            }
            IndividualType newIndividual;
            try {
                newIndividual = creator.createFromDoubles(candidateGenes);
                if (fitness.apply(newIndividual).compareTo(fitness.apply(original)) <= 0)
                    newIndividual = original;
            } catch (CreationException e) {
                e.printStackTrace();
                newIndividual = original;
            }

            newGeneration.add(newIndividual);

        }
        return newGeneration;
    }
}
