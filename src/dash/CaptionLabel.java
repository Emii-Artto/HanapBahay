package dash;

import javax.swing.JLabel;

/** A small gold caption above a field, e.g. "PRICE". CaptionLabel IS-A JLabel. */
public class CaptionLabel extends JLabel {

	private static final long serialVersionUID = 1L;

	public CaptionLabel() {
		this("CAPTION");
	}

	public CaptionLabel(String text) {
		super(text);
		setFont(Theme.SMALL_BOLD);
		setForeground(Theme.GOLD);
	}

	// captions are always shown in capital letters
	@Override
	public void setText(String text) {
		super.setText(text == null ? "" : text.toUpperCase());
	}
}
