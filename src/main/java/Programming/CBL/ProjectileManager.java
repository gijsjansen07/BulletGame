package Programming.CBL;

import java.awt.Graphics2D;
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

        System.out.println(projectiles.size());
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
        System.out.println(projectiles.size());
    }

    public Projectile[] removeHitProjectiles(Actor a) {
        ArrayList<Projectile> hits = new ArrayList<>();
        Iterator<Projectile> it = projectiles.iterator();

        while (it.hasNext()) {
            Projectile projectile = it.next();
            if (projectile.hits(a)) {
                hits.add(projectile);
                it.remove();
            }
        }
        return hits.toArray(new Projectile[0]);
    }

}
