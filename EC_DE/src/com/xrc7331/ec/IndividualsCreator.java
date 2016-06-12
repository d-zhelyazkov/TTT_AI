package com.xrc7331.ec;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by XRC_7331 on 6/8/2016.
 */
public interface IndividualsCreator<GenesBase, IndividualType extends Individual<GenesBase>> {
    IndividualType create(Collection<GenesBase> genes) throws CreationException;

    IndividualType createFromDoubles(Collection<Double> genes) throws CreationException;
}
