package dash;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 * A thin dark scroll bar with a rounded handle and no arrow buttons.
 * INHERITANCE + OVERRIDING: changes only how Swing's BasicScrollBarUI is painted.
 */
public class DarkScrollBarUI extends BasicScrollBarUI {

	@Override
	protected void configureScrollBarColors() {
		thumbColor = Theme.GRAY;
		trackColor = Theme.DARK;
	}

	@Override
	protected JButton createDecreaseButton(int orientation) {
		return noButton();
	}

	@Override
	protected JButton createIncreaseButton(int orientation) {
		return noButton();
	}

	private JButton noButton() {
		JButton button = new JButton();
		button.setPreferredSize(new Dimension(0, 0));
		button.setMinimumSize(new Dimension(0, 0));
		button.setMaximumSize(new Dimension(0, 0));
		return button;
	}

	@Override
	protected void paintTrack(Graphics g, JComponent c, Rectangle bounds) {
		g.setColor(trackColor);
		g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
	}

	@Override
	protected void paintThumb(Graphics g, JComponent c, Rectangle bounds) {
		if (bounds.isEmpty()) {
			return;
		}
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(thumbColor);
		g2.fillRoundRect(bounds.x + 3, bounds.y + 2, bounds.width - 6, bounds.height - 4, 8, 8);
		g2.dispose();
	}
}
