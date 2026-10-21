package Programming.CBL.Panels;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import Programming.CBL.Main;
import Programming.CBL.Manager.EnemyManager;
import Programming.CBL.Manager.HighScoresManager;
import Programming.CBL.Manager.ProjectileManager;
import Programming.CBL.Objects.Enemy;
import Programming.CBL.Objects.Actor;
import Programming.CBL.Objects.Player;
import Programming.CBL.Objects.ScoreObject;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel implements KeyListener {
    private static final int FRAME_DELAY_MS = 1000 / 60; // Target ~60 FPS

    private JFrame parentFrame;
    private Player player;
    private long lastUpdateNanos;

    private final boolean[] keys = new boolean[KeyEvent.KEY_TYPED];

    // Managers and renderers
    private EnemyManager enemyManager = new EnemyManager(this);
    private ProjectileManager projectileManager = new ProjectileManager();
    private TextElimination textElimination = new TextElimination();


    private double survivalTime = 0.0;
    private boolean gameOver = false;
    private JButton retryButton;
    private ScoreObject finalScore;

    private Timer gameTimer = new Timer(FRAME_DELAY_MS, e -> {
        updateGame();
        repaint();
    });

    public GamePanel(JFrame frame) {
        this.parentFrame = frame;
        setLayout(null);
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        // Add rety button to panel
        retryButton = new JButton("Try Again");
        retryButton.setBounds(185, 340, 130, 40);
        retryButton.setFocusable(false);
        retryButton.setVisible(false);
        retryButton.addActionListener(e -> restartGame());
        add(retryButton);

        // Start first game
        initGame();
    }

    private void initGame() {
        // Clears the previous games data
        this.player = new Player("Player", new Point2D.Double(225, 450), this);
        this.enemyManager.reset();
        this.projectileManager.reset();
        this.survivalTime = 0.0;
        this.lastUpdateNanos = System.nanoTime();
        this.gameOver = false;
        this.finalScore = null;


        //Start game
        gameTimer.start();
    }

    private void restartGame() {
        // Hide retry buttion
        retryButton.setVisible(false);
        requestFocusInWindow();
        // Start game
        initGame();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        // Turns on anti aliasing for subpixel movement.
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Render entities
        player.draw(g2);
        enemyManager.drawEnemies(g2);
        projectileManager.drawProjectiles(g2);

        // HUD / Survival score
        drawHUD(g2);

        // Render elimination screen if gameOver
        if (gameOver) {
            textElimination.draw(g2, getWidth(), getHeight(), finalScore.getScore());
        }

        Toolkit.getDefaultToolkit().sync();
    }

    protected void updateGame() {
        // Return to main menu on ESC
        if (keys[KeyEvent.VK_ESCAPE]) {
            gameTimer.stop();
            Main.showMenu(parentFrame);
            return;
        }

        // dt
        long now = System.nanoTime();
        double dt = (now - lastUpdateNanos) / 1_000_000_000.0;
        lastUpdateNanos = now;
        
        Point2D mousePos = getMousePos();
        Point2D playerDirVec = new Point2D.Double(mousePos.getX() - player.getX(),
                                       mousePos.getY() - player.getY());

        // Update player position
        int dx = (keys[KeyEvent.VK_RIGHT] ? 1 : 0) - (keys[KeyEvent.VK_LEFT] ? 1 : 0);
        int dy = (keys[KeyEvent.VK_DOWN] ? 1 : 0) - (keys[KeyEvent.VK_UP] ? 1 : 0);

        // Move all entities
        player.move(dx, dy, dt);
        enemyManager.updateEnemies(dt, survivalTime);
        projectileManager.updateProjectiles(dt);

        // Check if player hits an enemy directly
        if (!enemyManager.hitEnemies(player).isEmpty()) {
            // If so immediatly end game
            player.reduceHealth(player.getHealth());
            finishGameIfPlayerIsDead();
            if (gameOver) {
                return;
            }
        }

        // Get all the possible targets for projectile collision (Enemies and player)
        List<Actor> collisionTargets = new ArrayList<>(enemyManager.getEnemies());
        collisionTargets.add(player);
        // See if any of the targets get hit by a projectile
        ArrayList<Actor> hitTargets = projectileManager.resolveCollisions(collisionTargets);
        // Apply aproptiate consequences
        for (Actor target : hitTargets) {
            // Check if the targets instance is of the Enemy type and remove the enemy
            if (target instanceof Enemy) {
                enemyManager.remove((Enemy) target);
            } else if (target == player) {
                // If the target is the player take reduce health if no I frames.
                Instant nowInstant = Instant.now();
                Duration timeSinceLastHit = Duration.between(player.getLastHit(), nowInstant);
                if (timeSinceLastHit.toMillis() >= Player.getMaxInvincibillityTime()) {
                    player.reduceHealth(1);
                    player.setLastHit(nowInstant);
                    finishGameIfPlayerIsDead();
                }
            }
        }

        if (gameOver) {
            return;
        }

        // Shoot new player projectiles
        player.shootProjectile(playerDirVec, dt, projectileManager);

        survivalTime += dt;
    }

    /**
     * checks if player health is 0 and stops game.
     * 
    */
    private void finishGameIfPlayerIsDead() {
        if (player.getHealth() == 0) {
            gameOver = true;
            finalScore = new ScoreObject(LocalDateTime.now(),
                                         player.getName(),
                                         (long) survivalTime * 100);
            HighScoresManager.addScore(finalScore);
            gameTimer.stop();
            retryButton.setVisible(true);
        }
    }

    public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() < keys.length) {
            keys[e.getKeyCode()] = true;
        }
        if (gameOver && (e.getKeyCode() == KeyEvent.VK_R || e.getKeyCode() == KeyEvent.VK_ENTER)) {
            restartGame();
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() < keys.length) {
            keys[e.getKeyCode()] = false;
        }
    }

    private Point2D getMousePos() {
        Point mouse = MouseInfo.getPointerInfo().getLocation();
        Point panellocation = this.getLocationOnScreen();
        return new Point2D.Double(mouse.x - panellocation.x,
                                                   mouse.y - panellocation.y);
    }

    private void drawHUD(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        g2.drawString(String.format("Score: %d s", (int) survivalTime), 15, 25);

    }
}