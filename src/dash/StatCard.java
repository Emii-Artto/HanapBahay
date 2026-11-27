package dash;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * One number card: an icon in a gold tile, a small title, a big number and a note.
 * WindowBuilder shows icon, title, value and note as properties you can edit.
 * setIconType(NavIcon.PROPERTIES) shows a drawn icon; setIcon("₱") still shows a text symbol.
 */
public class StatCard extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel lblIcon;
	private JLabel lblTitle;
	private JLabel lblValue;
	private JLabel lblNote;

	public StatCard() {
		setOpaque(false); // paintComponent draws the rounded card
		setBackground(Theme.CARD);
		setBorder(new EmptyBorder(10, 14, 10, 14));
		setLayout(new BorderLayout(14, 0));

		lblIcon = new JLabel("", SwingConstants.CENTER);
		lblIcon.setFont(new Font("Arial", Font.BOLD, 20));
		lblIcon.setForeground(Theme.GOLD);
		lblIcon.setPreferredSize(new Dimension(42, 42));
		JPanel iconHolder = new JPanel();
		iconHolder.setOpaque(false);
		iconHolder.setLayout(new BorderLayout(0, 0));
		iconHolder.add(lblIcon, BorderLayout.NORTH);
		add(iconHolder, BorderLayout.WEST);

		JPanel text = new JPanel();
		text.setOpaque(false);
		text.setLayout(new BorderLayout(0, 0)); // title on top, the number, the note below
		lblTitle = new JLabel("Title");
		lblTitle.setFont(Theme.SMALL_BOLD);
		lblTitle.setForeground(Theme.MUTED);
		lblValue = new JLabel("0");
		lblValue.setFont(Theme.BIG_NUMBER);
		lblValue.setForeground(Theme.WHITE);
		lblNote = new JLabel(" ");
		lblNote.setFont(Theme.SMALL);
		lblNote.setForeground(Theme.MUTED);
		text.add(lblTitle, BorderLayout.NORTH);
		text.add(lblValue, BorderLayout.CENTER);
		text.add(lblNote, BorderLayout.SOUTH);
		add(text, BorderLayout.CENTER);
	}

	public String getIcon() {
		return lblIcon.getText();
	}

	public void setIcon(String icon) {
		lblIcon.setText(icon);
	}

	/** Shows one of the drawn NavIcon icons, e.g. NavIcon.CLIENTS. */
	public void setIconType(String iconType) {
		lblIcon.setText("");
		lblIcon.setIcon(new NavIcon(iconType, 20, Theme.GOLD));
	}

	public String getTitle() {
		return lblTitle.getText();
	}

	public void setTitle(String title) {
		lblTitle.setText(title == null ? "" : title.toUpperCase());
	}

	public String getValue() {
		return lblValue.getText();
	}

	public void setValue(String value) {
		lblValue.setText(value);
	}

	public String getNote() {
		return lblNote.getText();
	}

	public void setNote(String note) {
		lblNote.setText(note);
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(getBackground());
		g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
		g2.setColor(Theme.CARD_BORDER);
		g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
		// gold tile behind the icon
		Rectangle r = lblIcon.getBounds();
		r.translate(lblIcon.getParent().getX(), lblIcon.getParent().getY());
		g2.setColor(new Color(214, 173, 98, 38));
		g2.fillRoundRect(r.x, r.y, r.width, r.height, 12, 12);
		g2.dispose();
		super.paintComponent(g);
	}
}
