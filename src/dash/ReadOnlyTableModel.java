package dash;

import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;

/**
 * A table model whose cells cannot be typed into (records are changed through forms instead).
 * A column named "Photo" holds pictures, so the table draws them as images.
 */
public class ReadOnlyTableModel extends DefaultTableModel {

	private static final long serialVersionUID = 1L;

	public ReadOnlyTableModel(String[] columns) {
		super(new Object[][] {}, columns);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	@Override
	public Class<?> getColumnClass(int column) {
		if ("Photo".equals(getColumnName(column))) {
			return ImageIcon.class;
		}
		return Object.class;
	}
}
