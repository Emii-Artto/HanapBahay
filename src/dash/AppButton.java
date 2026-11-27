package dash;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

/**
 * A flat button with rounded corners in the dark-and-gold style that lights up when the
 * mouse is over it.
 * INHERITANCE: AppButton IS-A JButton, so WindowBuilder shows it and you can change its
 * text and background in the Properties view like any button.
 * Gold buttons have dark text; any other background color gets white text.
 * setOutline(color) draws a thin border, for quieter buttons such as Edit.
 */
public class AppButton extends JButton {

	private static final long serialVersionUID = 1L;
	private Color normalColor;
	private Color outline; // null = no border line

	public AppButton() {
		this("Button", Theme.GOLD);
	}

	public AppButton(String text) {
		this(text, Theme.GOLD);
	}

	public AppButton(String text, Color background) {
		super(text);
		setFont(Theme.BODY_BOLD);
		setOpaque(false);
		setContentAreaFilled(false); // paintComponent draws the rounded background
		setBorderPainted(false);
		setFocusPainted(false);
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		setBorder(new EmptyBorder(9, 16, 9, 16));
		setIconTextGap(8);
		setBackground(background);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (isEnabled()) {
					normalColor = getBackground();
					AppButton.super.setBackground(lighter(normalColor));
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (normalColor != null) {
					AppButton.super.setBackground(normalColor);
				}
			}
		});
	}

	// POLYMORPHISM (overriding): changing the color also picks a readable text color
	@Override
	public void setBackground(Color background) {
		super.setBackground(background);
		normalColor = background;
		setForeground(Theme.GOLD.equals(background) ? Theme.DARK : Theme.WHITE);
	}

	public Color getOutline() {
		return outline;
	}

	public void setOutline(Color outline) {
		this.outline = outline;
		repaint();
	}

	/** A little lighter for the hover effect (works for dark colors too). */
	private Color lighter(Color c) {
		return new Color(Math.min(255, c.getRed() + 18), Math.min(255, c.getGreen() + 18),
				Math.min(255, c.getBlue() + 18));
	}

	// POLYMORPHISM (overriding): draw a rounded background, then let JButton draw the text and icon
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Color fill = getBackground();
		if (getModel().isPressed()) {
			fill = fill.darker();
		}
		g2.setColor(fill);
		g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
		if (outline != null) {
			g2.setColor(outline);
			g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
		}
		g2.dispose();
		super.paintComponent(g);
	}
}
