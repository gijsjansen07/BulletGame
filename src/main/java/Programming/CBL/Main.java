package Programming.CBL;


import Programming.CBL.Panels.GamePanel;
import Programming.CBL.Panels.HighScoresPanel;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Main {
    public static final int WIDTH = 500;
    public static final int HEIGHT = 600;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Bullet Hell - Game");
        frame.setSize(WIDTH, HEIGHT);
        frame.setResizable(false); // Evita que se pueda cambiar el tamaño de la ventana
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        showMenu(frame);

        frame.setVisible(true);
    }

    public static void showMenu(JFrame frame) {
        frame.getContentPane().removeAll();

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(3, 1, 10, 20));

        JButton startButton = new JButton("Start");
        JButton highscoresButton = new JButton("Highscores");
        JButton exitButton = new JButton("Exit");

        exitButton.addActionListener(e -> System.exit(0));

        startButton.addActionListener(e -> {
            frame.getContentPane().removeAll();

            GamePanel gamePanel = new GamePanel(frame);
            frame.add(gamePanel);

            frame.revalidate();
            frame.repaint();

            gamePanel.requestFocusInWindow();
        });

        highscoresButton.addActionListener(e -> {
            frame.getContentPane().removeAll();
            // Inits the highscores panel
            HighScoresPanel highScoresPanel = new HighScoresPanel(frame);
            frame.add(highScoresPanel);
            frame.revalidate();
            frame.repaint();
        });

        menuPanel.add(startButton);
        menuPanel.add(highscoresButton);
        menuPanel.add(exitButton);

        frame.add(menuPanel);
        frame.revalidate();
        frame.repaint();
    }



}