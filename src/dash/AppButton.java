package dash;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

/**
 * A flat button in the dark-and-gold style that lights up when the mouse is over it.
 * INHERITANCE: AppButton IS-A JButton, so WindowBuilder shows it and you can change its
 * text and background in the Properties view like any button.
 * Gold buttons have dark text; any other background color gets white text.
 */
public class AppButton extends JButton {

	private static final long serialVersionUID = 1L;
	private Color normalColor;

	public AppButton() {
		this("Button", Theme.GOLD);
	}

	public AppButton(String text) {
		this(text, Theme.GOLD);
	}

	public AppButton(String text, Color background) {
		super(text);
		setFont(Theme.BODY_BOLD);
		setOpaque(true);
		setBorderPainted(false);
		setFocusPainted(false);
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		setBorder(new EmptyBorder(10, 16, 10, 16));
		setBackground(background);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (isEnabled()) {
					normalColor = getBackground();
					AppButton.super.setBackground(normalColor.brighter());
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
}
