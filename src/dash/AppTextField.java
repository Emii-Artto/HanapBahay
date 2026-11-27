package dash;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * A text field in the dark style. AppTextField IS-A JTextField (shows in WindowBuilder).
 * setPlaceholder("...") shows a gray hint while the field is empty.
 */
public class AppTextField extends JTextField {

	private static final long serialVersionUID = 1L;
	private String placeholder = "";

	public AppTextField() {
		setFont(Theme.BODY);
		setBackground(Theme.FIELD);
		setForeground(Theme.WHITE);
		setCaretColor(Theme.GOLD);
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Theme.CARD_BORDER),
				new EmptyBorder(6, 10, 6, 10)));
	}

	public String getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder == null ? "" : placeholder;
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (getText().isEmpty() && !placeholder.isEmpty()) {
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g2.setColor(Theme.MUTED);
			g2.setFont(getFont());
			int y = (getHeight() + g2.getFontMetrics().getAscent() - g2.getFontMetrics().getDescent()) / 2;
			g2.drawString(placeholder, getInsets().left, y);
			g2.dispose();
		}
	}
}
