package Programming.CBL.Objects;

import java.io.File;
import java.time.LocalDateTime;

/**
 * ScoreObject
 */
public class ScoreObject {
    private LocalDateTime date;
    private String name;
    private long score;

    public ScoreObject(LocalDateTime date, String name, long score) {
        this.date = date;
        this.name = name;
        this.score = score;
    }
    
    public boolean writeToFile(File file) {
        return false;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public long getScore() {
        return score;
    }

    @Override
    public String toString() {
        return date.toString() + " " + name + " " + score + "\n";
    }

}