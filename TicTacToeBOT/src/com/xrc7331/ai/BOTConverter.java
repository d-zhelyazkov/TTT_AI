package com.xrc7331.ai;

import com.xrc7331.ec.CreationException;
import com.xrc7331.ec.IndividualsCreator;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by XRC_7331 on 6/10/2016.
 */
public final class BOTConverter implements IndividualsCreator<Double, TicTacToeBOT> {

    private static final int STRINGS_OFFSET = 0;


    private static final BOTConverter instance = new BOTConverter();

    public static BOTConverter getInstance() {
        return instance;
    }

    private BOTConverter() {
    }


    public TicTacToeBOT createFromStrings(final Collection<String> strings) throws CreationException {
        TicTacToeBOT newBot;
        try {
            final Iterator<String> stringIterator = strings.iterator();
//            final BigInteger wins = new BigInteger(stringIterator.next());
//            final BigInteger draws = new BigInteger(stringIterator.next());
//            final BigInteger games = new BigInteger(stringIterator.next());

            final Double[] weights = new Double[strings.size() - STRINGS_OFFSET];
            for (int i = 0; stringIterator.hasNext(); i++)
                weights[i] = Double.parseDouble(stringIterator.next());

//            newBot = new TicTacToeBOT(wins, draws, games, weights);
            newBot = new TicTacToeBOT(weights);
        } catch (Exception e) {
            throw new CreationException(e);
        }

        return newBot;
    }

    public String[] toStringsArray(final TicTacToeBOT bot) {
        final Collection<Double> weights = bot.getGenes();
        final String[] strings = new String[weights.size() + STRINGS_OFFSET];
//        strings[0] = bot.getWins().toString();
//        strings[1] = bot.getDraws().toString();
//        strings[2] = bot.getGamesPlayed().toString();

        Iterator<Double> weightIterator = weights.iterator();
        for (int i = STRINGS_OFFSET; weightIterator.hasNext(); i++)
            strings[i] = weightIterator.next().toString();

        return strings;
    }

    @Override
    public TicTacToeBOT create(final Collection<Double> genes) {
//        final TicTacToeBOT newBot = new TicTacToeBOT(BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO, genes.toArray(new Double[genes.size()]));
        final TicTacToeBOT newBot = new TicTacToeBOT(genes.toArray(new Double[genes.size()]));
        return newBot;
    }

    @Override
    public TicTacToeBOT createFromDoubles(final Collection<Double> genes) {
        return create(genes);
    }


}
