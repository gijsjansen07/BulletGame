package Programming.CBL;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;

public class Player extends Actor{
    private String name;
    private int highscore = 0;
    private static final int SIZE = 30;

    public Player(String name, int[] startPos, JFrame frame) {
        super(frame);
        this.name = name;
        this.pos[0] = startPos[0];
        this.pos[1] = startPos[1];
        this.nextPos = this.pos;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLUE);
        g.fillRect(nextPos[0], nextPos[1], SIZE, SIZE);
        pos = nextPos;
    }
    
}
