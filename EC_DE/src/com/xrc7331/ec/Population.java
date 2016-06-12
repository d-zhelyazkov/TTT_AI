package com.xrc7331.ec;

import java.util.Collection;

/**
 * A population for an Evolutionary algorithm.
 * Represents a collection of agents({@link Individual}).
 */
public interface Population<IndividualsType extends Individual> {
    Collection<IndividualsType> getIndividuals();

    void setIndividuals(Collection<IndividualsType> individuals);
}
