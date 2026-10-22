package Programming.CBL.Objects;


import Programming.CBL.Panels.GamePanel;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;



/**
 * Represents a projectile fired by an enemy directed towards the player.
 * Inherits movement and boundary updates from Projectile.
 */
public class EnemyProjectile extends Projectile {
    // Increased size slightly (from 8 to 14) for better visibility
    private static final int PROJECTILE_SIZE = 14;
    private static final int ENEMY_BULLET_SPEED = 50;

    /**
     * Constructs an enemy bullet moving towards a normalized direction.
     * @param pos Start position of the projectile.
     * @param dir Direction vector towards the target.
     * @param origin The enemy firing the projectile.
     * @param panel The active game panel canvas.
     */
    public EnemyProjectile(Point2D pos, Point2D dir, Actor origin, GamePanel panel) {
        super(pos, PROJECTILE_SIZE, PROJECTILE_SIZE, dir, origin, ENEMY_BULLET_SPEED, panel);
    }

    @Override
    public void draw(Graphics2D g) {
        // Distinct orange color
        g.setColor(Color.ORANGE);
        g.fill(getShape());
    }

    @Override
    public Shape getShape() {
        return new Ellipse2D.Double(pos.getX(), pos.getY(), width, height);
    }
}