package Programming.CBL;

import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePanel extends JPanel implements KeyListener {
    
    private int playerX = 225; 
    private int playerY = 450;
    
    private static final int SIZE = 30;
    private static final int SPEED = 10;

    private JFrame parentFrame;

    public GamePanel(JFrame frame) {
        this.parentFrame = frame;   
        setBackground(Color.BLACK);
        setFocusable(true);         
        addKeyListener(this);       
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        g.setColor(Color.BLUE);
        g.fillRect(playerX, playerY, SIZE, SIZE);
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // esc menu again
        if (key == KeyEvent.VK_ESCAPE) {
            Main.showMenu(parentFrame);
            return; 
        }

        // window limits
        if (key == KeyEvent.VK_LEFT && playerX > 0) {
            playerX -= SPEED;
        }
        if (key == KeyEvent.VK_RIGHT && playerX < getWidth() - SIZE) {
            playerX += SPEED;
        }
        if (key == KeyEvent.VK_UP && playerY > 0) {
            playerY -= SPEED;
        }
        if (key == KeyEvent.VK_DOWN && playerY < getHeight() - SIZE) { 
            playerY += SPEED;
        }
        
        repaint(); 
    }

    // obligatory java so keys work
    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }
}