package Programming.CBL.Manager;


import Programming.CBL.Objects.Actor;
import Programming.CBL.Objects.Projectile;
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

    public void reset() {
        this.projectiles.clear();
    }

    public ArrayList<Projectile> getProjectiles() {
        return projectiles;
    }

    public boolean addProjectile(Projectile projectile) {
        return projectiles.add(projectile);
    }
    
    public void removeProjeciles(ArrayList<Projectile> projectiles) {
        this.projectiles.removeAll(projectiles);
    }

    /**
     * Draws the projectiles in the active projectile list 
     * @param g graphics2D object to draw on.
     */
    public void drawProjectiles(Graphics2D g) {
        Iterator<Projectile> it = projectiles.iterator();
        
        while (it.hasNext()) {
            Projectile projectile = it.next();
            projectile.draw(g);
        }
    }

    /**
     * Updates and moves the projectiles according to the passed delta time
     * @param dt delta time
     */
    public void updateProjectiles(double dt) {
        Iterator<Projectile> it = projectiles.iterator();
        while (it.hasNext()) {
            Projectile projectile = it.next();
            if (!projectile.update(dt)) {
                it.remove();
            }
        }
    }

    /**
     * @see Programming.CBL.Manager.ProjectileManager#updateProjectiles(double)
     * @see Programming.CBL.Manager.ProjectileManager#drawProjectiles(Graphics2D)
     * @param g
     * @param dt
    */
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

    /***
     * removes the hit projectiles by Actor a. 
     * @see Programming.CBL.Objects.Projectile#hits(Actor)
     * @param a
     * @return array of projectiles that hit Actor a
    */
    public ArrayList<Projectile> hitProjectiles(Actor a) {
        ArrayList<Projectile> hits = new ArrayList<>();
        Iterator<Projectile> it = projectiles.iterator();

        while (it.hasNext()) {
            Projectile projectile = it.next();
            if (projectile.hits(a)) {
                hits.add(projectile);
            }
        }
        return hits;
    }

}
