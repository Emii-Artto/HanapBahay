package dash;

import java.awt.Color;
import java.awt.Font;

/**
 * The colors and fonts of every screen (dark navy and gold), in one place.
 * STATIC FINAL constants: one shared copy that cannot be changed. WindowBuilder reads them too.
 * Change a color here and every screen changes.
 */
public class Theme {

	// ---- colors ----
	public static final Color DARK = new Color(27, 30, 38); // page background (#1B1E26)
	public static final Color PANEL = new Color(21, 23, 29); // sidebar (#15171D)
	public static final Color CARD = new Color(36, 40, 50); // cards and list rows (#242832)
	public static final Color FIELD = new Color(42, 47, 58); // text fields
	public static final Color GOLD = new Color(214, 173, 98); // primary gold (#D6AD62)
	public static final Color GOLD_LIGHT = new Color(230, 196, 130);
	public static final Color GOLD_DARK = new Color(74, 63, 42); // selected table rows
	public static final Color ACTIVE = new Color(44, 41, 35); // selected sidebar item
	public static final Color CARD_BORDER = new Color(52, 57, 70); // borders (#343946)
	public static final Color WHITE = new Color(245, 245, 245); // primary text
	public static final Color MUTED = new Color(165, 173, 189); // secondary text (#A5ADBD)
	public static final Color LINE = new Color(52, 57, 70); // lines between parts
	public static final Color RED = new Color(224, 91, 98); // danger (#E05B62)
	public static final Color BLUE = new Color(58, 110, 165);
	public static final Color GRAY = new Color(58, 63, 76);
	public static final Color SUCCESS = new Color(53, 184, 135); // green (#35B887)
	public static final Color AMBER = new Color(230, 170, 70);
	public static final Color ROW_SELECTED = new Color(46, 43, 36); // selected listing row

	// ---- fonts ----
	public static final Font TITLE = new Font("Segoe UI", Font.BOLD, 22);
	public static final Font HEADING = new Font("Segoe UI", Font.BOLD, 16);
	public static final Font BODY = new Font("Segoe UI", Font.PLAIN, 13);
	public static final Font BODY_BOLD = new Font("Segoe UI", Font.BOLD, 13);
	public static final Font SMALL = new Font("Segoe UI", Font.PLAIN, 11);
	public static final Font SMALL_BOLD = new Font("Segoe UI", Font.BOLD, 11);
	public static final Font BIG_NUMBER = new Font("Segoe UI", Font.BOLD, 26);
}
