package dash;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.io.File;
import java.text.MessageFormat;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.hanapbahay.AppContext;
import com.hanapbahay.model.Agent;
import com.hanapbahay.service.Report;
import com.hanapbahay.service.ReportGenerator;

/**
 * 3.1.8 Report Generation / 5.5 Reports: choose a report, see it in a table, print it,
 * or export it as a CSV file. An Agent sees only their own records; the Admin
 * (setAgent(null)) sees all agents.
 */
public class ReportsPanel extends JPanel implements DashboardSection {

	private static final long serialVersionUID = 1L;
	private Agent agent;
	private Report current;
	private boolean loading = false;
	private AppComboBox<String> cboReport;
	private JLabel lblReportTitle;
	private JLabel lblSummary;
	private AppTable table;

	public ReportsPanel() {
		setBackground(Theme.DARK);
		setBorder(new EmptyBorder(18, 24, 18, 24));
		setLayout(new BorderLayout(0, 14));

		CardPanel pnlToolbar = new CardPanel();
		add(pnlToolbar, BorderLayout.NORTH);
		pnlToolbar.setLayout(new BorderLayout(10, 0));

		JPanel pnlChoose = new JPanel();
		pnlChoose.setOpaque(false);
		pnlToolbar.add(pnlChoose, BorderLayout.WEST);
		pnlChoose.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));

		pnlChoose.add(new CaptionLabel("Report"));

		cboReport = new AppComboBox<String>();
		cboReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!loading) {
					refresh();
				}
			}
		});
		pnlChoose.add(cboReport);

		JPanel pnlActions = new JPanel();
		pnlActions.setOpaque(false);
		pnlToolbar.add(pnlActions, BorderLayout.EAST);
		pnlActions.setLayout(new FlowLayout(FlowLayout.RIGHT, 8, 0));

		AppButton btnRefresh = new AppButton("Refresh", Theme.GRAY);
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refresh();
			}
		});
		pnlActions.add(btnRefresh);

		AppButton btnPrint = new AppButton("Print", Theme.BLUE);
		btnPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				printReport();
			}
		});
		pnlActions.add(btnPrint);

		AppButton btnExport = new AppButton("Export CSV");
		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exportReport();
			}
		});
		pnlActions.add(btnExport);

		JPanel pnlReport = new JPanel();
		pnlReport.setOpaque(false);
		add(pnlReport, BorderLayout.CENTER);
		pnlReport.setLayout(new BorderLayout(0, 8));

		lblReportTitle = new JLabel("Report");
		lblReportTitle.setFont(Theme.HEADING);
		lblReportTitle.setForeground(Theme.GOLD);
		pnlReport.add(lblReportTitle, BorderLayout.NORTH);

		AppScrollPane scrollTable = new AppScrollPane();
		pnlReport.add(scrollTable, BorderLayout.CENTER);

		table = new AppTable();
		table.setModel(new ReadOnlyTableModel(new String[] { "Column 1", "Column 2" }));
		scrollTable.setViewportView(table);

		lblSummary = new JLabel(" ");
		lblSummary.setFont(Theme.BODY_BOLD);
		lblSummary.setForeground(Theme.GOLD_LIGHT);
		pnlReport.add(lblSummary, BorderLayout.SOUTH);
	}

	/** null = Admin: the Agent Performance report is added and all agents are counted. */
	@Override
	public void setAgent(Agent agent) {
		this.agent = agent;
		loading = true;
		cboReport.removeAllItems();
		String[] names = agent == null ? ReportGenerator.ADMIN_REPORTS : ReportGenerator.AGENT_REPORTS;
		for (int i = 0; i < names.length; i++) {
			cboReport.addItem(names[i]);
		}
		loading = false;
	}

	@Override
	public String getPageTitle() {
		return agent == null ? "Reports - All Agents" : "Reports";
	}

	@Override
	public void refresh() {
		String name = (String) cboReport.getSelectedItem();
		if (name == null) {
			return;
		}
		current = AppContext.reportGenerator().generate(name, agent == null ? 0 : agent.getAgentId());
		ReadOnlyTableModel model = new ReadOnlyTableModel(current.getColumns());
		for (String[] row : current.getRows()) {
			model.addRow(row);
		}
		table.setModel(model);
		lblReportTitle.setText(current.getTitle() + (agent == null ? " (all agents)" : ""));
		lblSummary.setText(current.getSummary());
	}

	/** EXCEPTION HANDLING: a printer problem shows a message instead of crashing. */
	private void printReport() {
		if (current == null) {
			return;
		}
		try {
			boolean done = table.print(JTable.PrintMode.FIT_WIDTH,
					new MessageFormat("HanapBahay - " + current.getTitle()),
					new MessageFormat(current.getSummary() + "   Page {0}"));
			if (done) {
				JOptionPane.showMessageDialog(this, "The report was sent to the printer.", getPageTitle(),
						JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (PrinterException e) {
			JOptionPane.showMessageDialog(this, "Cannot print: " + e.getMessage(), getPageTitle(),
					JOptionPane.WARNING_MESSAGE);
		}
	}

	private void exportReport() {
		if (current == null) {
			return;
		}
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
			JOptionPane.showMessageDialog(this, generator.getMessage(), getPageTitle(), JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(this, generator.getMessage(), getPageTitle(), JOptionPane.WARNING_MESSAGE);
		}
	}
}
