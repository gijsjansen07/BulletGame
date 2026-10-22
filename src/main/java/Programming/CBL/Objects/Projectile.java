package Programming.CBL.Objects;


import Programming.CBL.Interfaces.SelfMoving;
import Programming.CBL.Panels.GamePanel;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Objects;

/**
 * 
 * Projectile
 * Generic projectile that extends Actor and implements SelfMoving.
*/
public class Projectile extends Actor implements SelfMoving {
    private final Actor origin;
    private Point2D dir = new Point2D.Double(0, 0);
    private boolean alive = true;

    public Projectile(Point2D pos, int width, int height,
                      Point2D dir, Actor origin, int speed, GamePanel panel) {

        super(new Point2D.Double(pos.getX(), pos.getY()), width, height, panel);
        this.origin = Objects.requireNonNull(origin, "origin");
        this.dir = new Point2D.Double(dir.getX(), dir.getY());
        this.speed = speed;
    }

    public Actor getOrigin() {
        return origin;
    }

    @Override
    public Faction getFaction() {
        return origin.getFaction();
    }

    /**
     * @return false if a projectile can hit a targot actor by checking if it has the same origin,
     *  if it is itself, if the faction of either is neutral, and if the faction is the same. 
     * Otherwise return true
     * @param target if the projectile can hit
     */
    public boolean canHit(Actor target) {
        return target != origin
                && target != this
                && getFaction() != Faction.NEUTRAL
                && target.getFaction() != Faction.NEUTRAL
                && getFaction() != target.getFaction();
    }

    /**    (non-Javadoc)
     * 
     * @see Programming.CBL.Objects.Actor#draw(java.awt.Graphics2D)
     */
    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fill(getShape());
    }

    /**    (non-Javadoc)
     * 
     * @see Programming.CBL.Objects.Actor#getShape()
     */
    @Override
    public Shape getShape() {
        return new Rectangle2D.Double(pos.getX(), pos.getY(), width, height);
    }

    /**    
     * 
     * @see Programming.CBL.Interfaces.SelfMoving#update(double)
     */
    @Override
    public boolean update(double dt) {
        if (!alive) {
            return false;
        }

        double dx = this.speed * dir.getX();
        double dy = this.speed * dir.getY();

        this.move(dx, dy, dt);

        Rectangle2D bounds = getShape().getBounds2D();
        return bounds.getMaxX() > 0
                && bounds.getMinX() < panel.getWidth()
                && bounds.getMaxY() > 0
                && bounds.getMinY() < panel.getHeight();
    }

}
