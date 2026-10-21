package Programming.CBL.Objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import Programming.CBL.Interfaces.SelfMoving;
import Programming.CBL.Panels.GamePanel;

/**
 * Represents an enemy entity moving across the screen from any side.
 * Inherits common position and health logic from Actor.
 */
public class Enemy extends Actor implements SelfMoving {
    private static final int SIZE = 20;
    private double speedX;
    private double speedY;

    /**
     * Constructs a new enemy with 2D velocity.
     * @param startX Initial horizontal coordinate.
     * @param startY Initial vertical coordinate.
     * @param speedX Horizontal velocity (pixels/second).
     * @param speedY Vertical velocity (pixels/second).
     */
    public Enemy(Point2D startPos, double speedX, double speedY, GamePanel panel) {
        super(startPos, SIZE, SIZE, panel);
        this.speedX = speedX;
        this.speedY = speedY;
    }

    /**
     * Updates position on both axes using delta time.
     * @param elapsedSeconds Time elapsed since previous tick.
     */
    public boolean update(double elapsedSeconds) {
        double x = this.pos.getX() + speedX * elapsedSeconds;
        double y = this.pos.getY() + speedY * elapsedSeconds;
        this.pos.setLocation(x, y);
        return true;
    }

    /**
     * Checks if the enemy has completely crossed outside any of the 4 boundaries.
     * @param this.panel.getWidth() Screen width limit.
     * @param areaHeight Screen height limit.
     * @return True if off-screen, false otherwise.
     */
    public boolean isOffScreen() {
        return (speedX > 0 && this.pos.getX() > this.panel.getWidth() + 60)
                || (speedX < 0 && this.pos.getX() < -60)
                || (speedY > 0 && this.pos.getY() > this.panel.getHeight() + 60)
                || (speedY < 0 && this.pos.getY() < -60);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.RED);
        g.fill(getShape());
    }

    @Override
    public Shape getShape() {
        return new Ellipse2D.Double(pos.getX(), pos.getY(), SIZE, SIZE);
    }

    public boolean hits(Actor a) {
        if (this == a) {
            return false;
        }

        Shape targetShape = a.getShape();
        Shape projectileShape = getShape();
        if (!targetShape.getBounds2D().intersects(projectileShape.getBounds2D())) {
            return false;
        }

        Area overlap = new Area(targetShape);
        overlap.intersect(new Area(projectileShape));
        if (overlap.isEmpty()) {
            return false;
        }

        return true;
    }
}