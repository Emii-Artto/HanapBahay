package dash;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Colors a status cell like a badge (from the UI member's design):
 * green = Available / Completed, amber = Reserved / Pending / Scheduled, gray = Sold / Cancelled.
 */
public class StatusRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focus,
			int row, int column) {
		super.getTableCellRendererComponent(table, value, selected, focus, row, column);
		String status = value == null ? "" : value.toString();
		setHorizontalAlignment(SwingConstants.CENTER);
		setFont(Theme.BODY_BOLD);
		if ("Available".equals(status) || "Completed".equals(status)) {
			setBackground(new Color(36, 88, 66));
			setForeground(new Color(145, 230, 179));
		} else if ("Reserved".equals(status) || "Pending".equals(status) || "Scheduled".equals(status)) {
			setBackground(new Color(100, 72, 35));
			setForeground(new Color(255, 207, 117));
		} else if (status.isEmpty()) {
			setBackground(Theme.DARK);
			setForeground(Theme.WHITE);
		} else {
			setBackground(new Color(65, 69, 76));
			setForeground(new Color(210, 215, 220));
		}
		return this;
	}
}
