package dash;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * A list of suggestions that drops down under a text field (like a search box on a website).
 * Click the field to see all suggestions; typing narrows them. Choose one with the mouse, or
 * with the arrow keys and Enter, and it is put in the field and the field's search runs.
 *
 * It only helps the user type. Each suggestion has a kind (e.g. "Title" or "Location")
 * shown on the right in gray.
 */
public class SuggestionBox {

	private static final int MAX_SHOWN = 8;
	private static final int ROW_HEIGHT = 30;

	private AppTextField field;
	private JPopupMenu popup;
	private JList<String> list;
	private DefaultListModel<String> listModel = new DefaultListModel<String>();
	private ArrayList<String> words = new ArrayList<String>(); // every suggestion
	private ArrayList<String> kinds = new ArrayList<String>(); // "Title", "Location" ... (same order as words)
	private ArrayList<String> shownKinds = new ArrayList<String>(); // kinds of the rows in the list now
	private boolean choosing = false; // true while a chosen word is being put in the field
	private JPanel row;
	private JLabel lblWord;
	private JLabel lblKind;

	public SuggestionBox(AppTextField field) {
		this.field = field;

		list = new JList<String>(listModel);
		list.setFocusable(false); // typing stays in the text field
		list.setBackground(Theme.FIELD);
		list.setFixedCellHeight(ROW_HEIGHT);
		// one row of the list: the word on the left, its kind in gray on the right
		row = new JPanel(new BorderLayout(10, 0));
		row.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
		lblWord = new JLabel();
		lblWord.setFont(Theme.BODY);
		lblKind = new JLabel();
		lblKind.setFont(Theme.SMALL);
		lblKind.setForeground(Theme.MUTED);
		row.add(lblWord, BorderLayout.CENTER);
		row.add(lblKind, BorderLayout.EAST);
		list.setCellRenderer(new ListCellRenderer<String>() {
			public Component getListCellRendererComponent(JList<? extends String> l, String value, int index,
					boolean selected, boolean focus) {
				lblWord.setText(value);
				lblWord.setForeground(selected ? Theme.GOLD_LIGHT : Theme.WHITE);
				lblKind.setText(index < shownKinds.size() ? shownKinds.get(index) : "");
				row.setBackground(selected ? Theme.GOLD_DARK : Theme.FIELD);
				return row;
			}
		});
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				int index = list.locationToIndex(e.getPoint());
				if (index >= 0) {
					choose(index);
				}
			}
		});

		JScrollPane scroll = new JScrollPane(list);
		scroll.setBorder(BorderFactory.createEmptyBorder());
		scroll.getVerticalScrollBar().setUI(new DarkScrollBarUI());

		popup = new JPopupMenu();
		popup.setFocusable(false);
		popup.setBorder(BorderFactory.createLineBorder(Theme.GOLD_DARK));
		popup.setLayout(new BorderLayout());
		popup.add(scroll, BorderLayout.CENTER);

		// typing narrows the list
		field.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				textChanged();
			}

			public void removeUpdate(DocumentEvent e) {
				textChanged();
			}

			public void changedUpdate(DocumentEvent e) {
				textChanged();
			}
		});
		// clicking the field shows the suggestions
		field.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				showMatches();
			}
		});
		field.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				popup.setVisible(false);
			}
		});
		// arrow keys, Enter and Escape
		field.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					if (!popup.isVisible()) {
						showMatches();
					} else if (list.getSelectedIndex() < listModel.size() - 1) {
						list.setSelectedIndex(list.getSelectedIndex() + 1);
						list.ensureIndexIsVisible(list.getSelectedIndex());
					}
					e.consume();
				} else if (e.getKeyCode() == KeyEvent.VK_UP && popup.isVisible()) {
					if (list.getSelectedIndex() > 0) {
						list.setSelectedIndex(list.getSelectedIndex() - 1);
						list.ensureIndexIsVisible(list.getSelectedIndex());
					}
					e.consume();
				} else if (e.getKeyCode() == KeyEvent.VK_ENTER && popup.isVisible() && list.getSelectedIndex() >= 0) {
					choose(list.getSelectedIndex());
					e.consume(); // choose() already runs the search
				} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					popup.setVisible(false);
				}
			}
		});
	}

	/** Removes all suggestions (before filling the list again). */
	public void clear() {
		words.clear();
		kinds.clear();
	}

	/** Adds one suggestion. Empty words and repeats are skipped. */
	public void add(String word, String kind) {
		if (word == null || word.trim().isEmpty()) {
			return;
		}
		for (int i = 0; i < words.size(); i++) {
			if (words.get(i).equalsIgnoreCase(word.trim()) && kinds.get(i).equals(kind)) {
				return;
			}
		}
		words.add(word.trim());
		kinds.add(kind);
	}

	public boolean isShowing() {
		return popup.isVisible();
	}

	/** How many suggestions are in the drop-down list now. */
	public int getShownCount() {
		return listModel.size();
	}

	public String getShown(int index) {
		return listModel.get(index);
	}

	private void textChanged() {
		if (!choosing && field.isShowing() && field.hasFocus()) {
			showMatches();
		}
	}

	/** Shows the suggestions that contain what is typed (all of them if the field is empty). */
	public void showMatches() {
		String typed = field.getText().trim().toLowerCase();
		listModel.clear();
		shownKinds.clear();
		for (int i = 0; i < words.size(); i++) {
			String word = words.get(i);
			if (word.toLowerCase().contains(typed) && !word.equalsIgnoreCase(typed)) {
				listModel.addElement(word);
				shownKinds.add(kinds.get(i));
			}
		}
		if (listModel.isEmpty() || !field.isShowing()) {
			popup.setVisible(false);
			return;
		}
		list.clearSelection();
		int rows = Math.min(listModel.size(), MAX_SHOWN);
		popup.setPopupSize(new Dimension(field.getWidth(), rows * ROW_HEIGHT + 4));
		popup.show(field, 0, field.getHeight() + 2);
	}

	/** Puts the chosen word in the field, closes the list and runs the field's search. */
	public void choose(int index) {
		choosing = true;
		field.setText(listModel.get(index));
		choosing = false;
		popup.setVisible(false);
		field.postActionEvent(); // the same as pressing Enter in the field
	}
}
