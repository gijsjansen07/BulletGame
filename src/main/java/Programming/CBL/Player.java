package Programming.CBL;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.time.Duration;
import java.time.Instant;


public class Player extends Actor {
    private static final int TIME_BETWEEN_SHOTS = 200;
    private static final int SPEED = 100;

    private String name;
    private int highscore = 0;
    private int health = 100;
    private static final int SIZE = 15;
    private Instant lastShot = Instant.now();

    public Player(String name, Point2D startPos, GamePanel panel) {
        super(startPos, SIZE, SIZE, panel);
        this.name = name;
        this.speed = SPEED; 
    }

    public boolean shootProjectile(Point2D p, double dt, ProjectileManager pManager) {
        Instant now = Instant.now();
        if (Duration.between(lastShot, now).toMillis() > TIME_BETWEEN_SHOTS) {
            this.lastShot = now;
            return pManager.addProjectile(new Projectile(this.pos, width, height, p, this, 400, panel));
        }
        return false;
    }
    
    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.BLUE);
        g.fill(getShape());
    }

    @Override
    public Shape getShape() {
        return new Ellipse2D.Double(pos.getX(), pos.getY(), SIZE, SIZE);
    }

    @Override
    public void move(double dx, double dy, double elapsedSeconds) {
        double length = Math.hypot(dx, dy);

        // Normalize the movement speed
        if (length > 0) {
            dx /= length;
            dy /= length;
        }

        // Set new position and clamp to window borders.
        double newX = Math.max(0, Math.min(pos.getX() + dx * speed * elapsedSeconds, this.panel.getWidth() - width));
        double newY = Math.max(0, Math.min(pos.getY() + dy * speed * elapsedSeconds, this.panel.getHeight() - height));
        this.pos.setLocation(newX, newY);
    }

    public boolean reduceHealth(int amount) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'reduceHealth'");
    }
    
    public boolean increaseHealth(int amount) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'increaseHealth'");
    }
}