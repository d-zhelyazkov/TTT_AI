package com.xrc7331.game;

/**
 * Created by XRC_7331 on 6/10/2016.
 */

public enum Elem {
    BLANK(" "),
    X("X"),
    O("O");

    Elem(String str) {
        this.str = str;
    }

    private String str;

    @Override
    public String toString() {
        return str;
    }
}
