package Programming.CBL.Manager;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import Programming.CBL.Objects.Actor;
import Programming.CBL.Objects.Enemy;
import Programming.CBL.Objects.Player;
import Programming.CBL.Objects.Projectile;
import Programming.CBL.Panels.GamePanel;

/**
 * Manages the collection of active enemies on screen.
 * Handles procedural spawning, boundary checks, updates, rendering,
 * and radial collision detection against an actor.
 */
public class EnemyManager {
    private ArrayList<Enemy> enemies = new ArrayList<>();

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    private Random random = new Random();
    private double spawnTimer = 0.0;

    private GamePanel panel;

    public EnemyManager(GamePanel panel) {
        super();
        this.panel = panel;
    }

    /**
     * Resets the active enemies and the spawn clock.
     */
    public void reset() {
        enemies.clear();
        spawnTimer = 0.0;
    }

    /**
     * Updates enemy positions, scales dynamic difficulty, spawns new enemies,
     * removes off-screen entities, and checks for collisions against the player.
     *
     * @param elapsedSeconds Time delta since last update.
     * @param panel.getWidth() Current play area width.
     * @param panel.getHeight() Current play area height.
     * @param survivalTime Total elapsed game time used to scale difficulty.
     * @param player The target player actor to verify collisions against.
     * @return True if any enemy collides with the player, false otherwise.
     */
    public void updateEnemies(double elapsedSeconds, double survivalTime) {
        // Dynamic spawn frequency: interval shrinks as survivalTime increases
        double currentSpawnInterval = Math.max(0.25, 1.2 - (survivalTime * 0.02));
        spawnTimer += elapsedSeconds;

        if (spawnTimer >= currentSpawnInterval) {
            spawnTimer = 0.0;
            spawnEnemy(survivalTime);
        }

        // Iterate over active enemies for updates, cleanup, and collisions
        Iterator<Enemy> it = enemies.iterator();
        while (it.hasNext()) {
            Enemy enemy = it.next();
            enemy.update(elapsedSeconds);

            // Remove enemy once outside view bounds
            if (enemy.isOffScreen()) {
                it.remove();
                continue;
            }
        }
    }

    public ArrayList<Enemy> hitEnemies(Actor a) {
        ArrayList<Enemy> hits = new ArrayList<Enemy>();
        Iterator<Enemy> it = enemies.iterator();
        while (it.hasNext()) {
            Enemy enemy = it.next();
            if (enemy.hits(a)) {
                hits.add(enemy);
            }
        }
        return hits;
    }

    /**
     * Draws all active enemies onto the graphics context.
     *
     * @param g2 Graphics2D instance.
     */
    public void drawEnemies(Graphics2D g2) {
        for (Enemy enemy : enemies) {
            enemy.draw(g2);
        }
    }

    /**
     * Spawns an enemy along one of the 4 borders directed towards the map center.
     */
    private void spawnEnemy(double survivalTime) {
        if (panel.getWidth() <= 0 || panel.getHeight() <= 0) {
            return;
        }

        // 0: Top, 1: Bottom, 2: Left, 3: Right
        int side = random.nextInt(4);
        double startX = 0;
        double startY = 0;

        switch (side) {
            case 0: // Top
                startX = random.nextInt(panel.getWidth());
                startY = -25;
                break;
            case 1: // Bottom
                startX = random.nextInt(panel.getWidth());
                startY = panel.getHeight() + 25;
                break;
            case 2: // Left
                startX = -25;
                startY = random.nextInt(panel.getHeight());
                break;
            case 3: // Right
                startX = panel.getWidth() + 25;
                startY = random.nextInt(panel.getHeight());
                break;
            default:
                break;
        }

        // Pick a target coordinate to calculate directional vector
        double targetX = 50 + random.nextInt(Math.max(1, panel.getWidth() - 100));
        double targetY = 50 + random.nextInt(Math.max(1, panel.getHeight() - 100));

        double dirX = targetX - startX;
        double dirY = targetY - startY;
        double length = Math.hypot(dirX, dirY);

        if (length > 0) {
            dirX /= length;
            dirY /= length;
        }

        // Speed increases slightly over time
        double speed = 110 + random.nextInt(60) + Math.min(80, survivalTime * 1.5);
        double speedX = dirX * speed;
        double speedY = dirY * speed;

        Point2D startPos = new Point2D.Double(startX, startY);

        enemies.add(new Enemy(startPos, speedX, speedY, this.panel));
    }
}