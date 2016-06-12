package com.xrc7331;

import com.xrc7331.ec.Individual;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by XRC_7331 on 6/9/2016.
 */
public class Equation implements Individual<Integer> {

    private Collection<Integer> values;

    public Equation(int genes, int maxRandomGene) {
        Random random = new Random();
        values = random.ints(genes, 0, maxRandomGene).boxed().collect(Collectors.toList());
    }

    public Equation(Collection<Integer> genes) {
        this.values = genes;
    }

    @Override
    public Collection<Integer> getGenes() {
        return values;
    }

    @Override
    public Individual clone() {
        return new Equation(new LinkedList(this.values));
    }

    @Override
    public String toString() {
        return "Equation{" +
                "values=" + values +
                '}';
    }
}
