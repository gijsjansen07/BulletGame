package Programming.CBL;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import javax.swing.*;

/**
 * Main game panel handling rendering, loop updates, progressive enemy spawning
 * across all four window borders, collision detection, and score/game over overlay.
 */
public class GamePanel extends JPanel implements KeyListener {
    private static final int FRAME_DELAY_MS = 1000 / 60; // Target ~60 FPS
    
    // Core game loop timer triggering game updates and screen repainting
    private Timer gameTimer = new Timer(FRAME_DELAY_MS, e -> {
        updateGame();
        repaint();
    });

    private JFrame parentFrame;
    private Player player;
    private long lastUpdateNanos;

    // Buffer tracking active key presses
    private final boolean[] keys = new boolean[KeyEvent.KEY_TYPED];

    // Enemy lifecycle and game state tracking
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private Random random = new Random();
    private double spawnTimer = 0.0;
    private double survivalTime = 0.0;
    private boolean gameOver = false;
    private JButton retryButton;

    // Renderer for the elimination screen
    private TextElimination textElimination = new TextElimination();

    public GamePanel(JFrame frame) {
        this.parentFrame = frame;
        setLayout(null);
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        initGame();

        retryButton = new JButton("Try Again");
        retryButton.setBounds(185, 340, 130, 40);
        retryButton.setFocusable(false);
        retryButton.setVisible(false);
        retryButton.addActionListener(e -> restartGame());
        add(retryButton);

        lastUpdateNanos = System.nanoTime();
        gameTimer.start();
    }

    private void initGame() {
        this.player = new Player("Player", new int[] {225, 450});
        this.enemies.clear();
        this.spawnTimer = 0.0;
        this.survivalTime = 0.0;
        this.gameOver = false;
    }

    private void restartGame() {
        initGame();
        retryButton.setVisible(false);
        lastUpdateNanos = System.nanoTime();
        gameTimer.start();
        requestFocusInWindow();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // draw enwmies
        player.draw(g2);
        for (Enemy enemy : enemies) {
            enemy.draw(g2);
        }

        // score
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        g2.drawString(String.format("Score: %d s", (int) survivalTime), 15, 25);

        // call text eliminator
        if (gameOver) {
            textElimination.draw(g2, getWidth(), getHeight(), survivalTime);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    protected void updateGame() {
        if (gameOver) {
            return;
        }

        if (keys[KeyEvent.VK_ESCAPE]) {
            gameTimer.stop();
            Main.showMenu(parentFrame);
            return;
        }

        long now = System.nanoTime();
        double elapsedSeconds = (now - lastUpdateNanos) / 1_000_000_000.0;
        lastUpdateNanos = now;

        survivalTime += elapsedSeconds;

        int w = getWidth();
        int h = getHeight();

        int dx = (keys[KeyEvent.VK_RIGHT] ? 1 : 0) - (keys[KeyEvent.VK_LEFT] ? 1 : 0);
        int dy = (keys[KeyEvent.VK_DOWN] ? 1 : 0) - (keys[KeyEvent.VK_UP] ? 1 : 0);
        player.move(dx, dy, elapsedSeconds, w, h);

        double currentSpawnInterval = Math.max(0.25, 1.2 - (survivalTime * 0.02));

        spawnTimer += elapsedSeconds;
        if (spawnTimer >= currentSpawnInterval) {
            spawnTimer = 0.0;
            spawnEnemy(w, h);
        }

        Iterator<Enemy> it = enemies.iterator();
        while (it.hasNext()) {
            Enemy enemy = it.next();
            enemy.update(elapsedSeconds);

            if (enemy.isOffScreen(w, h)) {
                it.remove();
                continue;
            }

            if (checkCollision(player, enemy)) {
                gameOver = true;
                gameTimer.stop();
                retryButton.setVisible(true);
                break;
            }
        }
    }

    private void spawnEnemy(int screenWidth, int screenHeight) {
        if (screenWidth <= 0 || screenHeight <= 0) return;

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

        enemies.add(new Enemy(startX, startY, speedX, speedY));
    }

    private boolean checkCollision(Player p, Enemy e) {
        double pCenterX = p.getX() + p.getSize() / 2.0;
        double pCenterY = p.getY() + p.getSize() / 2.0;
        double eCenterX = e.getX() + e.getWidth() / 2.0;
        double eCenterY = e.getY() + e.getHeight() / 2.0;

        double distance = Math.hypot(pCenterX - eCenterX, pCenterY - eCenterY);
        double minDistance = (p.getSize() / 2.0) + (e.getWidth() / 2.0);

        return distance < minDistance;
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
}