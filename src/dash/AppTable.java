package dash;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

/**
 * A table in the dark style: quiet header, padded cells and thin lines between rows.
 * A column named "Status" is drawn as a colored badge automatically.
 * AppTable IS-A JTable (shows in WindowBuilder).
 */
public class AppTable extends JTable {

	private static final long serialVersionUID = 1L;
	private StatusRenderer statusRenderer = new StatusRenderer();

	public AppTable() {
		setRowHeight(40);
		setFont(Theme.BODY);
		setForeground(Theme.WHITE);
		setBackground(Theme.DARK);
		setSelectionBackground(Theme.GOLD_DARK);
		setSelectionForeground(Theme.WHITE);
		setGridColor(Theme.LINE);
		setShowVerticalLines(false);
		setShowHorizontalLines(true);
		setIntercellSpacing(new Dimension(0, 1));
		setFillsViewportHeight(true);

		// every normal cell gets some space on the left and right
		setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean selected,
					boolean focus, int row, int column) {
				super.getTableCellRendererComponent(table, value, selected, focus, row, column);
				setBorder(new EmptyBorder(0, 12, 0, 12));
				return this;
			}
		});

		JTableHeader header = getTableHeader();
		header.setReorderingAllowed(false);
		header.setPreferredSize(new Dimension(0, 36));
		header.setDefaultRenderer(new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean selected,
					boolean focus, int row, int column) {
				super.getTableCellRendererComponent(table, value, selected, focus, row, column);
				setText(value == null ? "" : value.toString().toUpperCase());
				setBackground(Theme.CARD);
				setForeground(Theme.MUTED);
				setFont(Theme.SMALL_BOLD);
				setHorizontalAlignment(SwingConstants.LEFT);
				setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.LINE),
						new EmptyBorder(0, 12, 0, 12)));
				return this;
			}
		});
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
