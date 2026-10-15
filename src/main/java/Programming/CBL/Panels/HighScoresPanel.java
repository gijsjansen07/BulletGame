package Programming.CBL.Panels;


import Programming.CBL.Main;
import Programming.CBL.Manager.HighScoresManager;
import Programming.CBL.Objects.ScoreObject;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

public class HighScoresPanel extends JPanel {
    private static final Color BACKGROUND = Color.WHITE;
    private static final Color ROW_BACKGROUND = Color.MAGENTA;
    private static final Color ACCENT = Color.BLUE;

    private final JFrame parentFrame;

    public HighScoresPanel(JFrame frame) {
        this.parentFrame = frame;
        setBackground(BACKGROUND);
        setLayout(new BorderLayout(16, 16));
        setBorder(BorderFactory.createEmptyBorder(24, 32, 24, 32));

        JLabel title = new JLabel("HIGH SCORES", SwingConstants.CENTER);
        title.setForeground(ACCENT);
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
        add(title, BorderLayout.NORTH);

        add(createScoreList(), BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> Main.showMenu(parentFrame));
        JPanel footer = new JPanel();
        footer.setOpaque(false);
        footer.add(backButton);
        add(footer, BorderLayout.SOUTH);
    }

    private JScrollPane createScoreList() {
        TreeMap<Long, ScoreObject> scores = HighScoresManager.getScores();
        JPanel rows = new JPanel(new GridLayout(0, 1, 0, 8));
        rows.setBackground(BACKGROUND);

        if (scores.isEmpty()) {
            JLabel emptyMessage = new JLabel("No scores yet. Play a game to set a high score!",
                    SwingConstants.CENTER);
            emptyMessage.setForeground(ACCENT);
            emptyMessage.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
            rows.add(emptyMessage);
        } else {
            rows.add(createRow("RANK", "PLAYER", "SCORE", true));
            int rank = 1;
            for (Map.Entry<Long, ScoreObject> entry : scores.descendingMap().entrySet()) {
                ScoreObject score = entry.getValue();
                rows.add(createRow(
                        Integer.toString(rank++),
                        score.getName(),
                        Long.toString(score.getScore()),
                        false));
            }
        }

        JScrollPane scrollPane = new JScrollPane(rows);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(BACKGROUND);
        scrollPane.setPreferredSize(new Dimension(400, 350));
        return scrollPane;
    }

    private JPanel createRow(String rank, String player, String score, boolean header) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setBackground(header ? BACKGROUND : ROW_BACKGROUND);
        row.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

        JLabel rankLabel = createCell(rank, header, SwingConstants.LEFT);
        rankLabel.setPreferredSize(new Dimension(48, 24));
        JLabel playerLabel = createCell(player, header, SwingConstants.LEFT);
        JLabel scoreLabel = createCell(score, header, SwingConstants.RIGHT);

        row.add(rankLabel, BorderLayout.WEST);
        row.add(playerLabel, BorderLayout.CENTER);
        row.add(scoreLabel, BorderLayout.EAST);
        return row;
    }

    private JLabel createCell(String text, boolean header, int alignment) {
        JLabel label = new JLabel(text, alignment);
        label.setForeground(header ? ACCENT : Color.WHITE);
        label.setFont(new Font(Font.SANS_SERIF,
                header ? Font.BOLD : Font.PLAIN,
                header ? 13 : 15));
        return label;
    }
}
