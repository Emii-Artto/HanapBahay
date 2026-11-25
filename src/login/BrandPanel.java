package login;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import dash.Theme;

/**
 * The left half of the login screen: a dark gradient with gold houses (UI member's design).
 * It only paints the background; the text on it is added in LoginFrame.
 */
public class BrandPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		int w = getWidth();
		int h = getHeight();
		g2.setPaint(new GradientPaint(0, 0, new Color(40, 36, 30), w, h, new Color(18, 20, 26)));
		g2.fillRect(0, 0, w, h);
		g2.setColor(new Color(205, 169, 96, 60));
		g2.fillOval(w / 2 - 40, h / 3 - 120, 360, 360);
		drawHouse(g2, w / 2 - 20, h / 2 - 20, 150);
		drawHouse(g2, w / 2 + 110, h / 2 + 20, 110);
		g2.dispose();
	}

	private void drawHouse(Graphics2D g2, int x, int y, int size) {
		g2.setStroke(new BasicStroke(4));
		g2.setColor(Theme.GOLD);
		g2.drawLine(x - size / 2 - 10, y, x, y - size / 2);
		g2.drawLine(x, y - size / 2, x + size / 2 + 10, y);
		g2.setColor(new Color(15, 16, 20));
		g2.fillRect(x - size / 2, y, size, (int) (size * 0.6));
		g2.setColor(Theme.GOLD);
		int window = size / 5;
		g2.fillRect(x - size / 3, y + size / 8, window, (int) (window * 1.4));
		g2.fillRect(x + size / 3 - window, y + size / 8, window, (int) (window * 1.4));
	}
}
