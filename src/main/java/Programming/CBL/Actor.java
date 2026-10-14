package Programming.CBL;

import java.awt.Graphics2D;
import java.awt.Shape;

/**
 * Actor class is an abstract class that is the root class of all actors within the
 * game that have a position on screen and a health value.
 * Actors are plain data objects; they are drawn by the GamePanel.
 */
public abstract class Actor {
    protected GamePanel panel;
    protected double speed = 100.0; // pixels per second
    protected double x;
    protected double y;
    protected final int width;
    protected final int height;

    public Actor(double x, double y, int width, int height, GamePanel panel) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.panel = panel;
    }

    /**
     * @deprecated
     * Moves the actor horizontally by direction * SPEED * elapsedSeconds,
     * clamped to the area [0, areaWidth].
     * @param direction -1 for left, 1 for right
     * @param elapsedSeconds time since the last update
     * @param areaWidth width of the play area
     */
    public void moveX(int direction, double elapsedSeconds, int areaWidth) {
        double nextX = x + direction * speed * elapsedSeconds;
        x = Math.max(0, Math.min(nextX, areaWidth - width));
    }

    /**
     * @deprecated 
     * Moves the actor vertically by direction * SPEED * elapsedSeconds,
     * clamped to the area [0, areaHeight].
     * @param direction -1 for up, 1 for down
     * @param elapsedSeconds time since the last update
     * @param areaHeight height of the play area
     */
    public void moveY(int direction, double elapsedSeconds, int areaHeight) {
        double nextY = y + direction * speed * elapsedSeconds;
        y = Math.max(0, Math.min(nextY, areaHeight - height));
    }

    /**
     * Moves the actor by (dx, dy) over elapsedSeconds,
     * clamped to the area [0, areaWidth] x [0, areaHeight].
     * @param dx horizontal distance to move
     * @param dy vertical distance to move
     * @param elapsedSeconds time since the last update
     * @param areaWidth width of the play area
     * @param areaHeight height of the play area
     */
    public void move(double dx, double dy, double elapsedSeconds) {
        double length = Math.hypot(dx, dy);
        
        // Normalize the movement speed
        if (length > 0) {
            dx /= length;
            dy /= length;
        }

        // Set new position
        x = x + dx * speed * elapsedSeconds;
        y = y + dy * speed * elapsedSeconds;
    }

    /**
     * Reduces health of the actor.
     * @param amount amount health is reduced by.
     * @return true if health above 0
     */
    public abstract boolean reduceHealth(int amount);

    /**
     * Increases health of the actor.
     * @param amount amount health is increased by.
     * @return true if health above 0
     */
    public abstract boolean increaseHealth(int amount);

    /**
     * Returns the actor's current collision and drawing shape.
     * @return the actor's shape
     */
    public abstract Shape getShape();

    /**
     * Draws the actor.
     * @param g graphics context to draw with
     */
    public abstract void draw(Graphics2D g);
}