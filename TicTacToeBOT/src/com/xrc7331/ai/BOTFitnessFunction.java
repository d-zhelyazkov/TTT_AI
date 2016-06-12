package com.xrc7331.ai;

import com.xrc7331.game.Game;
import com.xrc7331.tools.observer.Observer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by XRC_7331 on 6/10/2016.
 */
public class BOTFitnessFunction implements Function<TicTacToeBOT, BOTFitness>, Observer<BOTPopulation> {

    private static final Game GAME = Game.getInstance();

    private BOTPopulation population;
    //private Set<TicTacToeBOT> testedBots = new HashSet<>();
    private Map<TicTacToeBOT, BOTFitness> testedBots = new HashMap<>();

    /**
     * Tests a bot with all bots in the population.
     * Marks the bot as tested with the current population in order to escape another testing - performance improvement.
     *
     * @param bot the tested bot
     * @return the win ratio; e.g.: wins = 123, games = 500, ratio = wins / games = 123 / 500 = 0.246
     */
    @Override
    public BOTFitness apply(TicTacToeBOT bot) {
        BOTFitness botFitness = testedBots.get(bot);

        if (botFitness == null) {
            int gamesPlayed = 0;
            int wins = 0;
            for (TicTacToeBOT player2 : population.getIndividuals()) {
                if (bot == player2)
                    continue;

                //two games are being played with the same opponent
                final Game.Result[] gameResults = {GAME.play(bot, player2), GAME.play(player2, bot)};
                gamesPlayed++;

                //the match is won if both games are won by the tested bot
                boolean winner = true;
                for (Game.Result result : gameResults)
                    winner &= (result.getWinner() == bot);

                if (winner)
                    wins++;
            }
            botFitness = new BOTFitness(wins, gamesPlayed);
            testedBots.put(bot, botFitness);
        }
//
//        final double wins = bot.getWins().doubleValue();
//        final double games = bot.getGamesPlayed().doubleValue();

        return botFitness;
    }

    public void setPopulation(final BOTPopulation population) {
        this.population = population;
        this.population.addObserver(this);
        testedBots.clear();
    }

    @Override
    public void updated(BOTPopulation caller) {
        population = caller;
        testedBots.clear();
    }


}
