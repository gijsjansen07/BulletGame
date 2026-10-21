package Programming.CBL.Panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

/**
 * Handles rendering the game over elimination overlay and text.
 */
public class GamePanelHudElements {

    public static void drawHUD(long score, Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        g2.drawString(String.format("Score: %d s", (int) score), 15, 25);

    }

    public static void drawGameOverScreen(Graphics2D g2, int width, int height, long score) {
        // background
        g2.setColor(new Color(0, 0, 0, 190));
        g2.fillRect(0, 0, width, height);

        //Eliminated
        g2.setColor(Color.RED);
        g2.setFont(new Font("Arial", Font.BOLD, 36));
        FontMetrics fm = g2.getFontMetrics();
        String title = "ELIMINATED!";
        int titleX = (width - fm.stringWidth(title)) / 2;
        g2.drawString(title, titleX, 230);

        // Final survivor
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.setColor(Color.WHITE);
        String finalScoreMsg = String.format("Score: %d points", score);
        FontMetrics fmScore = g2.getFontMetrics();
        int scoreX = (width - fmScore.stringWidth(finalScoreMsg)) / 2;
        g2.drawString(finalScoreMsg, scoreX, 270);

        // helper intructuon text
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        g2.setColor(Color.LIGHT_GRAY);
        String tip = "Click 'Try Again' or press 'R' to restart";
        FontMetrics fmTip = g2.getFontMetrics();
        int tipX = (width - fmTip.stringWidth(tip)) / 2;
        g2.drawString(tip, tipX, 310);
    }
}
