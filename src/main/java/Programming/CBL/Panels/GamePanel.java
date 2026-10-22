package Programming.CBL.Panels;


import Programming.CBL.Main;
import Programming.CBL.Manager.DifficultyManager;
import Programming.CBL.Manager.EnemyManager;
import Programming.CBL.Manager.HighScoresManager;
import Programming.CBL.Manager.ProjectileManager;
import Programming.CBL.Objects.Actor;
import Programming.CBL.Objects.Enemy;
import Programming.CBL.Objects.Player;
import Programming.CBL.Objects.ScoreObject;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.Timer;


public class GamePanel extends JPanel implements KeyListener {
    private static final int FRAME_DELAY_MS = 1000 / 60; // Target 60 FPS
    private long lastUpdateNanos;

    private final boolean[] keys = new boolean[KeyEvent.KEY_TYPED];

    // Managers and renderers
    private EnemyManager enemyManager = new EnemyManager(this);
    private ProjectileManager projectileManager = new ProjectileManager();

    private Player player;

    // Game State
    private boolean gameOver = false;
    private boolean paused;
    private double survivalTime = 0.0;
    private int enemiesKilled = 0;
    private long score = 0;

    // Rendering objects
    private JButton retryButton;
    private JTextField textField;
    private JScrollPane highScores;
    private ScoreObject finalScore;

    // Game loop
    private Timer gameTimer = new Timer(FRAME_DELAY_MS, e -> {
        updateGame();
        repaint();
    });

    public GamePanel() {
        setLayout(null);
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        // Add highscores panel
        highScores = HighScoresPanel.createScoreList();
        highScores.setBounds(50, 100, 400, 220);
        highScores.setVisible(false);
        add(highScores);

        // Add rety button to panel
        retryButton = new JButton("Try Again");
        retryButton.setBounds(185, 340, 130, 40);
        retryButton.setFocusable(false);
        retryButton.setVisible(false);
        retryButton.addActionListener(e -> restartGame());
        add(retryButton);

        // Add name input Field
        textField = new JTextField();
        textField.setBounds(185, 300, 130, 30);
        textField.addActionListener(e -> {
            String name = textField.getText().trim();

            if (!name.isEmpty()) {
                playerName = name;
                textField.setVisible(false);
                initGame();
            }
        });
        add(textField);
        repaint();
    }

    private void initGame() {
        player = new Player(playerName, new Point2D.Double(225, 450), this);
        this.enemyManager.reset();
        this.projectileManager.reset();
        this.score = 0;
        this.enemiesKilled = 0;
        this.survivalTime = 0.0;
        this.lastUpdateNanos = System.nanoTime();
        this.gameOver = false;
        this.finalScore = null;

        //Start game
        gameTimer.start();
    }

    private void restartGame() {
        playerName = null;
        retryButton.setVisible(false);
        highScores.setVisible(false);
        textField.setText("");
        textField.setVisible(true);
        textField.requestFocusInWindow();
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (playerName == null) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        // Turns on anti aliasing for subpixel movement.
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Render entities
        player.draw(g2);
        enemyManager.drawEnemies(g2);
        projectileManager.drawProjectiles(g2);

        // HUD / Survival score
        GamePanelHudElements.drawHUD(score, player.getHealth(), g2);

        // Render elimination screen if gameOver
        if (gameOver) {
            GamePanelHudElements.drawGameOverScreen(g2, getWidth(),
                                                    getHeight(), finalScore.getScore());
        } else if (paused) {
            GamePanelHudElements.drawPausedScreen(g2, getWidth(), getHeight());
        }

        Toolkit.getDefaultToolkit().sync();
    }

    protected void updateGame() {
        // dt
        long now = System.nanoTime();
        double dt = (now - lastUpdateNanos) / 1_000_000_000.0;
        lastUpdateNanos = now;
        
        Point2D mousePos = getMousePos();
        Point2D playerDirVec = new Point2D.Double(mousePos.getX() - player.getX(),
                                       mousePos.getY() - player.getY());

        // Update player position
        Point2D dpos = getDeltaPlayerPosition(); 
        // Move all entities
        player.move(dpos.getX(), dpos.getY(), dt);
        enemyManager.updateEnemies(player, projectileManager, dt, score);
        projectileManager.updateProjectiles(dt);

        playerHitsEnemy();
        if (gameOver) {
            return;
        }

        resolveProjectileCollisions();
        if (gameOver) {
            return;
        }

        // Shoot new player projectiles
        player.shootProjectile(playerDirVec, dt, projectileManager);

        survivalTime += dt;
        score = DifficultyManager.calculateScore(enemiesKilled, survivalTime);
    }

    private Point2D getDeltaPlayerPosition() {        
        int dx = (keys[KeyEvent.VK_RIGHT] ? 1 : 0) - (keys[KeyEvent.VK_LEFT] ? 1 : 0);
        int dy = (keys[KeyEvent.VK_DOWN] ? 1 : 0) - (keys[KeyEvent.VK_UP] ? 1 : 0);
        return new Point(dx, dy);
    }

    /**
     * Resolves the collisions between all projectiles and possible targets on screen.
     */
    private void resolveProjectileCollisions() {
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
                enemiesKilled += 1;
            } else if (target == player) {
                // If the target is the player take reduce health if no I frames.
                Instant nowInstant = Instant.now();
                Duration timeSinceLastHit = Duration.between(player.getLastHit(), nowInstant);
                
                if (timeSinceLastHit.toMillis() >= Player.getMaxInvincibillityTime()) {
                    player.reduceHealth(10);
                    player.setLastHit(nowInstant);
                    finishGameIfPlayerIsDead();
                }
            }
        }
    }

    /** 
     * Check if player hits an enemy directly
     * 
    */
    private void playerHitsEnemy() {
        if (!enemyManager.hitEnemies(player).isEmpty()) {
            // If so immediatly end game
            player.reduceHealth(player.getHealth());
            finishGameIfPlayerIsDead();
        }
    }

    /**
     * checks if player health is 0 and stops game.
     * 
    */
    private void finishGameIfPlayerIsDead() {
        if (player.getHealth() == 0) {
            if (gameOver) {
                return;
            }
            gameOver = true;
            finalScore = new ScoreObject(LocalDateTime.now(),
                                         player.getName(),
                                         score);
            HighScoresManager.addScore(finalScore);
            gameTimer.stop();
            textField.setVisible(false);

            // Highscores & retry button
            remove(highScores);
            retryButton.setVisible(true);
            highScores = HighScoresPanel.createScoreList();

            int listY = getHeight() / 3;
            int listHeight = getHeight() / 2;
            highScores.setBounds(50, listY, getWidth() - 100, listHeight);
            add(highScores);
            highScores.setVisible(true);
            retryButton.setBounds((getWidth() - 130) / 2, getHeight() - 55, 130, 40);
            revalidate();
            repaint();
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

        // Pauze
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (gameTimer.isRunning() && !gameOver) {
                paused = true;
                gameTimer.stop();
            } else if (!gameOver) {
                lastUpdateNanos = System.nanoTime();
                paused = false;
                gameTimer.start();
            }
            repaint();
            return;
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

    private String playerName = null;

}