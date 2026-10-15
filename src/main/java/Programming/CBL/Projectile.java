package Programming.CBL;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Objects;

public class Projectile extends Actor implements SelfMoving {
    
    private final Actor origin;
    private Point2D dir = new Point2D.Double(0, 0);

    public Projectile(Point2D pos, int width, int height,
                      Point2D dir, Actor origin, int speed,GamePanel panel) {

        super(new Point2D.Double(pos.getX(), pos.getY()), width, height, panel);
        this.origin = Objects.requireNonNull(origin, "origin");
        this.dir = new Point2D.Double(dir.getX(), dir.getY());
        this.speed = speed;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fill(getShape());
    }

    @Override
    public Shape getShape() {
        return new Rectangle2D.Double(pos.getX(), pos.getY(), width, height);
    }

    /**    
     * 
     * @see Programming.CBL.SelfMoving#update(double)
     */
    @Override
    public boolean update(double dt) {
        double dx = this.speed * dir.getX();
        double dy = this.speed * dir.getY();

        this.move(dx, dy, dt);

        Rectangle2D bounds = getShape().getBounds2D();
        return bounds.getMaxX() > 0
                && bounds.getMinX() < panel.getWidth()
                && bounds.getMaxY() > 0
                && bounds.getMinY() < panel.getHeight();
    }

    public boolean hits(Actor a) {
        if (this == a || this.origin == a) {
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
        this.kill();
        return true;
    }

    public void kill() {}
    
}
