package Programming.CBL;

import java.awt.Color;
import java.awt.Graphics;

public class Player extends Actor{
    private String name;
    private int highscore = 0;
    private static final int SIZE = 30;

    public Player(String name, int[] startPos) {
        super();
        this.name = name;
        this.x = startPos[0];
        this.y = startPos[1];
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLUE);
        g.fillRect(0, 0, SIZE, SIZE);
    }
    
}
