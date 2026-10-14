package Programming.CBL;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

public class Projectile extends Actor implements SelfMoving {
    public Actor origin;
    private double xDir; 
    private double yDir; 

    public Projectile(double x, double y, int width, int height, double xDir,
                      double yDir, GamePanel panel) {
        this(x, y, width, height, xDir, yDir, null, panel);
    }

    public Projectile(double x, double y, int width, int height, double xDir,
                      double yDir, Actor origin, GamePanel panel) {
        super(x, y, width, height, panel);
        this.origin = origin;
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

        if (this.x + this.width < 0 || this.x > this.panel.getWidth()) {
            return false;
        }
        return true;
    }

    public boolean hits(Actor a) {
        Area overlap = new Area(a.getShape());
        overlap.intersect(new Area(this.getShape()));
        if (this == a) {
            return false; 
        }
        if (this.origin == null) {
            return false; 
        }
        if (this.origin == a) {
            return false; 
        }
        return !overlap.isEmpty();
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
