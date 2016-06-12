package com.xrc7331;

import com.xrc7331.ec.Individual;
import com.xrc7331.ec.IndividualsCreator;
import com.xrc7331.ec.de.DifferentialEvolution;

import java.util.*;
import java.util.function.Function;

public class Main {

    private static final int GENES = 4;
    private static final int GOAL = 30;
    private static final double CR = 0.5;
    private static final double F = 1;
    private static final int POPULATION_SIZE = 20;
    private static final int MAX_GENERATIONS = 200;

    private static final Map<Equation, Integer> fitnesses = new HashMap<>();

    final static IndividualsCreator<Integer, Equation> integerIndividualsCreator = new IndividualsCreator<Integer, Equation>() {
        @Override
        public Equation create(Collection<Integer> genes) {
            return new Equation(genes);
        }

        @Override
        public Equation createFromDoubles(Collection<Double> genes) {
            final Collection<Integer> integerGenes = new LinkedList<>();
            for (Double gene : genes) {
                integerGenes.add(gene.intValue());
            }
            return create(integerGenes);
        }
    };
    final static Function<Equation, Integer> fitnessFunction = equation -> {
        if (fitnesses.containsKey(equation))
            return fitnesses.get(equation);

        Iterator<Integer> iterator = equation.getGenes().iterator();
        int sum = 0;
        for (int i = 0; i < GENES; i++) {
            sum += iterator.next();
        }

        int diff = Math.abs(GOAL - sum);
        int fitness = Integer.MAX_VALUE - diff;
        fitnesses.put(equation, fitness);
        return fitness;
    };
    final static Comparator<Equation> comparator = (o1, o2) -> Integer.compare(fitnessFunction.apply(o1), fitnessFunction.apply(o2));

    public static void main(String[] args) {
        Collection<Equation> generations = new LinkedList<>();
        for (int i = 0; i < POPULATION_SIZE; i++)
            generations.add(new Equation(GENES,GOAL));


        final DifferentialEvolution<Integer, Equation, Integer> differentialEvolution = new DifferentialEvolution(CR, F);

        for (int i = 0; i < MAX_GENERATIONS; i++) {
            generations = differentialEvolution.evolve(generations, fitnessFunction, integerIndividualsCreator);
            final Equation max = Collections.max(generations, comparator);
            Integer fitness = Integer.MAX_VALUE - fitnessFunction.apply(max);
            System.out.println(fitness);
            if (fitness == 0) {
                System.out.printf("Generation: %d\nEquation: %s\n", i, max);
                break;
            }
        }
    }
}


