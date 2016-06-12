package com.xrc7331.tools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by XRC_7331 on 2/25/2016.
 */
public class CSVTool {
    public static final Pattern DATA_FORMAT = Pattern.compile("[^,\\n]*");
    private static final CSVTool INSTANCE = new CSVTool();

    public static CSVTool getInstance() {
        return INSTANCE;
    }

    private CSVTool() {
    }

    public Collection<String[]> readCSV(final Path path) throws IOException {

        List<String[]> result = new LinkedList<>();
        List<String> lines = Files.readAllLines(path);

        for (String line : lines) {
            //System.out.println(line);
            String[] details = line.split(",");
            //System.out.println(details[0] + " " + details[1] + " : " + details[3] + " " + details[2]);
            result.add(details);
        }

        return result;
    }

    public void writeCSV(final Path path,final Collection<String[]> data) throws IOException {
        List<String> lines = new LinkedList<>();
        for (String[] dataLine : data)
            lines.add(String.join(",", dataLine));

        Files.write(path, lines);
    }
}
