package com.xrc7331.tools.iterator;

import java.util.*;

/**
 * Class for unlimited iterating through an array.
 * In a case when the iterator reaches the end,
 * it starts again from the beginning.
 */
public class CollectionCircularIterator<ElementType> implements Iterator<ElementType> {

    private Collection<ElementType> collection;
    private Iterator<ElementType> iterator;

    public CollectionCircularIterator(Collection<ElementType> collection) {
        this.collection = collection;
        reset();
    }

    /**
     * Put the iterator's cursor to the beginning.
     */
    public void reset(){
        iterator = collection.iterator();
    }

    public int getSize() {
        return collection.size();
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public ElementType next() {
        if(!this.hasNext())
            reset();

        return iterator.next();
    }

    @Override
    public void remove() {
        iterator.remove();
    }
}
