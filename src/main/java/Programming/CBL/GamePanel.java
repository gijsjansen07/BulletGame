package Programming.CBL;

import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class GamePanel extends JPanel implements KeyListener {
    private static final int FRAME_DELAY_MS = 1000 / 60;

    private Timer gameTimer;
    private JFrame parentFrame;
    private Player player;

    boolean[] keys = new boolean[KeyEvent.KEY_TYPED];


    public GamePanel(JFrame frame) {
        this.parentFrame = frame;   
        setBackground(Color.BLACK);
        setFocusable(true);         
        addKeyListener(this);
        this.player = new Player("Player", new int[] {225, 450}, frame);  
        
        gameTimer = new Timer(FRAME_DELAY_MS, e -> {
        updateGame(); // Update game state here
        repaint();
        });
        gameTimer.start();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        player.paintComponent(g);
    }

    protected void updateGame() {
        // esc menu again
        if (keys[KeyEvent.VK_ESCAPE]) {
            Main.showMenu(parentFrame);
            return; 
        }

        // window limits
        if (keys[KeyEvent.VK_LEFT]) {
            player.moveX(-1);
        }
        if (keys[KeyEvent.VK_RIGHT]) {
            player.moveX(1);
        }
        if (keys[KeyEvent.VK_UP]) {
            player.moveY(-1);
        }
        if (keys[KeyEvent.VK_DOWN]) { 
            player.moveY(1);
        }
    }

    // obligatory java so keys work
    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    keys[e.getKeyCode()] = true;
}

    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }
}