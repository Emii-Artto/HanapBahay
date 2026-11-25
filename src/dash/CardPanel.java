package dash;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/** A dark box with a thin gold border, used around filters and profile details. */
public class CardPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public CardPanel() {
		setBackground(Theme.CARD);
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Theme.CARD_BORDER),
				new EmptyBorder(12, 16, 12, 16)));
	}
}
