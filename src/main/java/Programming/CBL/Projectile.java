package Programming.CBL;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.Objects;

public class Projectile extends Actor implements SelfMoving {
    private final Actor origin;
    private double xDir; 
    private double yDir; 

    public Projectile(double x, double y, int width, int height, double xDir,
                      double yDir, Actor origin, GamePanel panel) {
        super(x, y, width, height, panel);
        this.origin = Objects.requireNonNull(origin, "origin");
        this.xDir = xDir;
        this.yDir = yDir;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fill(getShape());
    }

    @Override
    public Shape getShape() {
        return new Rectangle2D.Double(x, y, width, height);
    }

    /**    
     * 
     * @see Programming.CBL.SelfMoving#update(double)
     */
    @Override
    public boolean update(double dt) {
        double dx = this.speed * xDir;
        double dy = this.speed * yDir;

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
        return !overlap.isEmpty();
    }

    public void kill() {};

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
