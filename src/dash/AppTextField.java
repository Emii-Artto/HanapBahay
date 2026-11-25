package dash;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/** A text field in the dark style. AppTextField IS-A JTextField (shows in WindowBuilder). */
public class AppTextField extends JTextField {

	private static final long serialVersionUID = 1L;

	public AppTextField() {
		setFont(Theme.BODY);
		setBackground(Theme.FIELD);
		setForeground(Theme.WHITE);
		setCaretColor(Theme.GOLD);
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Theme.LINE),
				new EmptyBorder(6, 8, 6, 8)));
	}
}
