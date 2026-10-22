package Programming.CBL.Objects;

import Programming.CBL.Panels.GamePanel;
import java.awt.geom.Area;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;

/**
 * Actor class is an abstract class that is the root class of all actors within the
 * game that have a position on screen.
 * Actors are plain data objects; they are drawn by the GamePanel.
 */
public abstract class Actor {
    protected GamePanel panel;
    protected double speed; // pixels per second
    public Point2D pos;
    protected final int width;
    protected final int height;

    public Actor(Point2D pos, int width, int height, GamePanel panel) {
        this.pos = pos;
        this.width = width;
        this.height = height;
        this.panel = panel;
    }

    /**
     * Moves the actor by (dx, dy) over elapsedSeconds,
     * clamped to the area [0, areaWidth] x [0, areaHeight].
     * @param dx horizontal distance to move
     * @param dy vertical distance to move
     * @param elapsedSeconds time since the last update
     */
    public void move(double dx, double dy, double elapsedSeconds) {
        double length = Math.hypot(dx, dy);
        
        // Normalize the movement speed
        if (length > 0) {
            dx /= length;
            dy /= length;
        }

        // Set new position
        double newX = pos.getX() + dx * speed * elapsedSeconds;
        double newY = pos.getY() + dy * speed * elapsedSeconds;
        pos.setLocation(newX, newY);
    }

    /**
     * Returns the actor's current collision and drawing shape.
     * @return the actor's shape
     */
    public abstract Shape getShape();

    /**
     * Sees if the actor overlaps with another actor. First does a rough box check and if 
     * that holds then does a precise Area check.
     * @param other actor to compare with
     * @return true if overlaps
     */
    public boolean overlaps(Actor other) {
        Shape thisShape = getShape();
        Shape otherShape = other.getShape();
        if (!thisShape.getBounds2D().intersects(otherShape.getBounds2D())) {
            return false;
        }

        Area overlap = new Area(thisShape);
        overlap.intersect(new Area(otherShape));
        return !overlap.isEmpty();
    }

    public abstract Faction getFaction();

    /**
     * Draws the actor.
     * @param g graphics context to draw with
     */
    public abstract void draw(Graphics2D g);

    public double getX() {
        return this.pos.getX();
    }

    public double getY() {
        return this.pos.getY();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}