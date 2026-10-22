package Programming.CBL;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * Manages the collection of active enemies on screen.
 * Handles procedural spawning, shooting routines, updates, rendering, and collisions.
 */
public class EnemyManager {
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private Random random = new Random();
    private double spawnTimer = 0.0;

    private GamePanel panel;

    public EnemyManager(GamePanel panel) {
        this.panel = panel;
    }

    public void reset() {
        enemies.clear();
        spawnTimer = 0.0;
    }

    /**
     * Updates enemy positions, triggers shots towards the player, spawns new enemies,
     * cleans off-screen entities, and checks for direct collisions.
     */
    public boolean update(double elapsedSeconds, int screenWidth, int screenHeight,
                          double survivalTime, Player player, ProjectileManager pManager) {
        double currentSpawnInterval = Math.max(0.25, 1.2 - (survivalTime * 0.02));
        spawnTimer += elapsedSeconds;

        if (spawnTimer >= currentSpawnInterval) {
            spawnTimer = 0.0;
            spawnEnemy(screenWidth, screenHeight, survivalTime);
        }

        Iterator<Enemy> it = enemies.iterator();
        while (it.hasNext()) {
            Enemy enemy = it.next();
            enemy.update(elapsedSeconds);

            // Enemies shoot towards the player's position
            enemy.shootAt(new Point2D.Double(player.getX(), player.getY()), elapsedSeconds, pManager);

            // Remove enemy once outside view bounds
            if (enemy.isOffScreen(screenWidth, screenHeight)) {
                it.remove();
                continue;
            }

            // Direct collision check against the player body
            if (enemy.hits(player)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if a player's projectile hits any active enemy.
     * Enemy projectiles are ignored so enemies never hurt themselves.
     */
    public void enemyHits(Projectile projectile) {
        if (projectile instanceof EnemyProjectile || projectile.getOrigin() instanceof Enemy) {
            return;
        }

        Iterator<Enemy> it = enemies.iterator();
        while (it.hasNext()) {
            Enemy enemy = it.next();
            if (projectile.hits(enemy)) {
                it.remove();
                break;
            }
        }
    }

    public void draw(Graphics2D g2) {
        for (Enemy enemy : enemies) {
            enemy.draw(g2);
        }
    }

    private void spawnEnemy(int screenWidth, int screenHeight, double survivalTime) {
        if (screenWidth <= 0 || screenHeight <= 0) {
            return;
        }

        int side = random.nextInt(4);
        double startX = 0;
        double startY = 0;

        switch (side) {
            case 0: // Top
                startX = random.nextInt(screenWidth);
                startY = -25;
                break;
            case 1: // Bottom
                startX = random.nextInt(screenWidth);
                startY = screenHeight + 25;
                break;
            case 2: // Left
                startX = -25;
                startY = random.nextInt(screenHeight);
                break;
            case 3: // Right
                startX = screenWidth + 25;
                startY = random.nextInt(screenHeight);
                break;
            default:
                break;
        }

        double targetX = 50 + random.nextInt(Math.max(1, screenWidth - 100));
        double targetY = 50 + random.nextInt(Math.max(1, screenHeight - 100));

        double dirX = targetX - startX;
        double dirY = targetY - startY;
        double length = Math.hypot(dirX, dirY);

        if (length > 0) {
            dirX /= length;
            dirY /= length;
        }

        double speed = 110 + random.nextInt(60) + Math.min(80, survivalTime * 1.5);
        double speedX = dirX * speed;
        double speedY = dirY * speed;

        Point2D startPos = new Point2D.Double(startX, startY);
        enemies.add(new Enemy(startPos, speedX, speedY, this.panel));
    }
}