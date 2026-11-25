package dash;

import java.awt.Color;
import java.awt.Font;

/**
 * The colors and fonts of every screen (UI member's dark and gold design), in one place.
 * STATIC FINAL constants: one shared copy that cannot be changed. WindowBuilder reads them too.
 */
public class Theme {

	// ---- colors ----
	public static final Color DARK = new Color(24, 26, 33);
	public static final Color PANEL = new Color(31, 34, 43);
	public static final Color CARD = new Color(36, 39, 47);
	public static final Color FIELD = new Color(43, 46, 55);
	public static final Color GOLD = new Color(205, 169, 96);
	public static final Color GOLD_LIGHT = new Color(226, 190, 115);
	public static final Color GOLD_DARK = new Color(91, 77, 51);
	public static final Color ACTIVE = new Color(66, 60, 46);
	public static final Color CARD_BORDER = new Color(105, 87, 58);
	public static final Color WHITE = new Color(245, 245, 245);
	public static final Color MUTED = new Color(169, 184, 201);
	public static final Color LINE = new Color(65, 67, 76);
	public static final Color RED = new Color(173, 66, 66);
	public static final Color BLUE = new Color(52, 95, 140);
	public static final Color GRAY = new Color(88, 93, 104);

	// ---- fonts ----
	public static final Font TITLE = new Font("Serif", Font.BOLD, 28);
	public static final Font HEADING = new Font("Arial", Font.BOLD, 16);
	public static final Font BODY = new Font("Arial", Font.PLAIN, 13);
	public static final Font BODY_BOLD = new Font("Arial", Font.BOLD, 13);
	public static final Font SMALL = new Font("Arial", Font.PLAIN, 11);
	public static final Font SMALL_BOLD = new Font("Arial", Font.BOLD, 11);
	public static final Font BIG_NUMBER = new Font("Arial", Font.BOLD, 26);
}
