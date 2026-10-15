package Programming.CBL;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.GridLayout;

/**
 * Main entry point for the Bullet Hell game.
 * Sets up the fixed window and manages top-level menu navigation.
 */
public class Main {
    // Fixed window dimensions to prevent layout distortion
    public static final int WIDTH = 500;
    public static final int HEIGHT = 600;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Bullet Hell - Game");
        frame.setSize(WIDTH, HEIGHT);
        frame.setResizable(false); // Keeps window bounds consistent
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Display the start menu
        showMenu(frame);

        frame.setVisible(true);
    }

    /**
     * Clears existing views and renders the primary navigation menu.
     * @param frame The application main window.
     */
    public static void showMenu(JFrame frame) {
        frame.getContentPane().removeAll();

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(3, 1, 10, 20));

        JButton startButton = new JButton("Start");
        JButton highscoresButton = new JButton("Highscores");
        JButton exitButton = new JButton("Exit");

        // Terminate application on exit
        exitButton.addActionListener(e -> System.exit(0));

        // Switch from menu view to active game session
        startButton.addActionListener(e -> {
            frame.getContentPane().removeAll();

            GamePanel gamePanel = new GamePanel(frame);
            frame.add(gamePanel);

            frame.revalidate();
            frame.repaint();

            // Give input focus to the game canvas for key listening
            gamePanel.requestFocusInWindow();
        });

        menuPanel.add(startButton);
        menuPanel.add(highscoresButton);
        menuPanel.add(exitButton);

        frame.add(menuPanel);
        frame.revalidate();
        frame.repaint();
    }
}