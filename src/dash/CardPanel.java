package dash;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/** A dark box with rounded corners and a thin border, used around filters and profile details. */
public class CardPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public CardPanel() {
		setOpaque(false); // paintComponent draws the rounded box
		setBackground(Theme.CARD);
		setBorder(new EmptyBorder(14, 16, 14, 16));
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(getBackground());
		g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
		g2.setColor(Theme.CARD_BORDER);
		g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
		g2.dispose();
		super.paintComponent(g);
	}
}
