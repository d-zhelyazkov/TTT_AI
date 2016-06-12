package com.xrc7331.ec;

import java.util.Collection;

/**
 * An agent for a Evolutionary algorithm.
 * Could be also called a chromosome.
 * Represents the agent's genes.
 */
public interface Individual<GenesBase> {
    Collection<GenesBase> getGenes();

    Individual<GenesBase> clone();
}
