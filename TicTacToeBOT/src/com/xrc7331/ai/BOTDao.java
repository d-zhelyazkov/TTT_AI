package com.xrc7331.ai;

import com.xrc7331.ec.CreationException;
import com.xrc7331.tools.CSVTool;
import com.xrc7331.tools.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.*;

/**
 * Class used for storing bots' data
 */
public final class BOTDao {

    private static final CSVTool CSV_TOOL = CSVTool.getInstance();
    private static final BOTConverter BOT_CONVERTER = BOTConverter.getInstance();
    private static Path BOTS_FILE;

    static {
        try {
            BOTS_FILE = new File(ResourceLoader.getInstance().loadResource("bots.csv").toURI()).toPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private static final BOTDao ourInstance = new BOTDao();

    public static BOTDao getInstance() {
        return ourInstance;
    }

    private BOTDao() {
    }

    public void saveAll(final Collection<TicTacToeBOT> bots) throws IOException {
        final List<String[]> strings = new LinkedList<>();
        for (TicTacToeBOT bot : bots)
            strings.add(BOT_CONVERTER.toStringsArray(bot));

        CSV_TOOL.writeCSV(BOTS_FILE, strings);
    }

    public Collection<TicTacToeBOT> loadAll() throws IOException {
        Collection<String[]> strings = CSV_TOOL.readCSV(BOTS_FILE);
        List<TicTacToeBOT> bots = new LinkedList<>();
        for (String[] line : strings)
            try {
                bots.add(BOT_CONVERTER.createFromStrings(Arrays.asList(line)));
            } catch (CreationException e) {
                e.printStackTrace();
            }

        return bots;
    }
}
