package dash;

import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;

/**
 * A drop-down list in the dark style, with a thin border and a small arrow.
 * <E> is the kind of item, e.g. AppComboBox<Client>.
 */
public class AppComboBox<E> extends JComboBox<E> {

	private static final long serialVersionUID = 1L;

	public AppComboBox() {
		setFont(Theme.BODY);
		setBackground(Theme.FIELD);
		setForeground(Theme.WHITE);
		setFocusable(false);
		setBorder(BorderFactory.createLineBorder(Theme.CARD_BORDER));

		// the look of the box itself: dark background and a drawn arrow
		setUI(new BasicComboBoxUI() {
			@Override
			protected JButton createArrowButton() {
				JButton arrow = new JButton(new NavIcon(NavIcon.CHEVRON, 12, Theme.MUTED));
				arrow.setBorder(new EmptyBorder(0, 8, 0, 10));
				arrow.setContentAreaFilled(false);
				arrow.setFocusPainted(false);
				return arrow;
			}

			@Override
			public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
				g.setColor(Theme.FIELD);
				g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
			}

			// draws the chosen item; a locked (disabled) box keeps the dark color with gray text
			@Override
			public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				g2.setFont(comboBox.getFont());
				g2.setColor(comboBox.isEnabled() ? Theme.WHITE : Theme.MUTED);
				Object item = comboBox.getSelectedItem();
				String text = item == null ? "" : item.toString();
				FontMetrics fm = g2.getFontMetrics();
				int y = bounds.y + (bounds.height + fm.getAscent() - fm.getDescent()) / 2;
				g2.drawString(text, bounds.x + 10, y);
				g2.dispose();
			}
		});

		// the look of each item: some space on the left, gold-brown when highlighted
		setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean selected,
					boolean focus) {
				super.getListCellRendererComponent(list, value, index, selected, focus);
				setBorder(new EmptyBorder(6, 10, 6, 10));
				setBackground(selected && index >= 0 ? Theme.GOLD_DARK : Theme.FIELD);
				setForeground(Theme.WHITE);
				return this;
			}
		});
	}
}
