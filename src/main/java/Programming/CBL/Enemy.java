package Programming.CBL;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

/**
 * Represents an enemy entity moving across the screen from any side.
 * Inherits common position and health logic from Actor.
 */
public class Enemy extends Actor {
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
    public Enemy(double startX, double startY, double speedX, double speedY) {
        super(startX, startY, SIZE, SIZE);
        this.speedX = speedX;
        this.speedY = speedY;
    }

    /**
     * Updates position on both axes using delta time.
     * @param elapsedSeconds Time elapsed since previous tick.
     */
    public void update(double elapsedSeconds) {
        x += speedX * elapsedSeconds;
        y += speedY * elapsedSeconds;
    }

    /**
     * Checks if the enemy has completely crossed outside any of the 4 boundaries.
     * @param areaWidth Screen width limit.
     * @param areaHeight Screen height limit.
     * @return True if off-screen, false otherwise.
     */
    public boolean isOffScreen(int areaWidth, int areaHeight) {
        return (speedX > 0 && x > areaWidth + 60)
                || (speedX < 0 && x < -60)
                || (speedY > 0 && y > areaHeight + 60)
                || (speedY < 0 && y < -60);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.RED);
        g.fill(new Ellipse2D.Double(x, y, SIZE, SIZE));
    }
}