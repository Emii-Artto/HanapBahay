package dash;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * One number card (UI member's design): an icon, a title, a big number and a small note.
 * WindowBuilder shows icon, title, value and note as properties you can edit.
 */
public class StatCard extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel lblIcon;
	private JLabel lblTitle;
	private JLabel lblValue;
	private JLabel lblNote;

	public StatCard() {
		setBackground(Theme.CARD);
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Theme.CARD_BORDER),
				new EmptyBorder(8, 14, 8, 14)));
		setLayout(new BorderLayout(12, 0));

		lblIcon = new JLabel("⌂", SwingConstants.CENTER);
		lblIcon.setFont(new Font("Arial", Font.BOLD, 26));
		lblIcon.setForeground(Theme.GOLD);
		lblIcon.setPreferredSize(new Dimension(36, 40));
		add(lblIcon, BorderLayout.WEST);

		JPanel text = new JPanel();
		text.setOpaque(false);
		text.setLayout(new GridLayout(3, 1, 0, 0));
		lblTitle = new JLabel("Title");
		lblTitle.setFont(Theme.BODY);
		lblTitle.setForeground(Theme.MUTED);
		lblValue = new JLabel("0");
		lblValue.setFont(Theme.BIG_NUMBER);
		lblValue.setForeground(Theme.GOLD_LIGHT);
		lblNote = new JLabel(" ");
		lblNote.setFont(Theme.SMALL);
		lblNote.setForeground(Theme.MUTED);
		text.add(lblTitle);
		text.add(lblValue);
		text.add(lblNote);
		add(text, BorderLayout.CENTER);
	}

	public String getIcon() {
		return lblIcon.getText();
	}

	public void setIcon(String icon) {
		lblIcon.setText(icon);
	}

	public String getTitle() {
		return lblTitle.getText();
	}

	public void setTitle(String title) {
		lblTitle.setText(title);
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
}
