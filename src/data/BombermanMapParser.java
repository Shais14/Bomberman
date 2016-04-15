package data;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Anand on 4/14/2016.
 */
public class BombermanMapParser {
    static int lineNumber = 0;
    static Scanner scanner;

    public static ArrayList<String> parseMapConfig(String mapConfigFilePath) {
        ArrayList<String> mapConfig = new ArrayList<>();
        Path path = Paths.get(mapConfigFilePath);
        try {
            scanner = new Scanner(path, StandardCharsets.UTF_8.name());
            while (scanner.hasNextLine()) {
                mapConfig.add(readNextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mapConfig;
    }

    public static String readNextLine() {
        lineNumber++;
        return scanner.nextLine();
    }
}
