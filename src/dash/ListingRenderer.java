package dash;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * Draws one row of the Properties list as a card: photo, title, location and price,
 * type and lot area, and the status badge.
 * It only DRAWS the row. Selecting a row and the View / Edit / Delete buttons work the
 * same as before, because the table and its rows are unchanged.
 *
 * INTERFACE: implements Swing's TableCellRenderer, so the table asks this class how to draw
 * each cell. One class is used for every column; "kind" says which part of the card it is.
 *
 * Cell values: PHOTO = an ImageIcon (or null), TITLE = {title, caption, id},
 * DETAILS = {value, label, value, label}, STATUS = the status word.
 */
public class ListingRenderer extends JPanel implements TableCellRenderer {

	private static final long serialVersionUID = 1L;
	public static final int PHOTO = 0;
	public static final int TITLE = 1;
	public static final int DETAILS = 2;
	public static final int STATUS = 3;

	private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 14);
	private static final Font VALUE_FONT = new Font("Segoe UI", Font.BOLD, 13);

	private int kind;
	private Object value;
	private boolean selected;
	private boolean firstColumn;
	private boolean lastColumn;

	public ListingRenderer() {
		this(TITLE);
	}

	public ListingRenderer(int kind) {
		this.kind = kind;
		setOpaque(false);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focus,
			int row, int column) {
		this.value = value;
		this.selected = selected;
		this.firstColumn = column == 0;
		this.lastColumn = column == table.getColumnCount() - 1;
		return this;
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		int w = getWidth();
		int top = 5; // space between the rows
		int bottom = getHeight() - 5;

		// the card: one rounded rectangle across the whole row (each cell draws its own part)
		int left = firstColumn ? 0 : -20;
		int right = lastColumn ? w - 1 : w + 20;
		g2.setColor(selected ? Theme.ROW_SELECTED : Theme.CARD);
		g2.fillRoundRect(left, top, right - left, bottom - top, 14, 14);
		g2.setColor(selected ? Theme.GOLD : Theme.CARD_BORDER);
		g2.drawRoundRect(left, top, right - left, bottom - top, 14, 14);
		if (kind == DETAILS || kind == STATUS) { // thin divider before this part
			g2.setColor(Theme.LINE);
			g2.drawLine(0, top + 14, 0, bottom - 14);
		}

		if (kind == PHOTO) {
			paintPhoto(g2, 10, top + 9, w - 14, bottom - top - 18);
		} else if (kind == TITLE && value instanceof String[]) {
			String[] text = (String[]) value;
			paintText(g2, text[0], TITLE_FONT, Theme.WHITE, 14, top + 26, w - 28);
			paintText(g2, text[1], Theme.SMALL, Theme.MUTED, 14, top + 46, w - 28);
			paintText(g2, text[2], Theme.SMALL_BOLD, Theme.GOLD, 14, top + 70, w - 28);
		} else if (kind == DETAILS && value instanceof String[]) {
			String[] text = (String[]) value;
			paintText(g2, text[0], VALUE_FONT, Theme.WHITE, 18, top + 24, w - 32);
			paintText(g2, text[1], Theme.SMALL, Theme.MUTED, 18, top + 40, w - 32);
			paintText(g2, text[2], VALUE_FONT, Theme.GOLD_LIGHT, 18, top + 62, w - 32);
			paintText(g2, text[3], Theme.SMALL, Theme.MUTED, 18, top + 78, w - 32);
		} else if (kind == STATUS && value != null) {
			StatusRenderer.paintBadge(g2, value.toString(), 18, top + 30);
			paintText(g2, "Status", Theme.SMALL, Theme.MUTED, 18, top + 66, w - 32);
		}
		g2.dispose();
	}

	/** The first photo of the property, or an empty frame with a picture icon. */
	private void paintPhoto(Graphics2D g2, int x, int y, int width, int height) {
		Shape frame = new RoundRectangle2D.Double(x, y, width, height, 10, 10);
		g2.setColor(Theme.FIELD);
		g2.fill(frame);
		if (value instanceof ImageIcon) {
			ImageIcon photo = (ImageIcon) value;
			Shape oldClip = g2.getClip();
			g2.clip(frame);
			int px = x + (width - photo.getIconWidth()) / 2;
			int py = y + (height - photo.getIconHeight()) / 2;
			g2.drawImage(photo.getImage(), px, py, null);
			g2.setClip(oldClip);
		} else {
			new NavIcon(NavIcon.PHOTO, 26, Theme.MUTED).paintIcon(this, g2, x + (width - 26) / 2, y + (height - 26) / 2);
		}
	}

	/** Draws one line of text; text that is too long ends with "...". */
	private void paintText(Graphics2D g2, String text, Font font, Color color, int x, int y, int maxWidth) {
		if (text == null) {
			return;
		}
		g2.setFont(font);
		g2.setColor(color);
		FontMetrics fm = g2.getFontMetrics();
		String shown = text;
		if (fm.stringWidth(shown) > maxWidth) {
			while (shown.length() > 1 && fm.stringWidth(shown + "...") > maxWidth) {
				shown = shown.substring(0, shown.length() - 1);
			}
			shown = shown + "...";
		}
		g2.drawString(shown, x, y);
	}
}
