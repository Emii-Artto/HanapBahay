package dash;

import java.awt.Dimension;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

/**
 * A table in the dark style with a gold header. A column named "Status" is drawn as a
 * colored badge automatically. AppTable IS-A JTable (shows in WindowBuilder).
 */
public class AppTable extends JTable {

	private static final long serialVersionUID = 1L;
	private StatusRenderer statusRenderer = new StatusRenderer();

	public AppTable() {
		setRowHeight(36);
		setFont(Theme.BODY);
		setForeground(Theme.WHITE);
		setBackground(Theme.DARK);
		setSelectionBackground(Theme.GOLD_DARK);
		setSelectionForeground(Theme.WHITE);
		setGridColor(Theme.LINE);
		setFillsViewportHeight(true);

		JTableHeader header = getTableHeader();
		header.setReorderingAllowed(false);
		header.setPreferredSize(new Dimension(0, 38));
		DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
		headerRenderer.setBackground(Theme.CARD);
		headerRenderer.setForeground(Theme.GOLD);
		headerRenderer.setFont(Theme.BODY_BOLD);
		headerRenderer.setHorizontalAlignment(SwingConstants.LEFT);
		header.setDefaultRenderer(headerRenderer);
	}

	// POLYMORPHISM (overriding): the "Status" column uses the badge renderer
	@Override
	public TableCellRenderer getCellRenderer(int row, int column) {
		if ("Status".equals(getColumnName(column))) {
			return statusRenderer;
		}
		return super.getCellRenderer(row, column);
	}
}
