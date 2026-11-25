package com.hanapbahay.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

/**
 * The look of every screen in one place: colors, fonts, and small helper methods
 * for buttons, fields, tables and cards (3.2.5 consistent layout and button scheme).
 *
 * Everything is STATIC: screens use it by class name, e.g. Theme.GOLD or Theme.button(...).
 */
public class Theme {

	// ---- colors (dark and gold, from the UI member's design) ----
	public static final Color DARK = new Color(24, 26, 33);
	public static final Color PANEL = new Color(31, 34, 43);
	public static final Color CARD = new Color(36, 39, 47);
	public static final Color FIELD = new Color(43, 46, 55);
	public static final Color GOLD = new Color(205, 169, 96);
	public static final Color GOLD_LIGHT = new Color(226, 190, 115);
	public static final Color GOLD_DARK = new Color(91, 77, 51);
	public static final Color CARD_BORDER = new Color(105, 87, 58);
	public static final Color WHITE = new Color(245, 245, 245);
	public static final Color MUTED = new Color(169, 184, 201);
	public static final Color LINE = new Color(65, 67, 76);
	public static final Color RED = new Color(173, 66, 66);
	public static final Color BLUE = new Color(52, 95, 140);
	public static final Color GRAY = new Color(88, 93, 104);

	// ---- fonts ----
	public static final Font BRAND = new Font("Arial", Font.BOLD, 20);
	public static final Font TITLE = new Font("Serif", Font.BOLD, 28);
	public static final Font HEADING = new Font("Arial", Font.BOLD, 16);
	public static final Font BODY = new Font("Arial", Font.PLAIN, 13);
	public static final Font BODY_BOLD = new Font("Arial", Font.BOLD, 13);
	public static final Font SMALL = new Font("Arial", Font.PLAIN, 11);
	public static final Font SMALL_BOLD = new Font("Arial", Font.BOLD, 11);
	public static final Font BIG_NUMBER = new Font("Arial", Font.BOLD, 26);

