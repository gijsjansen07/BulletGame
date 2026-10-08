package Programming.CBL;

import java.awt.Graphics;
import javax.swing.*;

/**
 * Actor class is an abstract class that is the root class of all actors within the 
 * game that have a position on screen and a health value.
 * 
 */
public abstract class Actor extends JComponent {
    protected int health;
    protected static final double SPEED = 60.0;
    protected double x;
    protected double y;

    public Actor() {
    }

    /**
     * Moves the actor by y * speed.
     * @param x position the actor needs to move to
     */
    public void moveX(int direction, double elapsedSeconds) {
        double nextX = x + direction * SPEED * elapsedSeconds;
        if (nextX >= 0 && nextX + getWidth() <= getParent().getWidth()) {
            x = nextX;
            setLocation((int) Math.round(x), getY());
        }
    }
    
    /**
     * Moves the actor by y * speed.
     * @param y 
     */
    public void moveY(int direction, double elapsedSeconds) {
        double nextY = y + direction * SPEED * elapsedSeconds;
        if (nextY >= 0 && nextY + getHeight() <= getParent().getHeight()) {
            y = nextY;
            setLocation(getX(), (int) Math.round(y));
        }
    }

    /**
     * Reduces healt of the actor.
     * @param amount amount health is reduced by.
     * @return true if health above 0
     */
    public boolean reduceHealth(int amount) {
        health -= amount;
        return health > 0;
    }

    /**
     * Increases healt amount of actor.  
     * @param amount amount health is increased by.
     * @return true if health above 0
     */
    public boolean increaseHealth(int amount) {
        health -= amount;
        return health > 0;
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}
