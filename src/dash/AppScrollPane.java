package dash;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

/** A scroll area for tables, with a thin border and dark scroll bars. */
public class AppScrollPane extends JScrollPane {

	private static final long serialVersionUID = 1L;

	public AppScrollPane() {
		setBorder(BorderFactory.createLineBorder(Theme.CARD_BORDER));
		setBackground(Theme.DARK);
		getViewport().setBackground(Theme.DARK);
		getVerticalScrollBar().setUnitIncrement(16);
		getVerticalScrollBar().setUI(new DarkScrollBarUI());
		getVerticalScrollBar().setPreferredSize(new Dimension(12, 0));
		getHorizontalScrollBar().setUI(new DarkScrollBarUI());
		getHorizontalScrollBar().setPreferredSize(new Dimension(0, 12));
	}
}
