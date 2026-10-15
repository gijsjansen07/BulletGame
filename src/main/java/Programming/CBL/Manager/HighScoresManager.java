package Programming.CBL.Manager;


import Programming.CBL.Objects.ScoreObject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.TreeMap;


public class HighScoresManager {
    private static final File FILE = new File("Highscores.txt");
    
    static {
        try {
            FILE.createNewFile();
        } catch (IOException e) {
            System.out.println("An error has occurred while creating Highscores.txt .");
            e.printStackTrace();
        }
    }

    public static TreeMap<Long, ScoreObject> getScores() {
        try (Scanner reader = new Scanner(FILE)) {
            if (!FILE.canRead()) {
                throw new IOException("Cannot read Highscores.txt");
            }
            TreeMap<Long, ScoreObject> scores = new TreeMap<Long, ScoreObject>();

            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] segments = line.split("[ ]");
                LocalDateTime date = LocalDateTime.parse(segments[0]);
                String name = segments[1];
                long score = Long.parseLong(segments[2]);
                ScoreObject scoreObject = new ScoreObject(date, name, score);
                scores.put(scoreObject.getScore(), scoreObject);
            }
            return scores;
        } catch (Exception e) {
            return new TreeMap<Long, ScoreObject>();
        }
    }

    public static boolean addScore(ScoreObject scoreObject) {
        try (FileWriter fileWriter = new FileWriter(FILE, true)) {
            if (!FILE.canWrite()) {
                throw new IOException("Cannot read Highscores.txt");
            }

            fileWriter.write(scoreObject.toString());
            return true;

        } catch (Exception e) {
            return false;
        }
    }
}
