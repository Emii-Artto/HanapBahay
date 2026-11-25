package com.hanapbahay.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.io.File;
import java.text.MessageFormat;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import com.hanapbahay.AppContext;
import com.hanapbahay.model.Agent;
import com.hanapbahay.service.Report;
import com.hanapbahay.service.ReportGenerator;

/**
 * 3.1.8 Report Generation / 5.5 Reports: choose a report, see it in a table,
 * print it, or export it as a CSV file.
 * An Agent sees only their own records; the Admin (agent == null) sees all agents.
 */
public class ReportsPanel extends BasePanel {

	private static final long serialVersionUID = 1L;

	private Report current;
	private JComboBox<String> cboReport;
	private JLabel lblTitle;
	private JLabel lblSummary;
	private DefaultTableModel tableModel;
	private JTable table;

	public ReportsPanel(Agent agent) {
		super(agent);
		add(createToolbar(), BorderLayout.NORTH);
		add(createTable(), BorderLayout.CENTER);
		refresh();
	}

	@Override
	public String getTitle() {
		return "Reports";
	}

	private JPanel createToolbar() {
		JPanel card = Theme.card();
		card.setLayout(new BorderLayout(10, 0));
		JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		left.setOpaque(false);
		left.add(Theme.caption("Report"));
		String[] reports = agent == null ? ReportGenerator.ADMIN_REPORTS : ReportGenerator.AGENT_REPORTS;
		cboReport = new JComboBox<String>(reports);
		Theme.styleCombo(cboReport);
		cboReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refresh();
			}
		});
		left.add(cboReport);
		card.add(left, BorderLayout.WEST);

		JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
		right.setOpaque(false);
		JButton btnRefresh = Theme.grayButton("Refresh");
		JButton btnPrint = Theme.button("Print", Theme.BLUE, Theme.WHITE);
		JButton btnExport = Theme.goldButton("Export CSV");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refresh();
			}
		});
		btnPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				printReport();
			}
		});
		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exportReport();
			}
		});
		right.add(btnRefresh);
		right.add(btnPrint);
		right.add(btnExport);
		card.add(right, BorderLayout.EAST);
		return card;
	}

	private JPanel createTable() {
		JPanel center = new JPanel(new BorderLayout(0, 8));
		center.setOpaque(false);
		lblTitle = Theme.sectionTitle(" ");
		center.add(lblTitle, BorderLayout.NORTH);
		tableModel = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable(tableModel);
		center.add(Theme.styleTable(table), BorderLayout.CENTER);
		lblSummary = new JLabel(" ");
		lblSummary.setFont(Theme.BODY_BOLD);
		lblSummary.setForeground(Theme.GOLD_LIGHT);
		center.add(lblSummary, BorderLayout.SOUTH);
		return center;
	}

	@Override
	public void refresh() {
		String name = (String) cboReport.getSelectedItem();
		current = AppContext.reportGenerator().generate(name, agentId());
		tableModel.setColumnIdentifiers(current.getColumns());
		tableModel.setRowCount(0);
		for (String[] row : current.getRows()) {
			tableModel.addRow(row);
		}
		lblTitle.setText(current.getTitle() + (agent == null ? " (all agents)" : ""));
		lblSummary.setText(current.getSummary());
	}

	/** Prints the table. EXCEPTION HANDLING: a printer problem shows a message instead of crashing. */
	private void printReport() {
		try {
			boolean done = table.print(JTable.PrintMode.FIT_WIDTH,
					new MessageFormat("HanapBahay - " + current.getTitle()),
					new MessageFormat(current.getSummary() + "   Page {0}"));
			if (done) {
				showInfo("The report was sent to the printer.");
			}
		} catch (PrinterException e) {
			showWarning("Cannot print: " + e.getMessage());
		}
	}

	private void exportReport() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new FileNameExtensionFilter("CSV file (opens in Excel)", "csv"));
		chooser.setSelectedFile(new File("HanapBahay - " + current.getTitle() + ".csv"));
		if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
			return;
		}
		File file = chooser.getSelectedFile();
		if (!file.getName().toLowerCase().endsWith(".csv")) {
			file = new File(file.getAbsolutePath() + ".csv");
		}
		ReportGenerator generator = AppContext.reportGenerator();
		if (generator.exportCsv(current, file)) {
			showInfo(generator.getMessage());
		} else {
			showWarning(generator.getMessage());
		}
	}
}
