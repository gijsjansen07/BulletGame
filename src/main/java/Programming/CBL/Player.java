package Programming.CBL;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;


public class Player extends Actor {
    private String name;
    private int highscore = 0;
    private int health = 100;
    private static final int SIZE = 15;
    private double timeSinceLastShot = 0;

    public Player(String name, int[] startPos, GamePanel panel) {
        super(startPos[0], startPos[1], SIZE, SIZE, panel);
        this.name = name;
    }

    public Projectile shootProjectile(Point p, double dt) {

        return new Projectile(x, y, width, height, p.x, p.y, this, panel);
    }
    
    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.BLUE);
        g.fill(getShape());
    }

    @Override
    public Shape getShape() {
        return new Ellipse2D.Double(x, y, SIZE, SIZE);
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
        x = Math.max(0, Math.min(x + dx * speed * elapsedSeconds, this.panel.getWidth() - width));
        y = Math.max(0, Math.min(y + dy * speed * elapsedSeconds, this.panel.getHeight() - height));
    }

    @Override
    public boolean reduceHealth(int amount) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'reduceHealth'");
    }

    @Override
    public boolean increaseHealth(int amount) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'increaseHealth'");
    }
}