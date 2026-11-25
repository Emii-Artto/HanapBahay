package dash;

import javax.swing.BorderFactory;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

/** A password field in the dark style. */
public class AppPasswordField extends JPasswordField {

	private static final long serialVersionUID = 1L;

	public AppPasswordField() {
		setFont(Theme.BODY);
		setBackground(Theme.FIELD);
		setForeground(Theme.WHITE);
		setCaretColor(Theme.GOLD);
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Theme.LINE),
				new EmptyBorder(6, 8, 6, 8)));
	}
}
