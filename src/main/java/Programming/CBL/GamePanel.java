package Programming.CBL;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GamePanel extends JPanel implements KeyListener {
    private static final int FRAME_DELAY_MS = 1000 / 60; // ~60fps
    
    // Starts a timer that calls updateGame and repaint ~60/s
    private Timer gameTimer = new Timer(FRAME_DELAY_MS, e -> {
            updateGame();
            repaint();
        });
    private JFrame parentFrame;
    private Player player;
    private long lastUpdateNanos;

    // Stores the held keys
    private final boolean[] keys = new boolean[KeyEvent.KEY_TYPED];


    public GamePanel(JFrame frame) {
        // Panel set up
        this.parentFrame = frame;
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        // Player set up (position is plain data, drawn in paintComponent)
        this.player = new Player("Player", new int[] {225, 450});

        // Start timer
        lastUpdateNanos = System.nanoTime();
        gameTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // Draw call
        player.draw(g2);
        // Flush the display buffer (helps on Linux)
        Toolkit.getDefaultToolkit().sync();
    }

    protected void updateGame() {
        // esc menu
        if (keys[KeyEvent.VK_ESCAPE]) {
            gameTimer.stop();
            Main.showMenu(parentFrame);
            return;
        }

        long now = System.nanoTime();
        double elapsedSeconds = (now - lastUpdateNanos) / 1_000_000_000.0;
        lastUpdateNanos = now;

        int w = getWidth();
        int h = getHeight();

        // Move player according to held keys and elapsed time
        if (keys[KeyEvent.VK_LEFT]) {
            player.moveX(-1, elapsedSeconds, w);
        }
        if (keys[KeyEvent.VK_RIGHT]) {
            player.moveX(1, elapsedSeconds, w);
        }
        if (keys[KeyEvent.VK_UP]) {
            player.moveY(-1, elapsedSeconds, h);
        }
        if (keys[KeyEvent.VK_DOWN]) {
            player.moveY(1, elapsedSeconds, h);
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }
}