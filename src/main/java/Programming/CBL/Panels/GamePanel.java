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
import Programming.CBL.Objects.Player;
import Programming.CBL.Objects.Projectile;
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

/*
    When the game is over currently the timer gets stopped and the remaining call is finished. 


*/




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
            ScoreObject score = new ScoreObject(LocalDateTime.now(),
                                                player.getName(),
                                                (long) survivalTime * 100);
            HighScoresManager.addScore(score);
            textElimination.draw(g2, getWidth(), getHeight(), score.getScore());
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
        

        int w = getWidth();
        int h = getHeight();

        Point2D mousePos = getMousePos();
        Point2D playerDirVec = new Point2D.Double(mousePos.getX() - player.getX(),
                                       mousePos.getY() - player.getY());

        // Update player position
        int dx = (keys[KeyEvent.VK_RIGHT] ? 1 : 0) - (keys[KeyEvent.VK_LEFT] ? 1 : 0);
        int dy = (keys[KeyEvent.VK_DOWN] ? 1 : 0) - (keys[KeyEvent.VK_UP] ? 1 : 0);

        player.move(dx, dy, dt);

        enemyManager.updateEnemies(dt, survivalTime);
        projectileManager.updateProjectiles(dt);

        ArrayList<Enemy> hEnemies = enemyManager.hitEnemies(player);
        ArrayList<Projectile> hProjectiles = projectileManager.hitProjectiles(player);

        if (hEnemies.size() > 0) {
            player.reduceHealth(player.getHealth());
        }
        
        if (hProjectiles.size() > 0) {
            Instant nowInstant = Instant.now();
            Instant lastHit = player.getLastHit();
            Duration timeBetweenHits = Duration.between(lastHit, nowInstant); 
            if (timeBetweenHits.toMillis() < Player.getMaxInvincibillityTime()) {
                player.reduceHealth(1);
                player.setLastHit(nowInstant);
            }
        }

        for (Enemy enemy : enemyManager.getEnemies()) {
            ArrayList<Projectile> projectilesThatHitEnemy = projectileManager.hitProjectiles(enemy);
            for (Projectile projectile : projectilesThatHitEnemy) {
                if (projectile.getOrigin().getClass() == player.getClass()) {
                    enemyManager.remove(enemy);
                }
            }
        }
        player.shootProjectile(playerDirVec, dt, projectileManager);
        projectileManager.removeHitProjectiles(player);

        survivalTime += dt;
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