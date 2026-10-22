package Programming.CBL.Objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.Random;

import Programming.CBL.Interfaces.SelfMoving;
import Programming.CBL.Manager.ProjectileManager;
import Programming.CBL.Panels.GamePanel;

/**
 * Represents an enemy entity moving across the screen from any side.
 * Periodically shoots bullets towards the player's position.
 */
public class Enemy extends Actor implements SelfMoving {
    private static final int SIZE = 30;
    private double speedX;
    private double speedY;

    // Shooting cooldown timer
    private double shootTimer;
    private double shootInterval;
    private static final Random RANDOM = new Random();

    /**
     * Constructs a new enemy with 2D velocity and a rapid shoot interval.
     */
    public Enemy(Point2D startPos, double speedX, double speedY, GamePanel panel) {
        super(startPos, SIZE, SIZE, panel);
        this.speedX = speedX;
        this.speedY = speedY;
        // Interval between shots reduced to ~0.5s - 0.8s for much faster firing
        this.shootInterval = 0.5 + (RANDOM.nextDouble() * 0.3);
        this.shootTimer = RANDOM.nextDouble() * 0.4; 
    }

    @Override
    public boolean update(double elapsedSeconds) {
        double x = this.pos.getX() + speedX * elapsedSeconds;
        double y = this.pos.getY() + speedY * elapsedSeconds;
        this.pos.setLocation(x, y);
        return true;
    }

    @Override
    public Faction getFaction() {
        return Faction.ENEMY;
    }

    /**
     * Ticks the weapon cooldown and fires an EnemyProjectile towards the player.
     */
    public void shootAt(Point2D playerPos, double dt, ProjectileManager pManager) {
        shootTimer += dt;
        if (shootTimer >= shootInterval) {
            shootTimer = 0.0;

            // Direction vector towards player
            double dirX = playerPos.getX() - this.pos.getX();
            double dirY = playerPos.getY() - this.pos.getY();
            double length = Math.hypot(dirX, dirY);

            if (length > 0) {
                dirX /= length;
                dirY /= length;
                Point2D dirVec = new Point2D.Double(dirX, dirY);
                pManager.addProjectile(new EnemyProjectile(
                        new Point2D.Double(this.pos.getX(), this.pos.getY()),
                        dirVec,
                        this,
                        panel
                ));
            }
        }
    }

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

}