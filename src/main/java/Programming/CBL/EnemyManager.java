package Programming.CBL;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * Manages the collection of active enemies on screen.
 * Handles procedural spawning, boundary checks, updates, rendering,
 * and radial collision detection against an actor.
 */
public class EnemyManager {
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private Random random = new Random();
    private double spawnTimer = 0.0;

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
     * @param screenWidth Current play area width.
     * @param screenHeight Current play area height.
     * @param survivalTime Total elapsed game time used to scale difficulty.
     * @param player The target player actor to verify collisions against.
     * @return True if any enemy collides with the player, false otherwise.
     */
    public boolean update(double elapsedSeconds, int screenWidth, int screenHeight,
                          double survivalTime, Player player) {
        // Dynamic spawn frequency: interval shrinks as survivalTime increases
        double currentSpawnInterval = Math.max(0.25, 1.2 - (survivalTime * 0.02));
        spawnTimer += elapsedSeconds;

        if (spawnTimer >= currentSpawnInterval) {
            spawnTimer = 0.0;
            spawnEnemy(screenWidth, screenHeight, survivalTime);
        }

        // Iterate over active enemies for updates, cleanup, and collisions
        Iterator<Enemy> it = enemies.iterator();
        while (it.hasNext()) {
            Enemy enemy = it.next();
            enemy.update(elapsedSeconds);

            // Remove enemy once outside view bounds
            if (enemy.isOffScreen(screenWidth, screenHeight)) {
                it.remove();
                continue;
            }

            // Radial collision check against the player
            if (checkCollision(player, enemy)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Draws all active enemies onto the graphics context.
     *
     * @param g2 Graphics2D instance.
     */
    public void draw(Graphics2D g2) {
        for (Enemy enemy : enemies) {
            enemy.draw(g2);
        }
    }

    /**
     * Spawns an enemy along one of the 4 borders directed towards the map center.
     */
    private void spawnEnemy(int screenWidth, int screenHeight, double survivalTime) {
        if (screenWidth <= 0 || screenHeight <= 0) {
            return;
        }

        // 0: Top, 1: Bottom, 2: Left, 3: Right
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

        // Pick a target coordinate to calculate directional vector
        double targetX = 50 + random.nextInt(Math.max(1, screenWidth - 100));
        double targetY = 50 + random.nextInt(Math.max(1, screenHeight - 100));

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

        enemies.add(new Enemy(startX, startY, speedX, speedY));
    }

    /**
     * Radial Euclidean distance check between two circular bounding circles.
     */
    private boolean checkCollision(Player p, Enemy e) {
        double pCenterX = p.getX() + p.getSize() / 2.0;
        double pCenterY = p.getY() + p.getSize() / 2.0;
        double eCenterX = e.getX() + e.getWidth() / 2.0;
        double eCenterY = e.getY() + e.getHeight() / 2.0;

        double distance = Math.hypot(pCenterX - eCenterX, pCenterY - eCenterY);
        double minDistance = (p.getSize() / 2.0) + (e.getWidth() / 2.0);

        return distance < minDistance;
    }
}