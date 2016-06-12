package com.xrc7331.tools;

/**
 * Basic 2D (x,y) holder
 */
public class Coordinate {
    private int x;
    private int y;

    public Coordinate(final int x, final int y) {
        this.x =  x;
        this.y =  y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coordinate that = (Coordinate) o;

        if (x != that.x) return false;
        return y == that.y;

    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
