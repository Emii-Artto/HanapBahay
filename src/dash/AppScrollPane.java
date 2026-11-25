package dash;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

/** A scroll area for tables, with the thin gold border. */
public class AppScrollPane extends JScrollPane {

	private static final long serialVersionUID = 1L;

	public AppScrollPane() {
		setBorder(BorderFactory.createLineBorder(Theme.CARD_BORDER));
		getViewport().setBackground(Theme.DARK);
		getVerticalScrollBar().setUnitIncrement(16);
	}
}
