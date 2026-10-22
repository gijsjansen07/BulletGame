package Programming.CBL.Manager;


/**
 * 
 * DifficultyManager holds all the variables that would have an effect 
 * on how difficult the game may be
*/
public class DifficultyManager {    
    // Player variables
    public static final int PLAYER_TIME_BETWEEN_SHOTS = 200;
    public static final int PLAYER_SPEED = 80;
    public static final int PLAYER_SIZE = 10;
    public static final int PLAYER_MAX_INVINCIBILLITY_TIME = 500;
    public static final int PLAYER_MAX_HEALTH = 100;
    public static final int PLAYER_BULLET_SPEED = 300;
    public static final int PLAYER_BULLET_SIZE = 8;

    // Enemy variables
    public static final int ENEMY_SIZE = 20;
    public static final int ENEMY_CAP = 20;
    
    public static double getEnemySpawnInterval(long score) {
        return Math.max(0.25, 1.2 - (score * 0.0002));
    }

    public static double getEnemySpeed(long score) {
        return 40 + Math.max(9, Math.min(5, score * 0.0005));
    }

    public static final int PROJECTILE_SIZE = 14;
    public static final int ENEMY_BULLET_SPEED = 50;

    // Calculate score
    public static long calculateScore(int enemiesKilled, double survivalTime) {
        return enemiesKilled * 80 + Math.round(survivalTime * 50);
    }

    public static double getEnemyShootInterval(long score) {
        return 0.7 - Math.min(score * 0.0005, 0.2);
    }
}
