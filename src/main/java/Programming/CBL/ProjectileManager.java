package Programming.CBL;

import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * 
 * ProjectileManager
 * This class manages the projectiles on screen.
 * It has a Arraylist of *Projectiles* that it keeps track of.
 * It manages the life cycle of the projectiles and allows the user to check
 * wether an *Actor* is being hit by one or more of the projectiles.  
 */
public class ProjectileManager {
    private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();

    public boolean addProjectile(Projectile projectile) {
        return projectiles.add(projectile);
    }
    
    public void drawProjectiles(Graphics2D g) {
        Iterator<Projectile> it = projectiles.iterator();
        
        while (it.hasNext()) {
            Projectile projectile = it.next();
            projectile.draw(g);
        }
    }

    public void updateProjectiles(double dt) {
        Iterator<Projectile> it = projectiles.iterator();
        
        while (it.hasNext()) {
            Projectile projectile = it.next();
            if (!projectile.update(dt)) {
                it.remove();
            }
        }
    }

    public void updateAndDrawProjectiles(Graphics2D g, double dt) {
        Iterator<Projectile> it = projectiles.iterator();
        
        while (it.hasNext()) {
            Projectile projectile = it.next();
            if (!projectile.update(dt)) {
                it.remove();
            } else {
                projectile.draw(g);
            }
        }
    }

    public Projectile[] hitsByProjectiles(Actor a) {
        ArrayList<Projectile> hits = new ArrayList<>();

        for (Projectile projectile : projectiles) {
            if (projectile.hits(a)) {
                hits.add(projectile);
            }
        }
        return hits.toArray(new Projectile[0]);
    }

}
