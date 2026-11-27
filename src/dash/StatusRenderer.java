package dash;

import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Draws a status cell as a rounded badge with a colored dot:
 * green = Available / Completed / Active, amber = Reserved / Pending / Scheduled,
 * red = Cancelled / Deactivated, gray = Sold and anything else.
 * paintBadge() is STATIC so the Properties list (ListingRenderer) draws the same badge.
 */
public class StatusRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;
	private String status = "";

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focus,
			int row, int column) {
		super.getTableCellRendererComponent(table, value, selected, focus, row, column);
		status = value == null ? "" : value.toString();
		setText(""); // the badge is painted in paintComponent
		setToolTipText(status);
		return this;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g); // the cell background (selected or not)
		Graphics2D g2 = (Graphics2D) g.create();
		paintBadge(g2, status, 12, getHeight() / 2);
		g2.dispose();
	}

	/** {background, text} colors of the badge for one status word. */
	public static Color[] colorsFor(String status) {
		if ("Available".equals(status) || "Completed".equals(status) || "Active".equals(status)) {
			return new Color[] { new Color(28, 72, 58), new Color(120, 224, 180) };
		}
		if ("Reserved".equals(status) || "Pending".equals(status) || "Scheduled".equals(status)) {
			return new Color[] { new Color(86, 66, 32), new Color(242, 198, 112) };
		}
		if ("Cancelled".equals(status) || "Deactivated".equals(status)) {
			return new Color[] { new Color(88, 40, 46), new Color(244, 150, 156) };
		}
		return new Color[] { new Color(60, 66, 80), new Color(208, 214, 224) };
	}

	/** Draws the badge with its left edge at x, centered on centerY. Returns its width. */
	public static int paintBadge(Graphics2D g2, String status, int x, int centerY) {
		if (status == null || status.isEmpty()) {
			return 0;
		}
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		Color[] colors = colorsFor(status);
		g2.setFont(Theme.SMALL_BOLD);
		FontMetrics fm = g2.getFontMetrics();
		int width = 26 + fm.stringWidth(status);
		int height = 22;
		int top = centerY - height / 2;
		g2.setColor(colors[0]);
		g2.fillRoundRect(x, top, width, height, height, height);
		g2.setColor(colors[1]);
		g2.fillOval(x + 9, centerY - 3, 7, 7);
		g2.drawString(status, x + 20, centerY + (fm.getAscent() - fm.getDescent()) / 2);
		return width;
	}
}