	/** A flat colored button that gets lighter when the mouse is over it. */
	public static JButton button(String text, final Color background, Color foreground) {
		final JButton button = new JButton(text);
		button.setFont(BODY_BOLD);
		button.setForeground(foreground);
		button.setBackground(background);
		button.setOpaque(true);
		button.setBorderPainted(false);
		button.setFocusPainted(false);
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button.setBorder(new EmptyBorder(10, 16, 10, 16));
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (button.isEnabled()) {
					button.setBackground(background.brighter());
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				button.setBackground(background);
			}
		});
		return button;
	}

	public static JButton goldButton(String text) {
		return button(text, GOLD, DARK);
	}

	public static JButton grayButton(String text) {
		return button(text, GRAY, WHITE);
	}

	public static JButton redButton(String text) {
		return button(text, RED, WHITE);
	}

	public static JTextField field() {
		JTextField field = new JTextField();
		styleField(field);
		return field;
	}

	public static void styleField(JTextField field) {
		field.setFont(BODY);
		field.setBackground(FIELD);
		field.setForeground(WHITE);
		field.setCaretColor(GOLD);
		field.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(LINE),
				new EmptyBorder(6, 8, 6, 8)));
	}

	public static void styleCombo(JComboBox<?> combo) {
		combo.setFont(BODY);
		combo.setBackground(FIELD);
		combo.setForeground(WHITE);
		combo.setFocusable(false);
	}

	/** A small gold caption above a field, e.g. "PRICE". */
	public static JLabel caption(String text) {
		JLabel label = new JLabel(text.toUpperCase());
		label.setFont(SMALL_BOLD);
		label.setForeground(GOLD);
		return label;
	}

	public static JLabel sectionTitle(String text) {
		JLabel label = new JLabel(text);
		label.setFont(HEADING);
		label.setForeground(GOLD);
		label.setBorder(new EmptyBorder(0, 0, 10, 0));
		label.setAlignmentX(Component.LEFT_ALIGNMENT);
		return label;
	}

	public static JLabel mutedLabel(String text) {
		JLabel label = new JLabel(text);
		label.setFont(BODY);
		label.setForeground(MUTED);
		return label;
	}

	/** A dark rounded-looking box with a thin gold border, used for filters and forms. */
	public static JPanel card() {
		JPanel card = new JPanel();
		card.setBackground(CARD);
		card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(CARD_BORDER),
				new EmptyBorder(14, 16, 14, 16)));
		return card;
	}

	/** One number card for a dashboard: icon, title, big number and a small note. */
	public static JPanel statCard(String icon, String title, JLabel number, JLabel note) {
		JPanel card = new JPanel(new BorderLayout(12, 0));
		card.setBackground(CARD);
		card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(CARD_BORDER),
				new EmptyBorder(16, 14, 16, 14)));

		JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
		iconLabel.setFont(new Font("Arial", Font.BOLD, 26));
		iconLabel.setForeground(GOLD);
		iconLabel.setPreferredSize(new Dimension(36, 40));

		JPanel text = new JPanel();
		text.setOpaque(false);
		text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
		JLabel titleLabel = new JLabel(title);
		titleLabel.setFont(BODY);
		titleLabel.setForeground(MUTED);
		number.setFont(BIG_NUMBER);
		number.setForeground(GOLD_LIGHT);
		note.setFont(SMALL);
		note.setForeground(MUTED);
		text.add(titleLabel);
		text.add(number);
		text.add(note);

		card.add(iconLabel, BorderLayout.WEST);
		card.add(text, BorderLayout.CENTER);
		return card;
	}

	/**
	 * A form with captions on the left and fields on the right, used inside dialogs.
	 * Pass pairs: "Caption", component, "Caption", component, ...
	 */
	public static JPanel form(Object[] captionsAndFields) {
		JPanel form = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(4, 4, 4, 8);
		c.anchor = GridBagConstraints.WEST;
		for (int i = 0; i < captionsAndFields.length; i += 2) {
			c.gridy = i / 2;
			c.gridx = 0;
			c.weightx = 0;
			c.fill = GridBagConstraints.NONE;
			JLabel label = new JLabel(captionsAndFields[i].toString());
			label.setFont(BODY_BOLD);
			form.add(label, c);
			c.gridx = 1;
			c.weightx = 1;
			c.fill = GridBagConstraints.HORIZONTAL;
			JComponent field = (JComponent) captionsAndFields[i + 1];
			field.setPreferredSize(new Dimension(280, field.getPreferredSize().height + 4));
			form.add(field, c);
		}
		return form;
	}

	/** Dark table style with a gold header, used by every table. */
	public static JScrollPane styleTable(JTable table) {
		table.setRowHeight(36);
		table.setFont(BODY);
		table.setForeground(WHITE);
		table.setBackground(DARK);
		table.setSelectionBackground(GOLD_DARK);
		table.setSelectionForeground(WHITE);
		table.setGridColor(LINE);
		table.setFillsViewportHeight(true);
		table.getTableHeader().setReorderingAllowed(false);

		JTableHeader header = table.getTableHeader();
		header.setPreferredSize(new Dimension(0, 38));
		DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
		headerRenderer.setBackground(CARD);
		headerRenderer.setForeground(GOLD);
		headerRenderer.setFont(BODY_BOLD);
		headerRenderer.setHorizontalAlignment(SwingConstants.LEFT);
		header.setDefaultRenderer(headerRenderer);

		JScrollPane scroll = new JScrollPane(table);
		scroll.setBorder(BorderFactory.createLineBorder(CARD_BORDER));
		scroll.getViewport().setBackground(DARK);
		return scroll;
	}

	/** Colors a status cell: green Available/Completed, amber Reserved/Pending/Scheduled, gray Sold/Cancelled. */
	public static class StatusRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focus,
				int row, int column) {
			super.getTableCellRendererComponent(table, value, selected, focus, row, column);
			String status = value == null ? "" : value.toString();
			setHorizontalAlignment(SwingConstants.CENTER);
			setFont(BODY_BOLD);
			if ("Available".equals(status) || "Completed".equals(status)) {
				setBackground(new Color(36, 88, 66));
				setForeground(new Color(145, 230, 179));
			} else if ("Reserved".equals(status) || "Pending".equals(status) || "Scheduled".equals(status)) {
				setBackground(new Color(100, 72, 35));
				setForeground(new Color(255, 207, 117));
			} else {
				setBackground(new Color(65, 69, 76));
				setForeground(new Color(210, 215, 220));
			}
			return this;
		}
	}
}
