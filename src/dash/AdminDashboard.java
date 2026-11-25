package dash;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.hanapbahay.AppContext;
import com.hanapbahay.model.User;
import com.hanapbahay.service.DashboardService;
import com.hanapbahay.service.DashboardSummary;
import com.hanapbahay.service.UserService;

import login.LogIn;

/**
 * The Admin's dashboard. Same dark and gold style as AgentDashboard.
 *
 * Shows totals for ALL agents (listings, inquiries, sales) and one row per agent.
 * (Client Mode is only on the Agent's screen, so it is not shown here.)
 * All numbers come from MySQL through the services:
 *   AppContext.dashboardService() and AppContext.userService()
 */
public class AdminDashboard extends JFrame {

	private static final long serialVersionUID = 1L;

	// COLOR PALETTE (same as AgentDashboard)
	private final Color dark = new Color(24, 26, 33);
	private final Color dark2 = new Color(31, 34, 43);
	private final Color card = new Color(36, 39, 47);
	private final Color gold = new Color(205, 169, 96);
	private final Color goldLight = new Color(226, 190, 115);
	private final Color ink = new Color(245, 245, 245);
	private final Color muted = new Color(169, 184, 201);
	private final Color line = new Color(65, 67, 76);

	private User admin; // the admin who logged in (null when opened from WindowBuilder)

	// numbers on the four cards
	private JLabel lblListings;
	private JLabel lblListingsDetail;
	private JLabel lblInquiries;
	private JLabel lblInquiriesDetail;
	private JLabel lblSales;
	private JLabel lblSalesAmount;
	private JLabel lblScope;

	private DefaultTableModel agentModel;

	// Used by WindowBuilder (no login)
	public AdminDashboard() {
		this(null);
	}

	// Used after login: LogIn passes the logged-in admin
	public AdminDashboard(User admin) {
		this.admin = admin;

		setTitle("Estate Manager - Admin Dashboard");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1280, 800);
		setMinimumSize(new Dimension(1000, 650));
		setLocationRelativeTo(null);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().setBackground(dark);
		getContentPane().add(createSidebar(), BorderLayout.WEST);
		getContentPane().add(createMainArea(), BorderLayout.CENTER);

