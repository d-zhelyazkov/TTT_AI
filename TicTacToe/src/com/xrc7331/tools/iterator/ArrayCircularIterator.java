package com.xrc7331.tools.iterator;

import java.util.ListIterator;

/**
 * Class for unlimited iterating through an array.
 * In a case when the iterator reaches the end,
 * it starts again from the beginning.
 */
public class ArrayCircularIterator<ElementType> implements ListIterator<ElementType> {

    private ElementType[] array;
    private int index;

    /**
     * Initializes an iterator.
     *
     * @param array the array that is going to be iterated through
     * @throws UnsupportedOperationException if the array is zero-sized
     */
    public ArrayCircularIterator(final ElementType... array) {
        if (array.length == 0)
            throw new UnsupportedOperationException("Zero sized arrays not supported!");
        this.array = array;
    }

    /**
     * Not supported for an array!
     */
    @Override
    public void add(final Object object) {
        throw new UnsupportedOperationException("Not supported for an array!");
    }

    /**
     * Checks for the availability of a next element.
     * Since this is a circular iterator it always returns true.
     */
    @Override
    public boolean hasNext() {
        return true;
    }

    /**
     * Checks for the availability of a previous element.
     * Since this is a circular iterator it always returns true.
     */
    @Override
    public boolean hasPrevious() {
        return true;
    }

    /**
     * {@link ListIterator#next()}
     * If the iterator is at the end of the array,
     * it returns the first element.
     *
     * @return the next element.
     */
    @Override
    public ElementType next() {
        final ElementType current = getCurrent();
        index = nextIndex();
        return current;
    }

    @Override
    public int nextIndex() {
        return validateIX(this.index + 1);
    }

    /**
     * {@link ListIterator#next()}
     * If the iterator is at the beginning of the array,
     * it returns the last element.
     *
     * @return the previous element.
     */
    @Override
    public ElementType previous() {
        final ElementType current = getCurrent();
        index = previousIndex();
        return current;
    }

    @Override
    public int previousIndex() {
        return validateIX(index - 1);
    }

    /**
     * Not supported for an array.
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void set(final ElementType object) {
        array[index] = object;
    }

    public ElementType getCurrent() {
        return array[index];
    }

    public ElementType reset() {
        index = 0;
        return getCurrent();
    }

    private int validateIX(int ix) {
        ix += array.length;
        return ix % array.length;
    }
}
