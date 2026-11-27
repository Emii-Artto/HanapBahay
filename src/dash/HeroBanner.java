package dash;

import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * The heading at the top of the Properties page: a title and a one-line explanation.
 * The title and subtitle are properties, so WindowBuilder can change them.
 */
public class HeroBanner extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel lblTitle;
	private JLabel lblSubtitle;

	public HeroBanner() {
		setOpaque(false);
		setBorder(new EmptyBorder(0, 2, 0, 0));
		setLayout(new GridLayout(2, 1, 0, 2));
		lblTitle = new JLabel("Property Listings");
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
		lblTitle.setForeground(Theme.WHITE);
		add(lblTitle);
		lblSubtitle = new JLabel("Manage and organize your property listings.");
		lblSubtitle.setFont(Theme.BODY);
		lblSubtitle.setForeground(Theme.MUTED);
		add(lblSubtitle);
	}

	public String getTitle() {
		return lblTitle.getText();
	}

	public void setTitle(String title) {
		lblTitle.setText(title);
	}

	public String getSubtitle() {
		return lblSubtitle.getText();
	}

	public void setSubtitle(String subtitle) {
		lblSubtitle.setText(subtitle);
	}
}