		loadData();
	}

	// ================= SIDEBAR =================
	private JPanel createSidebar() {
		JPanel sidebar = new JPanel(new BorderLayout());
		sidebar.setPreferredSize(new Dimension(245, 0));
		sidebar.setBackground(dark2);
		sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, gold));

		JPanel upper = new JPanel();
		upper.setOpaque(false);
		upper.setLayout(new BoxLayout(upper, BoxLayout.Y_AXIS));
		upper.setBorder(new EmptyBorder(28, 18, 10, 18));

		JLabel logo = new JLabel("⌂ ESTATE MANAGER");
		logo.setFont(new Font("Arial", Font.BOLD, 19));
		logo.setForeground(gold);
		logo.setAlignmentX(Component.LEFT_ALIGNMENT);

		JLabel sub = new JLabel("ADMIN PANEL");
		sub.setFont(new Font("Arial", Font.PLAIN, 11));
		sub.setForeground(muted);
		sub.setBorder(new EmptyBorder(7, 4, 35, 0));
		sub.setAlignmentX(Component.LEFT_ALIGNMENT);

		JButton btnOverview = makeButton(" ■  Overview", new Color(56, 53, 46), Color.WHITE);
		btnOverview.setHorizontalAlignment(SwingConstants.LEFT);
		btnOverview.setAlignmentX(Component.LEFT_ALIGNMENT);
		btnOverview.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));

		upper.add(logo);
		upper.add(sub);
		upper.add(btnOverview);
		sidebar.add(upper, BorderLayout.NORTH);

		// ADMIN PROFILE + LOG OUT
		JPanel lower = new JPanel();
		lower.setOpaque(false);
		lower.setLayout(new BoxLayout(lower, BoxLayout.Y_AXIS));
		lower.setBorder(new EmptyBorder(10, 18, 22, 18));

		JPanel separator = new JPanel();
		separator.setBackground(line);
		separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
		lower.add(separator);
		lower.add(Box.createVerticalStrut(22));

		JLabel profile = new JLabel("● Admin Profile");
		profile.setFont(new Font("Arial", Font.BOLD, 15));
		profile.setForeground(gold);
		lower.add(profile);

		String adminName = " System Administrator";
		if (admin != null) {
			adminName = " " + admin.getFullName();
		}
		JLabel profileName = new JLabel(adminName);
		profileName.setFont(new Font("Arial", Font.PLAIN, 13));
		profileName.setForeground(muted);
		profileName.setBorder(new EmptyBorder(10, 0, 20, 0));
		lower.add(profileName);

		JButton logout = makeButton("Log Out", gold, dark);
		logout.setAlignmentX(Component.LEFT_ALIGNMENT);
		logout.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
		logout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doLogout();
			}
		});
		lower.add(logout);

		sidebar.add(lower, BorderLayout.SOUTH);
		return sidebar;
	}

	// ================= MAIN AREA =================
	private JPanel createMainArea() {
		JPanel mainArea = new JPanel(new BorderLayout());
		mainArea.setBackground(dark);

		// TOP BAR: greeting + refresh button
		JPanel topBar = new JPanel(new BorderLayout());
		topBar.setBackground(dark);
		topBar.setBorder(new EmptyBorder(15, 30, 15, 20));
		topBar.setPreferredSize(new Dimension(100, 65));

		String greeting = "Welcome, Admin.";
		if (admin != null) {
			greeting = "Welcome, " + admin.getFullName() + ".";
		}
		JLabel lblGreeting = new JLabel(greeting);
		lblGreeting.setFont(new Font("Arial", Font.ITALIC, 15));
		lblGreeting.setForeground(Color.WHITE);

		JButton btnRefresh = makeButton("Refresh", gold, dark);
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadData();
			}
		});

		topBar.add(lblGreeting, BorderLayout.WEST);
		topBar.add(btnRefresh, BorderLayout.EAST);
		mainArea.add(topBar, BorderLayout.NORTH);

		// PAGE CONTENT (scrolls when the window is small)
		JPanel content = new JPanel();
		content.setBackground(dark);
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		content.setBorder(new EmptyBorder(20, 30, 30, 30));

		JLabel lblTitle = new JLabel("Admin Dashboard");
		lblTitle.setFont(new Font("Serif", Font.BOLD, 30));
		lblTitle.setForeground(ink);
		lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
		content.add(lblTitle);

		lblScope = new JLabel(" ");
		lblScope.setFont(new Font("Arial", Font.PLAIN, 13));
		lblScope.setForeground(muted);
		lblScope.setBorder(new EmptyBorder(6, 0, 22, 0));
		lblScope.setAlignmentX(Component.LEFT_ALIGNMENT);
		content.add(lblScope);

		content.add(createStatistics());
		content.add(Box.createVerticalStrut(30));

		// AGENT PERFORMANCE TABLE
		content.add(createSectionTitle("Agent Performance"));
		String[] agentColumns = { "Agent", "Username", "Listings", "Inquiries", "Sales", "Total Sales" };
		agentModel = createTableModel(agentColumns);
		content.add(createTablePanel(agentModel, 230));

		JScrollPane scroll = new JScrollPane(content);
		scroll.setBorder(null);
		scroll.getViewport().setBackground(dark);
		scroll.getVerticalScrollBar().setUnitIncrement(16);
		mainArea.add(scroll, BorderLayout.CENTER);

		return mainArea;
	}

	// The four cards at the top
	private JPanel createStatistics() {
		JPanel stats = new JPanel(new GridLayout(1, 4, 18, 0));
		stats.setOpaque(false);
		stats.setAlignmentX(Component.LEFT_ALIGNMENT);
		stats.setPreferredSize(new Dimension(700, 120)); // small enough so no sideways scrollbar
		stats.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

		lblListings = new JLabel("0");
		lblListingsDetail = new JLabel(" ");
		lblInquiries = new JLabel("0");
		lblInquiriesDetail = new JLabel(" ");
		lblSales = new JLabel("0");
		lblSalesAmount = new JLabel("PHP 0.00");

		stats.add(createStatCard("⌂", "Total Listings", lblListings, lblListingsDetail));
		stats.add(createStatCard("●", "Inquiries", lblInquiries, lblInquiriesDetail));
		stats.add(createStatCard("■", "Sales Recorded", lblSales, new JLabel("all agents")));
		stats.add(createStatCard("₱", "Total Sales", lblSalesAmount, new JLabel("sum of all sale prices")));
		return stats;
	}

	private JPanel createStatCard(String icon, String title, JLabel number, JLabel detail) {
		JPanel statCard = new JPanel(new BorderLayout(10, 0));
		statCard.setBackground(card);
		statCard.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(105, 87, 58)),
				new EmptyBorder(18, 15, 18, 15)));

		JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
		iconLabel.setFont(new Font("Arial", Font.BOLD, 27));
		iconLabel.setForeground(gold);
		iconLabel.setPreferredSize(new Dimension(40, 45));

		JPanel text = new JPanel();
		text.setOpaque(false);
		text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

		JLabel titleLabel = new JLabel(title);
		titleLabel.setFont(new Font("Arial", Font.PLAIN, 13));
		titleLabel.setForeground(muted);

		number.setFont(new Font("Arial", Font.BOLD, 24));
		number.setForeground(goldLight);

		detail.setFont(new Font("Arial", Font.PLAIN, 11));
		detail.setForeground(muted);

		text.add(titleLabel);
		text.add(Box.createVerticalStrut(5));
		text.add(number);
		text.add(Box.createVerticalStrut(4));
		text.add(detail);

		statCard.add(iconLabel, BorderLayout.WEST);
		statCard.add(text, BorderLayout.CENTER);
		return statCard;
	}

	private JLabel createSectionTitle(String text) {
		JLabel label = new JLabel(text);
		label.setFont(new Font("Arial", Font.BOLD, 17));
		label.setForeground(gold);
		label.setBorder(new EmptyBorder(0, 0, 12, 0));
		label.setAlignmentX(Component.LEFT_ALIGNMENT);
		return label;
	}

	// A table model whose cells cannot be edited by typing
	private DefaultTableModel createTableModel(String[] columns) {
		return new DefaultTableModel(columns, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
	}

	private JScrollPane createTablePanel(DefaultTableModel model, int height) {
		JTable table = new JTable(model);
		table.setRowHeight(40);
		table.setFont(new Font("Arial", Font.PLAIN, 13));
		table.setForeground(Color.WHITE);
		table.setBackground(dark2);
		table.setSelectionBackground(new Color(91, 77, 51));
		table.setSelectionForeground(Color.WHITE);
		table.setGridColor(line);
		table.setFillsViewportHeight(true);

		JTableHeader header = table.getTableHeader();
		header.setPreferredSize(new Dimension(0, 40));
		header.setReorderingAllowed(false);
		DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
		headerRenderer.setBackground(card);
		headerRenderer.setForeground(gold);
		headerRenderer.setFont(new Font("Arial", Font.BOLD, 13));
		headerRenderer.setHorizontalAlignment(SwingConstants.LEFT);
		header.setDefaultRenderer(headerRenderer);

		JScrollPane scroll = new JScrollPane(table);
		scroll.setBorder(BorderFactory.createLineBorder(new Color(105, 87, 58)));
		scroll.getViewport().setBackground(dark2);
		scroll.setAlignmentX(Component.LEFT_ALIGNMENT);
		scroll.setPreferredSize(new Dimension(800, height));
		scroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
		return scroll;
	}

	// ================= DATA (from the backend) =================
	// Called when the window opens and when Refresh is clicked
	private void loadData() {
		if (admin == null) {
			lblScope.setText("Log in as an Admin to see the data.");
			return;
		}

		DashboardService dashboardService = AppContext.dashboardService();
		UserService userService = AppContext.userService();

		// 1. The four cards: Admin sees ALL agents because Admin.canViewAllSales() is true
		DashboardSummary summary = dashboardService.getSummary(admin);
		if (summary == null) {
			JOptionPane.showMessageDialog(this, dashboardService.getMessage(), "Cannot load dashboard",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		lblScope.setText(summary.getScopeLabel());
		lblListings.setText(String.valueOf(summary.getTotalListings()));
		lblListingsDetail.setText(summary.getAvailableListings() + " avail., " + summary.getReservedListings()
				+ " reserved, " + summary.getSoldListings() + " sold");
		lblInquiries.setText(String.valueOf(summary.getTotalInquiries()));
		lblInquiriesDetail.setText(summary.getNewInquiries() + " new");
		lblSales.setText(String.valueOf(summary.getTotalSales()));
		lblSalesAmount.setText(summary.getTotalSalesAmountText());

		// 2. One row per agent: the same getSummary() gives ONLY that agent's numbers,
		//    because Agent.canViewAllSales() is false (polymorphism)
		ArrayList<User> agents = userService.getAgents();
		agentModel.setRowCount(0);
		for (User agent : agents) {
			DashboardSummary agentSummary = dashboardService.getSummary(agent);
			agentModel.addRow(new Object[] { agent.getFullName(), agent.getUsername(),
					agentSummary.getTotalListings(), agentSummary.getTotalInquiries(),
					agentSummary.getTotalSales(), agentSummary.getTotalSalesAmountText() });
		}
		if (agents.isEmpty()) {
			agentModel.addRow(new Object[] { "No agents yet", "", "", "", "", "" });
		}
	}

	private void doLogout() {
		int answer = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?", "Log Out",
				JOptionPane.YES_NO_OPTION);
		if (answer == JOptionPane.YES_OPTION) {
			LogIn login = new LogIn();
			login.setVisible(true); // go back to the login screen
			dispose();
		}
	}

	// Same button style as AgentDashboard
	private JButton makeButton(String text, final Color background, Color foreground) {
		final JButton button = new JButton(text);
		button.setFont(new Font("Arial", Font.BOLD, 13));
		button.setForeground(foreground);
		button.setBackground(background);
		button.setOpaque(true);
		button.setBorderPainted(false);
		button.setFocusPainted(false);
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button.setBorder(new EmptyBorder(12, 17, 12, 17));
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				button.setBackground(background.brighter());
			}

			@Override
			public void mouseExited(MouseEvent e) {
				button.setBackground(background);
			}
		});
		return button;
	}
}
