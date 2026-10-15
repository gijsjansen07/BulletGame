package Programming.CBL;


import Programming.CBL.Panels.GamePanel;
import Programming.CBL.Panels.HighScoresPanel;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Bullet Hell - Game");
        frame.setSize(500, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // menu
        showMenu(frame);

        frame.setVisible(true);
    }

    public static void showMenu(JFrame frame) {
        // clean screen
        frame.getContentPane().removeAll();

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(3, 1, 10, 20));

        JButton startButton = new JButton("Start");
        JButton highscoresButton = new JButton("Highscores");
        JButton exitButton = new JButton("Exit");

        // exit
        exitButton.addActionListener(e -> System.exit(0));

        // start button
        startButton.addActionListener(e -> {
            //Clears the frame
            frame.getContentPane().removeAll();

            //Inits a gamepanel on in the window
            GamePanel gamePanel = new GamePanel(frame);
            frame.add(gamePanel);

            frame.revalidate();
            frame.repaint();

            // Allows the gamepanel to check key inputs
            gamePanel.requestFocusInWindow();
        });

        highscoresButton.addActionListener(e -> {
            frame.getContentPane().removeAll();
            HighScoresPanel highScoresPanel = new HighScoresPanel(frame);
            frame.add(highScoresPanel);
            frame.revalidate();
            frame.repaint();
        });

        menuPanel.add(startButton);
        menuPanel.add(highscoresButton);
        menuPanel.add(exitButton);

        frame.add(menuPanel);

        // refresh window
        frame.revalidate();
        frame.repaint();
    }



}