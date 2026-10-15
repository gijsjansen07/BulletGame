package Programming.CBL;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

/**
 * Represents the playable character controlled via keyboard input.
 */
public class Player extends Actor {
    private String name;
    private int highscore = 0;
    private static final int SIZE = 15;

    public Player(String name, int[] startPos) {
        super(startPos[0], startPos[1], SIZE, SIZE);
        this.name = name;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getSize() {
        return SIZE;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.BLUE);
        // Sub-pixel rendering with anti-aliasing
        g.fill(new Ellipse2D.Double(x, y, SIZE, SIZE));
    }
}