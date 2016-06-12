package com.xrc7331.ai;

/**
 * Created by XRC_7331 on 6/11/2016.
 */
public class BOTFitness implements Comparable<BOTFitness> {
    private int wins;
    private int gamesPlayed;

    public BOTFitness(final int wins, final int gamesPlayed) {
        this.wins = wins;
        this.gamesPlayed = gamesPlayed;
    }

    public int getWins() {
        return wins;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public double getRatio() {
        return (double) wins / gamesPlayed;
    }

    @Override
    public String toString() {
        return "BotFitness{" +
                "wins=" + wins +
                ", gamesPlayed=" + gamesPlayed +
                ", ratio=" + getRatio() +
                "}";
    }

    @Override
    public int compareTo(final BOTFitness o) {
        return Double.compare(this.getRatio(), o.getRatio());
    }
}
