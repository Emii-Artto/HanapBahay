package dash;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * The big "PROPERTY LISTINGS" banner with the gold houses, from the UI member's design.
 * The title and subtitle are properties, so WindowBuilder can change them.
 */
public class HeroBanner extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel lblTitle;
	private JLabel lblSubtitle;

	public HeroBanner() {
		setOpaque(false);
		setPreferredSize(new Dimension(600, 112));
		setBorder(new EmptyBorder(18, 30, 26, 20));
		setLayout(new GridLayout(2, 1, 0, 2));
		lblTitle = new JLabel("PROPERTY LISTINGS");
		lblTitle.setFont(new Font("Arial", Font.BOLD, 28));
		lblTitle.setForeground(Color.WHITE);
		add(lblTitle);
		lblSubtitle = new JLabel("Manage and organize your property listings.");
		lblSubtitle.setFont(new Font("Arial", Font.PLAIN, 15));
		lblSubtitle.setForeground(Theme.GOLD_LIGHT);
		add(lblSubtitle);
	}

	public String getTitle() {
		return lblTitle.getText();
	}

	public void setTitle(String title) {
		lblTitle.setText(title);
	}

	public String getSubtitle() {
		return lblSubtitle.getText();
	}

	public void setSubtitle(String subtitle) {
		lblSubtitle.setText(subtitle);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		int w = getWidth();
		int h = getHeight();
		g2.setPaint(new GradientPaint(0, 0, new Color(35, 37, 44), w, h, new Color(65, 59, 45)));
		g2.fillRoundRect(0, 0, w, h, 25, 25);
		g2.setColor(new Color(205, 169, 96, 40));
		g2.fillOval(w - 430, 18, 480, 110);
		drawHouse(g2, w - 360, 8);
		drawHouse(g2, w - 210, 28);
		g2.setColor(Theme.GOLD);
		g2.fillRoundRect(25, h - 11, 185, 5, 5, 5);
		g2.dispose();
	}

	private void drawHouse(Graphics2D g2, int x, int y) {
		Graphics2D h2 = (Graphics2D) g2.create();
		h2.translate(x, y);
		h2.scale(0.58, 0.58);
		h2.setColor(new Color(13, 15, 20));
		h2.fillRect(20, 70, 170, 100);
		h2.setColor(Theme.GOLD);
		h2.setStroke(new BasicStroke(4f));
		h2.drawPolyline(new int[] { 5, 105, 205 }, new int[] { 75, 5, 75 }, 3);
		h2.fillRect(45, 105, 35, 65);
		h2.fillRect(105, 105, 35, 65);
		h2.dispose();
	}
}
