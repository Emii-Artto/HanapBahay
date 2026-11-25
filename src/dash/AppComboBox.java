package dash;

import javax.swing.JComboBox;

/** A drop-down list in the dark style. <E> is the kind of item, e.g. AppComboBox<Client>. */
public class AppComboBox<E> extends JComboBox<E> {

	private static final long serialVersionUID = 1L;

	public AppComboBox() {
		setFont(Theme.BODY);
		setBackground(Theme.FIELD);
		setForeground(Theme.WHITE);
		setFocusable(false);
	}
}
