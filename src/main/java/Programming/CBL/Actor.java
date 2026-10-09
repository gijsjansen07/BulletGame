package Programming.CBL;

import java.awt.Graphics2D;

/**
 * Actor class is an abstract class that is the root class of all actors within the
 * game that have a position on screen and a health value.
 * Actors are plain data objects; they are drawn by the GamePanel.
 */
public abstract class Actor {
    protected int health;
    protected static final double SPEED = 250.0; // pixels per second
    protected double x;
    protected double y;
    protected final int width;
    protected final int height;

    public Actor(double x, double y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Moves the actor horizontally by direction * SPEED * elapsedSeconds,
     * clamped to the area [0, areaWidth].
     * @param direction -1 for left, 1 for right
     * @param elapsedSeconds time since the last update
     * @param areaWidth width of the play area
     */
    public void moveX(int direction, double elapsedSeconds, int areaWidth) {
        double nextX = x + direction * SPEED * elapsedSeconds;
        x = Math.max(0, Math.min(nextX, areaWidth - width));
    }

    /**
     * Moves the actor vertically by direction * SPEED * elapsedSeconds,
     * clamped to the area [0, areaHeight].
     * @param direction -1 for up, 1 for down
     * @param elapsedSeconds time since the last update
     * @param areaHeight height of the play area
     */
    public void moveY(int direction, double elapsedSeconds, int areaHeight) {
        double nextY = y + direction * SPEED * elapsedSeconds;
        y = Math.max(0, Math.min(nextY, areaHeight - height));
    }

    /**
     * Reduces health of the actor.
     * @param amount amount health is reduced by.
     * @return true if health above 0
     */
    public boolean reduceHealth(int amount) {
        health -= amount;
        return health > 0;
    }

    /**
     * Increases health of the actor.
     * @param amount amount health is increased by.
     * @return true if health above 0
     */
    public boolean increaseHealth(int amount) {
        health += amount;
        return health > 0;
    }

    /**
     * Draws the actor.
     * @param g graphics context to draw with
     */
    public abstract void draw(Graphics2D g);
}