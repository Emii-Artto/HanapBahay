package dash;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * A sidebar menu button: an icon, the name, and a gold bar on the left when it is the page
 * being shown.
 * INHERITANCE: NavButton IS-A AppButton (which IS-A JButton). It adds setActive().
 */
public class NavButton extends AppButton {

	private static final long serialVersionUID = 1L;
	private boolean active = false;

	public NavButton() {
		this("Menu", NavIcon.PROPERTIES);
	}

	public NavButton(String text, String iconType) {
		super(text, Theme.PANEL);
		setIcon(new NavIcon(iconType));
		setIconTextGap(12);
		setHorizontalAlignment(SwingConstants.LEFT);
		setFont(Theme.BODY_BOLD);
		setBorder(new EmptyBorder(10, 16, 10, 12));
		setForeground(Theme.MUTED);
	}

	public boolean isActive() {
		return active;
	}

	/** true = this is the page being shown: gold text, a lighter background and a gold bar. */
	public void setActive(boolean active) {
		this.active = active;
		setBackground(active ? Theme.ACTIVE : Theme.PANEL);
		setForeground(active ? Theme.GOLD : Theme.MUTED);
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (active) {
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(Theme.GOLD);
			g2.fillRoundRect(0, 9, 3, getHeight() - 18, 3, 3);
			g2.dispose();
		}
	}
}
