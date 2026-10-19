package Programming.CBL;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Main game panel handling loop updates, input routing, and top-level rendering delegation.
 */
public class GamePanel extends JPanel implements KeyListener {
    private static final int FRAME_DELAY_MS = 1000 / 60; // Target ~60 FPS
    
    private Timer gameTimer = new Timer(FRAME_DELAY_MS, e -> {
        updateGame();
        repaint();
    });

    private JFrame parentFrame;
    private Player player;
    private long lastUpdateNanos;

    private final boolean[] keys = new boolean[KeyEvent.KEY_TYPED];

    // Managers and renderers
    private EnemyManager enemyManager = new EnemyManager();
    private TextElimination textElimination = new TextElimination();

    private double survivalTime = 0.0;
    private boolean gameOver = false;
    private JButton retryButton;

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
        this.enemyManager.reset();
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

        // Render entities
        player.draw(g2);
        enemyManager.draw(g2);

        // HUD / Survival score
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        g2.drawString(String.format("Score: %d s", (int) survivalTime), 15, 25);

        // Render elimination screen if hit
        if (gameOver) {
            textElimination.draw(g2, getWidth(), getHeight(), survivalTime);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    protected void updateGame() {
        if (gameOver) {
            return;
        }

        // Return to main menu on ESC
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

        // Update player position
        int dx = (keys[KeyEvent.VK_RIGHT] ? 1 : 0) - (keys[KeyEvent.VK_LEFT] ? 1 : 0);
        int dy = (keys[KeyEvent.VK_DOWN] ? 1 : 0) - (keys[KeyEvent.VK_UP] ? 1 : 0);
        player.move(dx, dy, elapsedSeconds, w, h);

        // Delegate enemy updates, spawns, and collision checks
        boolean playerHit = enemyManager.update(elapsedSeconds, w, h, survivalTime, player);
        if (playerHit) {
            gameOver = true;
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
}