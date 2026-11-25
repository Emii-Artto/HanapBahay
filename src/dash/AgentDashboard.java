package dash;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.hanapbahay.AppContext;
import com.hanapbahay.model.User;
import com.hanapbahay.service.DashboardService;
import com.hanapbahay.service.DashboardSummary;
import com.hanapbahay.service.PropertyService;

import login.LogIn;

public class AgentDashboard extends JFrame {

 private static final long serialVersionUID = 1L;

 // COLOR PALETTE
 private final Color dark = new Color(24, 26, 33);
 private final Color dark2 = new Color(31, 34, 43);
 private final Color gold = new Color(205, 169, 96);
 private final Color goldLight = new Color(226, 190, 115);
 private final Color page = new Color(24, 26, 33);
 private final Color ink = new Color(245, 245, 245);
 private final Color muted = new Color(169, 184, 201);
 private final Color line = new Color(65, 67, 76);

 private JPanel mainArea;
 private JPanel pageHost;
 private JPanel statsPanel;
 private JPanel contentPanel;

 private JScrollPane mainScroll;

 private JTable propertyTable;
 private DefaultTableModel tableModel;

 private JTextField searchField;
 private JTextField minPriceField;
 private JTextField maxPriceField;

 private JComboBox<String> typeFilter;

 private JButton addButton;
 private JButton editButton;
 private JButton deleteButton;
 private JButton deleteTopButton;
 private JButton viewButton;
 private JButton modeButton;

 private JLabel propertyCount;
 private JLabel clientCount;
 private JLabel appointmentCount;
 private JLabel dealCount;
 private JLabel filterStatus;

 private boolean clientMode = false;


 private java.awt.Image bannerImage;

 private final ArrayList<Property> properties =
 new ArrayList<Property>();

 private final DecimalFormat money =
 new DecimalFormat("#,##0.00");

 private User user; // the agent who logged in (null when opened from WindowBuilder or main)

 private JPanel sidebarPanel; // the left menu; hidden in Client Mode

 // Used by WindowBuilder and the main method below (no login)
 public AgentDashboard() {
 this(null);
 }

 // Used after login: LogIn passes the logged-in agent
 public AgentDashboard(User user) {

 this.user = user;

 setTitle("Estate Manager - Agent Dashboard");

 setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

 setSize(1440, 900);

 setMinimumSize(new Dimension(850, 600));

 setLocationRelativeTo(null);

 loadBanner();

 buildInterface();
 }

 // LOAD BANNER IMAGE
 private void loadBanner() {

 try {

 java.net.URL resource =
 getClass().getResource("/assets/dreamnest_banner.png");

 if (resource != null) {

 bannerImage = ImageIO.read(resource);

 } else {

 File file =
 new File("src/assets/dreamnest_banner.png");

 if (!file.exists()) {
 file = new File("assets/dreamnest_banner.png");
 }

 if (file.exists()) {
 bannerImage = ImageIO.read(file);
 }
 }

 } catch (Exception e) {
 bannerImage = null;
 }
 }

 // MAIN INTERFACE
 private void buildInterface() {

 getContentPane().setLayout(new BorderLayout());

 getContentPane().setBackground(page);

 sidebarPanel = createSidebar(); // kept in a field so Client Mode can hide it

 getContentPane().add(
 sidebarPanel,
 BorderLayout.WEST
 );

 getContentPane().add(
 createMainArea(),
 BorderLayout.CENTER
 );
 }

 // SIDEBAR
 private JPanel createSidebar() {

 JPanel sidebar = new JPanel(new BorderLayout());

 sidebar.setPreferredSize(new Dimension(225, 0));

 sidebar.setMinimumSize(new Dimension(190, 0));

 sidebar.setBackground(dark2);

 sidebar.setBorder(
 BorderFactory.createMatteBorder(
 0, 0, 0, 1, gold
 )
 );

 JPanel upper = new JPanel();

 upper.setOpaque(false);

 upper.setLayout(
 new BoxLayout(upper, BoxLayout.Y_AXIS)
 );

 upper.setBorder(
 new EmptyBorder(28, 18, 10, 18)
 );

 JLabel logo = new JLabel("⌂ ESTATE MANAGER");

 logo.setFont(new Font("Arial", Font.BOLD, 23));

 logo.setForeground(gold);

 logo.setAlignmentX(Component.LEFT_ALIGNMENT);

 JLabel sub = new JLabel("REAL ESTATE SYSTEM");

 sub.setFont(new Font("Arial", Font.PLAIN, 11));

 sub.setForeground(muted);

 sub.setBorder(new EmptyBorder(7, 4, 35, 0));

 sub.setAlignmentX(Component.LEFT_ALIGNMENT);

 upper.add(logo);
 upper.add(sub);

 addNavigationButton(upper, "Dashboard");
 addNavigationButton(upper, "Properties");
 addNavigationButton(upper, "Clients");
 addNavigationButton(upper, "Viewing");
 addNavigationButton(upper, "Deals");
 addNavigationButton(upper, "Reports");

 sidebar.add(upper, BorderLayout.NORTH);

 // AGENT PROFILE
 JPanel lower = new JPanel();

 lower.setOpaque(false);

 lower.setLayout(
 new BoxLayout(lower, BoxLayout.Y_AXIS)
 );

 lower.setBorder(
 new EmptyBorder(10, 18, 22, 18)
 );

 JPanel separator = new JPanel();

 separator.setBackground(line);

 separator.setMaximumSize(
 new Dimension(Integer.MAX_VALUE, 1)
 );

 lower.add(separator);

 lower.add(Box.createVerticalStrut(22));

 JLabel profile = new JLabel("● Agent Profile");

 profile.setFont(new Font("Arial", Font.BOLD, 15));

 profile.setForeground(gold);

 lower.add(profile);

 String agentName = " Real Estate Agent";
 if (user != null) {
 agentName = " " + user.getFullName();
 }

 JLabel profileName = new JLabel(agentName);

 profileName.setFont(
 new Font("Arial", Font.PLAIN, 13)
 );

 profileName.setForeground(muted);

 profileName.setBorder(
 new EmptyBorder(10, 0, 20, 0)
 );

 lower.add(profileName);

 JButton logout = makeButton(
 "Log Out", gold, dark
 );

 logout.setAlignmentX(Component.LEFT_ALIGNMENT);

 logout.setMaximumSize(
 new Dimension(Integer.MAX_VALUE, 42)
 );

 logout.addActionListener(e -> {

 int answer = JOptionPane.showConfirmDialog(
 this,
 "Are you sure you want to log out?",
 "Log Out",
 JOptionPane.YES_NO_OPTION
 );

 if (answer == JOptionPane.YES_OPTION) {
 LogIn login = new LogIn();
 login.setVisible(true); // go back to the login screen
 dispose();
 }
 });

 lower.add(logout);

 sidebar.add(lower, BorderLayout.SOUTH);

 return sidebar;
 }

 private void addNavigationButton(
 JPanel parent, String name) {

 JButton button = makeButton(
 " " + navigationSymbol(name) + " " + name,
 dark2,
 Color.WHITE
 );

 button.setHorizontalAlignment(SwingConstants.LEFT);

 button.setAlignmentX(Component.LEFT_ALIGNMENT);

 button.setMaximumSize(
 new Dimension(Integer.MAX_VALUE, 55)
 );

 button.setPreferredSize(
 new Dimension(185, 55)
 );

 button.setBorder(
 new EmptyBorder(0, 12, 0, 8)
 );

 button.addActionListener(e -> {

 if ("Dashboard".equals(name)) {
 showDashboardPage();
 } else if ("Properties".equals(name)) {
 showPropertiesPage();
 } else {
 showPlaceholderPage(name);
 }
 });

 button.addMouseListener(new MouseAdapter() {

 @Override
 public void mouseEntered(MouseEvent e) {
 button.setBackground(new Color(56, 53, 46));
 }

 @Override
 public void mouseExited(MouseEvent e) {
 button.setBackground(dark2);
 }
 });

 parent.add(button);

 parent.add(Box.createVerticalStrut(10));
 }

 private String navigationSymbol(String name) {

 if ("Dashboard".equals(name)) return "■";
 if ("Properties".equals(name)) return "◇";
 if ("Clients".equals(name)) return "●";
 if ("Viewing".equals(name)) return "▦";
 if ("Deals".equals(name)) return "⇄";

 return "▥";
 }

 // MAIN AREA
 private JPanel createMainArea() {

 mainArea = new JPanel(new BorderLayout());

 mainArea.setBackground(page);

 JPanel topBar = new JPanel(new BorderLayout());

 topBar.setBackground(dark);

 topBar.setBorder(
 new EmptyBorder(15, 30, 15, 20)
 );

 topBar.setPreferredSize(
 new Dimension(100, 65)
 );

 JLabel slogan = new JLabel(
 "Build more dreams. Sell more homes."
 );

 slogan.setFont(
 new Font("Arial", Font.ITALIC, 15)
 );

 slogan.setForeground(Color.WHITE);

 modeButton = makeButton(
 "● Agent Mode ▼",
 gold,
 dark
 );

 modeButton.addActionListener(
 e -> toggleClientMode()
 );

 topBar.add(slogan, BorderLayout.WEST);

 topBar.add(modeButton, BorderLayout.EAST);

 pageHost = new JPanel(new BorderLayout());

 pageHost.setBackground(page);

 mainArea.add(topBar, BorderLayout.NORTH);

 mainArea.add(pageHost, BorderLayout.CENTER);

 showPropertiesPage(); // builds the property table first (Client Mode needs it)
 showDashboardPage();  // the agent lands on My Dashboard after login

 return mainArea;
 }

 // PROPERTY PAGE
 private void showPropertiesPage() {

 pageHost.removeAll();

 contentPanel = new JPanel();

 contentPanel.setBackground(page);

 contentPanel.setLayout(
 new BoxLayout(contentPanel, BoxLayout.Y_AXIS)
 );

 contentPanel.setBorder(
 new EmptyBorder(25, 25, 25, 25)
 );

 contentPanel.add(createHeroBanner());

 contentPanel.add(Box.createVerticalStrut(20));

 statsPanel = createStatistics();

 contentPanel.add(statsPanel);

 contentPanel.add(Box.createVerticalStrut(20));

 contentPanel.add(createSearchPanel());

 contentPanel.add(Box.createVerticalStrut(20));

 contentPanel.add(createNotebookPanel());

 // MAIN SCROLL: VERTICAL ONLY
 mainScroll = new JScrollPane(
 contentPanel,
 JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
 JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
 );

 mainScroll.setBorder(null);

 mainScroll.setBackground(page);

 mainScroll.getViewport().setBackground(page);

 mainScroll.getVerticalScrollBar().setUnitIncrement(18);

 // Keep the content width equal to the available viewport width.
 mainScroll.getViewport().addComponentListener(
 new ComponentAdapter() {

 @Override
 public void componentResized(ComponentEvent e) {

 int width = mainScroll.getViewport().getWidth();

 if (width > 0) {

 contentPanel.setPreferredSize(
 new Dimension(width, contentPanel.getPreferredSize().height)
 );

 contentPanel.revalidate();

 adjustStatisticsLayout(width);
 }
 }
 });

 pageHost.add(mainScroll, BorderLayout.CENTER);

 loadProperties(); // get this agent's listings from MySQL

 updateStatistics();

 searchProperties();

 updateModeControls();

 pageHost.revalidate();

 pageHost.repaint();
 }

 // HERO BANNER
 private JPanel createHeroBanner() {

 JPanel hero = new JPanel(new BorderLayout()) {

 private static final long serialVersionUID = 1L;

 @Override
 protected void paintComponent(Graphics g) {

 super.paintComponent(g);

 Graphics2D g2 = (Graphics2D) g.create();

 g2.setRenderingHint(
 RenderingHints.KEY_ANTIALIASING,
 RenderingHints.VALUE_ANTIALIAS_ON
 );

 int w = getWidth();

 int h = getHeight();

 g2.setPaint(
 new GradientPaint(
 0, 0, new Color(35, 37, 44),
 w, h, new Color(65, 59, 45)
 )
 );

 g2.fillRoundRect(
 0, 0, w, h, 25, 25
 );

 if (bannerImage != null) {

 java.awt.Shape oldClip = g2.getClip();

 g2.setClip(
 w / 2, 0, w / 2, h
 );

 g2.drawImage(
 bannerImage,
 w / 2, 0,
 w / 2, h,
 null
 );

 g2.setClip(oldClip);

 } else {

 g2.setColor(
 new Color(205, 169, 96, 40)
 );

 g2.fillOval(
 w - 480, 45, 550, 170
 );

 drawHouse(g2, w - 400, 35);

 drawHouse(g2, w - 220, 70);
 }

 g2.setColor(gold);

 g2.fillRoundRect(
 25, h - 10, 185, 5, 5, 5
 );

 g2.dispose();
 }

 private void drawHouse(
 Graphics2D g2, int x, int y) {

 Graphics2D h2 = (Graphics2D) g2.create();

 h2.translate(x, y);

 h2.setColor(new Color(13, 15, 20));

 h2.fillRect(20, 70, 170, 100);

 h2.setColor(new Color(205, 169, 96));

 h2.setStroke(
 new java.awt.BasicStroke(4f)
 );

 int[] xs = {5, 105, 205};

 int[] ys = {75, 5, 75};

 h2.drawPolyline(xs, ys, 3);

 h2.fillRect(45, 105, 35, 65);

 h2.fillRect(105, 105, 35, 65);

 h2.dispose();
 }
 };

 hero.setOpaque(false);

 hero.setPreferredSize(
 new Dimension(100, 225)
 );

 hero.setMinimumSize(
 new Dimension(100, 180)
 );

 hero.setMaximumSize(
 new Dimension(Integer.MAX_VALUE, 225)
 );

 hero.setBorder(
 new EmptyBorder(35, 35, 25, 25)
 );

 JPanel textPanel = new JPanel();

 textPanel.setOpaque(false);

 textPanel.setLayout(
 new BoxLayout(textPanel, BoxLayout.Y_AXIS)
 );

 JLabel title = new JLabel("PROPERTY LISTINGS");

 title.setFont(
 new Font("Arial", Font.BOLD, 34)
 );

 title.setForeground(Color.WHITE);

 JLabel subtitle = new JLabel(
 "Manage and organize your property listings."
 );

 subtitle.setFont(
 new Font("Arial", Font.PLAIN, 17)
 );

 subtitle.setForeground(goldLight);

 textPanel.add(title);

 textPanel.add(Box.createVerticalStrut(12));

 textPanel.add(subtitle);

 hero.add(textPanel, BorderLayout.WEST);

 return hero;
 }

 // STATISTICS
 private JPanel createStatistics() {

 JPanel stats = new JPanel(
 new GridLayout(1, 4, 14, 14)
 );

 stats.setOpaque(false);

 stats.setMaximumSize(
 new Dimension(Integer.MAX_VALUE, 190)
 );

 stats.setPreferredSize(
 new Dimension(100, 130)
 );

 stats.setMinimumSize(
 new Dimension(100, 110)
 );

 propertyCount = new JLabel("0");

 clientCount = new JLabel("0");

 appointmentCount = new JLabel("0");

 dealCount = new JLabel("0");

 stats.add(createStatCard(
 "⌂", "Total Properties", propertyCount
 ));

 stats.add(createStatCard(
 "♙", "Total Clients", clientCount
 ));

 stats.add(createStatCard(
 "▦", "Appointments", appointmentCount
 ));

 stats.add(createStatCard(
 "⇄", "Completed Deals", dealCount
 ));

 return stats;
 }

 // RESPONSIVE STATISTICS
 private void adjustStatisticsLayout(int width) {

 if (statsPanel == null) return;

 if (width < 750) {

 statsPanel.setLayout(
 new GridLayout(2, 2, 10, 10)
 );

 statsPanel.setPreferredSize(
 new Dimension(width, 230)
 );

 } else {

 statsPanel.setLayout(
 new GridLayout(1, 4, 14, 14)
 );

 statsPanel.setPreferredSize(
 new Dimension(width, 130)
 );
 }

 statsPanel.revalidate();
 }

 private JPanel createStatCard(
 String icon, String title, JLabel number) {

 JPanel card = new JPanel(
 new BorderLayout(10, 0)
 );

 card.setBackground(new Color(36, 39, 47));

 card.setBorder(
 BorderFactory.createCompoundBorder(
 BorderFactory.createLineBorder(
 new Color(105, 87, 58)
 ),
 new EmptyBorder(18, 15, 18, 15)
 )
 );

 JLabel iconLabel = new JLabel(
 icon, SwingConstants.CENTER
 );

 iconLabel.setFont(
 new Font("Arial", Font.BOLD, 27)
 );

 iconLabel.setForeground(gold);

 iconLabel.setPreferredSize(
 new Dimension(40, 45)
 );

 JPanel text = new JPanel();

 text.setOpaque(false);

 text.setLayout(
 new BoxLayout(text, BoxLayout.Y_AXIS)
 );

 JLabel titleLabel = new JLabel(title);

 titleLabel.setFont(
 new Font("Arial", Font.PLAIN, 13)
 );

 titleLabel.setForeground(muted);

 number.setFont(
 new Font("Arial", Font.BOLD, 27)
 );

 number.setForeground(goldLight);

 text.add(titleLabel);

 text.add(Box.createVerticalStrut(5));

 text.add(number);

 card.add(iconLabel, BorderLayout.WEST);

 card.add(text, BorderLayout.CENTER);

 return card;
 }

 // SEARCH PANEL
 private JPanel createSearchPanel() {

 JPanel wrapper = new JPanel(
 new BorderLayout(0, 10)
 );

 wrapper.setBackground(dark2);

 wrapper.setBorder(
 BorderFactory.createCompoundBorder(
 BorderFactory.createLineBorder(line),
 new EmptyBorder(17, 17, 15, 17)
 )
 );

 wrapper.setMaximumSize(
 new Dimension(Integer.MAX_VALUE, 150)
 );

 wrapper.setMinimumSize(
 new Dimension(100, 120)
 );

 // RESPONSIVE SEARCH GRID
 JPanel controls = new JPanel(
 new GridLayout(2, 3, 10, 8)
 );

 controls.setOpaque(false);

 searchField = new HintTextField(
 "Search title, location, or ID..."
 );

 styleTextField(searchField);

 typeFilter = new JComboBox<String>(
 new String[] {
 "All Types",
 "House and Lot",
 "Lot Only",
 "Condominium"
 }
 );

 styleCombo(typeFilter);

 minPriceField = makeFilterField("Min Price");

 maxPriceField = makeFilterField("Max Price");

 JButton search = makeButton(
 "⌕ Search", gold, dark
 );

 JButton reset = makeButton(
 "↻ Reset",
 new Color(92, 99, 108),
 Color.WHITE
 );

 // Components automatically share the available width.
 searchField.setMinimumSize(new Dimension(50, 40));

 typeFilter.setMinimumSize(new Dimension(50, 40));

 minPriceField.setMinimumSize(new Dimension(50, 40));

 maxPriceField.setMinimumSize(new Dimension(50, 40));

 search.setMinimumSize(new Dimension(50, 40));

 reset.setMinimumSize(new Dimension(50, 40));

 controls.add(searchField);

 controls.add(typeFilter);

 controls.add(minPriceField);

 controls.add(maxPriceField);

 controls.add(search);

 controls.add(reset);

 filterStatus = new JLabel(
 "Showing 0 property records"
 );

 filterStatus.setFont(
 new Font("Arial", Font.PLAIN, 12)
 );

 filterStatus.setForeground(muted);

 wrapper.add(controls, BorderLayout.CENTER);

 wrapper.add(filterStatus, BorderLayout.SOUTH);

 search.addActionListener(e -> searchProperties());

 reset.addActionListener(e -> resetFilters());

 searchField.addActionListener(e -> searchProperties());

 minPriceField.addActionListener(e -> searchProperties());

 maxPriceField.addActionListener(e -> searchProperties());

 return wrapper;
 }

 private JTextField makeFilterField(String hint) {

 JTextField field = new HintTextField(hint);

 styleTextField(field);

 return field;
 }

 private void styleTextField(JTextField field) {

 field.setFont(
 new Font("Arial", Font.PLAIN, 13)
 );

 field.setForeground(Color.WHITE);

 field.setBackground(new Color(43, 46, 55));

 field.setCaretColor(gold);

 field.setBorder(
 BorderFactory.createCompoundBorder(
 BorderFactory.createLineBorder(line),
 new EmptyBorder(7, 11, 7, 11)
 )
 );
 }

 private void styleCombo(JComboBox<String> combo) {

 combo.setFont(
 new Font("Arial", Font.PLAIN, 13)
 );

 combo.setBackground(new Color(43, 46, 55));

 combo.setForeground(Color.WHITE);

 combo.setFocusable(false);
 }

 // PLACEHOLDER FIELD
 private static class HintTextField extends JTextField {

 private static final long serialVersionUID = 1L;

 private final String hint;

 HintTextField(String hint) {

 this.hint = hint;

 addFocusListener(
 new java.awt.event.FocusAdapter() {

 @Override
 public void focusGained(
 java.awt.event.FocusEvent e) {
 repaint();
 }

 @Override
 public void focusLost(
 java.awt.event.FocusEvent e) {
 repaint();
 }
 });
 }

 @Override
 protected void paintComponent(Graphics g) {

 super.paintComponent(g);

 if (getText().isEmpty() && !isFocusOwner()) {

 Graphics2D g2 = (Graphics2D) g.create();

 g2.setRenderingHint(
 RenderingHints.KEY_TEXT_ANTIALIASING,
 RenderingHints.VALUE_TEXT_ANTIALIAS_ON
 );

 g2.setColor(new Color(170, 174, 183));

 g2.setFont(getFont());

 java.awt.Insets insets = getInsets();

 g2.drawString(
 hint,
 insets.left + 1,
 getHeight() / 2
 + g2.getFontMetrics().getAscent() / 2 - 2
 );

 g2.dispose();
 }
 }
 }

 // PROPERTY RECORDS
 private JPanel createNotebookPanel() {

 JPanel notebook = new JPanel(
 new BorderLayout(0, 15)
 );

 notebook.setBackground(dark2);

 notebook.setBorder(
 BorderFactory.createCompoundBorder(
 BorderFactory.createLineBorder(line),
 new EmptyBorder(20, 20, 18, 20)
 )
 );

 notebook.setPreferredSize(
 new Dimension(100, 430)
 );

 notebook.setMinimumSize(
 new Dimension(100, 350)
 );

 notebook.setMaximumSize(
 new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE)
 );

 JPanel top = new JPanel(new BorderLayout());

 top.setOpaque(false);

 JLabel heading = new JLabel("⌂ Property Records");

 heading.setFont(
 new Font("Arial", Font.BOLD, 22)
 );

 heading.setForeground(Color.WHITE);

 addButton = makeButton(
 "+ Add Property", gold, dark
 );

 addButton.addActionListener(e -> addProperty());

 deleteTopButton = makeButton(
 "Delete Selected",
 new Color(173, 66, 66),
 Color.WHITE
 );

 deleteTopButton.addActionListener(
 e -> deleteProperty()
 );

 JPanel topActions = new JPanel(
 new FlowLayout(FlowLayout.RIGHT, 9, 5)
 );

 topActions.setOpaque(false);

 topActions.add(deleteTopButton);

 topActions.add(addButton);

 top.add(heading, BorderLayout.WEST);

 top.add(topActions, BorderLayout.EAST);

 notebook.add(top, BorderLayout.NORTH);

 String[] columns = {
 "ID",
 "Title",
 "Location",
 "Property Type",
 "Price",
 "Status"
 };

 tableModel = new DefaultTableModel(columns, 0) {

 private static final long serialVersionUID = 1L;

 @Override
 public boolean isCellEditable(int row, int column) {
 return false;
 }
 };

 propertyTable = new JTable(tableModel);

 propertyTable.setRowHeight(47);

 propertyTable.setFont(
 new Font("Arial", Font.PLAIN, 13)
 );

 propertyTable.setForeground(Color.WHITE);

 propertyTable.setBackground(dark);

 propertyTable.setSelectionBackground(
 new Color(91, 77, 51)
 );

 propertyTable.setSelectionForeground(Color.WHITE);

 propertyTable.setShowGrid(true);

 propertyTable.setGridColor(line);

 propertyTable.setIntercellSpacing(
 new Dimension(1, 1)
 );

 propertyTable.setFillsViewportHeight(true);

 propertyTable.setAutoCreateRowSorter(false);

 // IMPORTANT: Automatically resize all table columns.
 propertyTable.setAutoResizeMode(
 JTable.AUTO_RESIZE_ALL_COLUMNS
 );

 JTableHeader header = propertyTable.getTableHeader();

 header.setPreferredSize(
 new Dimension(0, 46)
 );

 header.setReorderingAllowed(false);

 header.setDefaultRenderer(new HeaderRenderer());

 DefaultTableCellRenderer center =
 new DefaultTableCellRenderer();

 center.setHorizontalAlignment(
 SwingConstants.CENTER
 );

 propertyTable.getColumnModel()
 .getColumn(0).setCellRenderer(center);

 propertyTable.getColumnModel()
 .getColumn(4).setCellRenderer(new PriceRenderer());

 propertyTable.getColumnModel()
 .getColumn(5).setCellRenderer(new StatusRenderer());

 // PROPORTIONAL COLUMN WIDTHS
 propertyTable.getColumnModel()
 .getColumn(0).setPreferredWidth(65);

 propertyTable.getColumnModel()
 .getColumn(1).setPreferredWidth(180);

 propertyTable.getColumnModel()
 .getColumn(2).setPreferredWidth(180);

 propertyTable.getColumnModel()
 .getColumn(3).setPreferredWidth(140);

 propertyTable.getColumnModel()
 .getColumn(4).setPreferredWidth(120);

 propertyTable.getColumnModel()
 .getColumn(5).setPreferredWidth(100);

 // TABLE SCROLL: VERTICAL ONLY
 JScrollPane tableScroll = new JScrollPane(
 propertyTable,
 JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
 JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
 );

 tableScroll.setBorder(
 BorderFactory.createLineBorder(line)
 );

 tableScroll.getViewport().setBackground(dark);

 // Keep table inside available width.
 tableScroll.setPreferredSize(
 new Dimension(500, 270)
 );

 notebook.add(tableScroll, BorderLayout.CENTER);

 JPanel bottom = new JPanel(
 new FlowLayout(FlowLayout.RIGHT, 9, 0)
 );

 bottom.setOpaque(false);

 viewButton = makeButton(
 "View Details",
 new Color(55, 103, 145),
 Color.WHITE
 );

 editButton = makeButton(
 "Edit",
 new Color(190, 143, 54),
 Color.WHITE
 );

 deleteButton = makeButton(
 "Delete",
 new Color(173, 66, 66),
 Color.WHITE
 );

 viewButton.addActionListener(e -> viewProperty());

 editButton.addActionListener(e -> editProperty());

 deleteButton.addActionListener(e -> deleteProperty());

 bottom.add(viewButton);

 bottom.add(editButton);

 bottom.add(deleteButton);

 notebook.add(bottom, BorderLayout.SOUTH);

 return notebook;
 }

 // TABLE HEADER
 private class HeaderRenderer
 extends DefaultTableCellRenderer {

 private static final long serialVersionUID = 1L;

 HeaderRenderer() {

 setOpaque(true);

 setBackground(dark);

 setForeground(gold);

 setFont(new Font("Arial", Font.BOLD, 13));

 setHorizontalAlignment(
 SwingConstants.CENTER
 );

 setBorder(
 BorderFactory.createMatteBorder(
 0, 0, 2, 1, gold
 )
 );
 }

 @Override
 public Component getTableCellRendererComponent(
 JTable table, Object value,
 boolean selected, boolean focus,
 int row, int col) {

 super.getTableCellRendererComponent(
 table, value, selected, focus, row, col
 );

 setBackground(dark);

 setForeground(gold);

 setHorizontalAlignment(
 SwingConstants.CENTER
 );

 return this;
 }
 }

 // PRICE RENDERER
 private class PriceRenderer
 extends DefaultTableCellRenderer {

 private static final long serialVersionUID = 1L;

 @Override
 public Component getTableCellRendererComponent(
 JTable table, Object value,
 boolean selected, boolean focus,
 int row, int col) {

 super.getTableCellRendererComponent(
 table, value, selected, focus, row, col
 );

 if (value instanceof Number) {

 setText("₱" + money.format(
 ((Number) value).doubleValue()
 ));
 }

 setForeground(Color.WHITE);

 if (!selected) {
 setBackground(dark);
 }

 return this;
 }
 }

 // STATUS RENDERER
 private class StatusRenderer
 extends DefaultTableCellRenderer {

 private static final long serialVersionUID = 1L;

 @Override
 public Component getTableCellRendererComponent(
 JTable table, Object value,
 boolean selected, boolean focus,
 int row, int col) {

 super.getTableCellRendererComponent(
 table, value, selected, focus, row, col
 );

 String status = value == null
 ? "" : value.toString();

 setHorizontalAlignment(
 SwingConstants.CENTER
 );

 setFont(
 new Font("Arial", Font.BOLD, 12)
 );

 if ("Available".equals(status)) {

 setBackground(new Color(36, 88, 66));

 setForeground(new Color(145, 230, 179));

 } else if ("Reserved".equals(status)) {

 setBackground(new Color(100, 72, 35));

 setForeground(new Color(255, 207, 117));

 } else {

 setBackground(new Color(65, 69, 76));

 setForeground(new Color(210, 215, 220));
 }

 return this;
 }
 }

 // REFRESH TABLE
 private void refreshTable() {

 if (tableModel == null) return;

 searchProperties();

 updateStatistics();

 updateModeControls();
 }

 private void addPropertyRow(Property p) {

 tableModel.addRow(new Object[] {
 p.id,
 p.title,
 p.location,
 p.type,
 Double.valueOf(p.price),
 p.status
 });
 }

 // STATISTICS
 private void updateStatistics() {

 if (propertyCount != null) {
 propertyCount.setText(
 String.valueOf(properties.size())
 );
 }

 if (clientCount != null) {
 clientCount.setText("0");
 }

 if (appointmentCount != null) {
 appointmentCount.setText("0");
 }

 if (dealCount != null) {
 dealCount.setText("0");
 }
 }

 // RESET FILTERS
 private void resetFilters() {

 searchField.setText("");

 minPriceField.setText("");

 maxPriceField.setText("");

 typeFilter.setSelectedIndex(0);

 searchProperties();
 }

 // PRICE PARSER
 private double parsePrice(
 String value, double defaultValue) {

 String cleaned = value.trim()
 .replace(",", "")
 .replace("₱", "")
 .replace("PHP", "")
 .trim();

 return cleaned.isEmpty()
 ? defaultValue
 : Double.parseDouble(cleaned);
 }

 // SEARCH PROPERTY
 private void searchProperties() {

 if (tableModel == null) return;

 try {

 String keyword = searchField.getText()
 .trim().toLowerCase();

 String selectedType =
 typeFilter.getSelectedItem() == null
 ? "All Types"
 : typeFilter.getSelectedItem().toString();

 double min = parsePrice(
 minPriceField.getText(), 0
 );

 double max = parsePrice(
 maxPriceField.getText(),
 Double.MAX_VALUE
 );

 if (min < 0 || max < 0 || min > max) {

 JOptionPane.showMessageDialog(
 this,
 "Please enter a valid price range.",
 "Invalid Price Range",
 JOptionPane.WARNING_MESSAGE
 );

 return;
 }

 tableModel.setRowCount(0);

 int matches = 0;

 for (Property p : properties) {

 String searchable =
 (p.id + " " + p.title + " "
 + p.location).toLowerCase();

 boolean keywordMatch =
 keyword.isEmpty()
 || searchable.contains(keyword);

 boolean typeMatch =
 "All Types".equals(selectedType)
 || p.type.equalsIgnoreCase(selectedType);

 boolean priceMatch =
 p.price >= min && p.price <= max;

 boolean modeMatch =
 !clientMode
 || "Available".equals(p.status);

 if (keywordMatch
 && typeMatch
 && priceMatch
 && modeMatch) {

 addPropertyRow(p);

 matches++;
 }
 }

 if (filterStatus != null) {

 if (matches == 0) {

 filterStatus.setText(
 "No matching properties found."
 );

 } else {

 filterStatus.setText(
 "Showing " + matches
 + " matching record"
 + (matches == 1 ? "" : "s")
 );
 }
 }

 } catch (NumberFormatException ex) {

 JOptionPane.showMessageDialog(
 this,
 "Enter numbers only in the minimum "
 + "and maximum price fields.",
 "Invalid Price",
 JOptionPane.WARNING_MESSAGE
 );
 }
 }

 // ADD PROPERTY
 private void addProperty() {

 JTextField title = new JTextField();

 JTextField location = new JTextField();

 JTextField price = new JTextField();

 JComboBox<String> type =
 new JComboBox<String>(
 new String[] {
 "House and Lot",
 "Lot Only",
 "Condominium"
 }
 );

 JComboBox<String> status =
 new JComboBox<String>(
 new String[] {
 "Available",
 "Reserved",
 "Sold"
 }
 );

 Object[] fields = {
 "Property Title:", title,
 "Location:", location,
 "Property Type:", type,
 "Price (PHP):", price,
 "Status:", status
 };

 int result = JOptionPane.showConfirmDialog(
 this,
 fields,
 "Add Property",
 JOptionPane.OK_CANCEL_OPTION
 );

 if (result != JOptionPane.OK_OPTION) return;

 // BACKEND: PropertyService checks the fields (try/catch for the price) and saves to MySQL
 PropertyService propertyService = AppContext.propertyService();

 boolean saved = propertyService.addProperty(
 user,
 title.getText(),
 location.getText(),
 type.getSelectedItem().toString(),
 price.getText(),
 status.getSelectedItem().toString()
 );

 if (saved) {
 reloadFromDatabase();
 }

 JOptionPane.showMessageDialog(this, propertyService.getMessage());
 }

 // EDIT PROPERTY
 private void editProperty() {

 int row = propertyTable.getSelectedRow();

 if (row < 0) {

 JOptionPane.showMessageDialog(
 this,
 "Select a property first."
 );

 return;
 }

 String id = tableModel.getValueAt(row, 0).toString();

 Property p = findProperty(id);

 if (p == null) return;

 JTextField title = new JTextField(p.title);

 JTextField location = new JTextField(p.location);

 JTextField price = new JTextField(
 String.valueOf(p.price)
 );

 JComboBox<String> type =
 new JComboBox<String>(
 new String[] {
 "House and Lot",
 "Lot Only",
 "Condominium"
 }
 );

 type.setSelectedItem(p.type);

 JComboBox<String> status =
 new JComboBox<String>(
 new String[] {
 "Available",
 "Reserved",
 "Sold"
 }
 );

 status.setSelectedItem(p.status);

 Object[] fields = {
 "Property Title:", title,
 "Location:", location,
 "Property Type:", type,
 "Price (PHP):", price,
 "Status:", status
 };

 int result = JOptionPane.showConfirmDialog(
 this,
 fields,
 "Edit Property",
 JOptionPane.OK_CANCEL_OPTION
 );

 if (result != JOptionPane.OK_OPTION) return;

 // BACKEND: PropertyService checks the fields and the owner, then updates MySQL
 PropertyService propertyService = AppContext.propertyService();

 boolean saved = propertyService.updateProperty(
 user,
 toDatabaseId(p.id),
 title.getText(),
 location.getText(),
 type.getSelectedItem().toString(),
 price.getText(),
 status.getSelectedItem().toString()
 );

 if (saved) {
 reloadFromDatabase();
 }

 JOptionPane.showMessageDialog(this, propertyService.getMessage());
 }

 // VIEW DETAILS
 private void viewProperty() {

 int row = propertyTable.getSelectedRow();

 if (row < 0) {

 JOptionPane.showMessageDialog(
 this,
 "Select a property first."
 );

 return;
 }

 String id = tableModel.getValueAt(row, 0).toString();

 Property p = findProperty(id);

 if (p == null) return;

 JOptionPane.showMessageDialog(
 this,
 "PROPERTY DETAILS\n\n"
 + "Property ID: " + p.id + "\n"
 + "Title: " + p.title + "\n"
 + "Location: " + p.location + "\n"
 + "Type: " + p.type + "\n"
 + "Price: PHP " + money.format(p.price) + "\n"
 + "Status: " + p.status,
 "Property Details",
 JOptionPane.INFORMATION_MESSAGE
 );
 }

 // DELETE PROPERTY
 private void deleteProperty() {

 int row = propertyTable.getSelectedRow();

 if (row < 0) {

 JOptionPane.showMessageDialog(
 this,
 "Select a property first."
 );

 return;
 }

 String id = tableModel.getValueAt(row, 0).toString();

 Property selectedProperty = findProperty(id);

 if (selectedProperty == null) return;

 int answer = JOptionPane.showConfirmDialog(
 this,
 "Are you sure you want to delete this property?\n\n"
 + selectedProperty.title + " ("
 + selectedProperty.id + ")\n\n"
 + "This action cannot be undone.",
 "Confirm Property Deletion",
 JOptionPane.YES_NO_OPTION,
 JOptionPane.WARNING_MESSAGE
 );

 if (answer == JOptionPane.YES_OPTION) {

 // BACKEND: PropertyService checks the owner, then deletes from MySQL
 PropertyService propertyService = AppContext.propertyService();

 if (propertyService.deleteProperty(user, toDatabaseId(id))) {
 reloadFromDatabase();
 }

 JOptionPane.showMessageDialog(this, propertyService.getMessage());
 }
 }

 // ================= MYSQL CONNECTION FOR THE PROPERTIES PAGE =================
 // The table still shows this screen's small Property objects, but they are now
 // filled from MySQL through PropertyService instead of being typed in by hand.
 private void loadProperties() {

 properties.clear();

 if (user == null) {
 return; // opened without login (WindowBuilder / main method)
 }

 // full name because this class also has its own small Property class
 for (com.hanapbahay.model.Property listing : AppContext.propertyService().getListings(user)) {
 properties.add(new Property(
 toDisplayId(listing.getPropertyId()),
 listing.getTitle(),
 listing.getLocation(),
 toDisplayType(listing.getType()),
 listing.getPrice(),
 toDisplayStatus(listing.getStatus())
 ));
 }
 }

 // database id 1 -> "P001" on screen
 private String toDisplayId(int propertyId) {
 return String.format("P%03d", propertyId);
 }

 // "P001" on screen -> database id 1
 private int toDatabaseId(String displayId) {
 return Integer.parseInt(displayId.substring(1));
 }

 // The first sample rows in schema.sql use short names; show them like the dropdown choices
 private String toDisplayType(String type) {
 if ("House".equalsIgnoreCase(type)) return "House and Lot";
 if ("Condo".equalsIgnoreCase(type)) return "Condominium";
 if ("Lot".equalsIgnoreCase(type)) return "Lot Only";
 return type;
 }

 // reload from MySQL, then redraw the table and counters
 private void reloadFromDatabase() {
 loadProperties();
 refreshTable();
 }

 // FIND PROPERTY
 private Property findProperty(String id) {

 for (Property p : properties) {

 if (p.id.equals(id)) {
 return p;
 }
 }

 return null;
 }

 // ================= MY DASHBOARD PAGE (real data from MySQL) =================
 // Shows ONLY this agent's listings and sales numbers.
 // DashboardService.getSummary(user) filters by agent because Agent.canViewAllSales() is false.
 private void showDashboardPage() {

 pageHost.removeAll();

 JPanel content = new JPanel();
 content.setBackground(page);
 content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
 content.setBorder(new EmptyBorder(25, 25, 25, 25));

 JLabel title = new JLabel("My Dashboard");
 title.setFont(new Font("Serif", Font.BOLD, 30));
 title.setForeground(ink);
 title.setAlignmentX(Component.LEFT_ALIGNMENT);
 content.add(title);

 JLabel scope = new JLabel(" ");
 scope.setFont(new Font("Arial", Font.PLAIN, 13));
 scope.setForeground(muted);
 scope.setBorder(new EmptyBorder(6, 0, 20, 0));
 scope.setAlignmentX(Component.LEFT_ALIGNMENT);
 content.add(scope);

 // the four cards
 JLabel myListings = new JLabel("0");
 JLabel myInquiries = new JLabel("0");
 JLabel mySales = new JLabel("0");
 JLabel mySalesAmount = new JLabel("₱0.00");

 JPanel cards = new JPanel(new GridLayout(1, 4, 15, 0));
 cards.setOpaque(false);
 cards.setAlignmentX(Component.LEFT_ALIGNMENT);
 cards.setPreferredSize(new Dimension(600, 105)); // small so there is no sideways scrollbar
 cards.setMaximumSize(new Dimension(Integer.MAX_VALUE, 105));
 cards.add(createStatCard("⌂", "My Listings", myListings));
 cards.add(createStatCard("●", "Client Inquiries", myInquiries));
 cards.add(createStatCard("■", "My Sales", mySales));
 cards.add(createStatCard("₱", "My Total Sales", mySalesAmount));
 content.add(cards);
 content.add(Box.createVerticalStrut(28));

 // table 1: my listings
 content.add(createDashboardSectionTitle("My Listings"));
 String[] listingColumns = { "Title", "Location", "Type", "Price", "Status" };
 DefaultTableModel listingModel = createReadOnlyModel(listingColumns);
 JTable listingTable = createDashboardTable(listingModel);
 listingTable.getColumnModel().getColumn(3).setCellRenderer(new PriceRenderer());
 listingTable.getColumnModel().getColumn(4).setCellRenderer(new StatusRenderer());
 content.add(wrapDashboardTable(listingTable, 230));

 // ---- fill everything from the backend ----
 if (user == null) {
 scope.setText("Log in as an agent to see your data.");
 } else {
 DashboardService dashboardService = AppContext.dashboardService();
 DashboardSummary summary = dashboardService.getSummary(user);

 scope.setText(summary.getScopeLabel());
 myListings.setText(summary.getTotalListings() + "");
 myInquiries.setText(summary.getTotalInquiries() + "");
 mySales.setText(summary.getTotalSales() + "");
 mySalesAmount.setText("₱" + money.format(summary.getTotalSalesAmount()));

 // full name because this class also has its own small Property class
 ArrayList<com.hanapbahay.model.Property> listings = AppContext.propertyService().getListings(user);
 for (com.hanapbahay.model.Property listing : listings) {
 listingModel.addRow(new Object[] {
 listing.getTitle(), listing.getLocation(), toDisplayType(listing.getType()),
 listing.getPrice(), toDisplayStatus(listing.getStatus()) });
 }
 if (listings.isEmpty()) {
 listingModel.addRow(new Object[] { "No listings yet", "", "", "", "" });
 }
 }

 JScrollPane scroll = new JScrollPane(
 content,
 JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
 JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
 );
 scroll.setBorder(null);
 scroll.getViewport().setBackground(page);
 scroll.getVerticalScrollBar().setUnitIncrement(18);

 pageHost.add(scroll, BorderLayout.CENTER);
 pageHost.revalidate();
 pageHost.repaint();
 }

 // "AVAILABLE" from the database -> "Available" (the StatusRenderer colors use this form)
 private String toDisplayStatus(String status) {
 if (status == null || status.isEmpty()) {
 return "";
 }
 return status.substring(0, 1) + status.substring(1).toLowerCase();
 }

 private JLabel createDashboardSectionTitle(String text) {
 JLabel label = new JLabel(text);
 label.setFont(new Font("Arial", Font.BOLD, 17));
 label.setForeground(gold);
 label.setBorder(new EmptyBorder(0, 0, 12, 0));
 label.setAlignmentX(Component.LEFT_ALIGNMENT);
 return label;
 }

 private DefaultTableModel createReadOnlyModel(String[] columns) {
 return new DefaultTableModel(columns, 0) {
 private static final long serialVersionUID = 1L;

 @Override
 public boolean isCellEditable(int row, int column) {
 return false;
 }
 };
 }

 // Same look as the Property Records table
 private JTable createDashboardTable(DefaultTableModel model) {
 JTable table = new JTable(model);
 table.setRowHeight(42);
 table.setFont(new Font("Arial", Font.PLAIN, 13));
 table.setForeground(Color.WHITE);
 table.setBackground(dark);
 table.setSelectionBackground(new Color(91, 77, 51));
 table.setSelectionForeground(Color.WHITE);
 table.setGridColor(line);
 table.setFillsViewportHeight(true);

 JTableHeader header = table.getTableHeader();
 header.setPreferredSize(new Dimension(0, 42));
 header.setReorderingAllowed(false);
 header.setDefaultRenderer(new HeaderRenderer());
 return table;
 }

 private JScrollPane wrapDashboardTable(JTable table, int height) {
 JScrollPane scroll = new JScrollPane(table);
 scroll.setBorder(BorderFactory.createLineBorder(new Color(105, 87, 58)));
 scroll.getViewport().setBackground(dark);
 scroll.setAlignmentX(Component.LEFT_ALIGNMENT);
 scroll.setPreferredSize(new Dimension(600, height));
 scroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
 return scroll;
 }

 // AGENT / CLIENT MODE
 private void toggleClientMode() {

 // Leaving Client Mode needs the agent's password, so the client cannot go back to Agent Mode
 if (clientMode && !askAgentPassword()) {
 return;
 }

 clientMode = !clientMode;

 // Client Mode: hide the side menu (pages and Log Out) so the client only sees the properties
 sidebarPanel.setVisible(!clientMode);

 if (clientMode) {
 showPropertiesPage(); // never leave the agent's own dashboard on screen for the client
 }
 getContentPane().revalidate();
 getContentPane().repaint();

 if (clientMode) {

 modeButton.setText(
 "● Client Browse Mode ▼"
 );

 } else {

 modeButton.setText(
 "● Agent Mode ▼"
 );
 }

 refreshTable();

 JOptionPane.showMessageDialog(
 this,
 clientMode
 ? "Client Browse Mode is active.\n"
 + "Only available properties are shown."
 : "Agent Mode is active.\n"
 + "You can manage all property records."
 );
 }

 // Asks for the logged-in agent's password. Returns true if it is correct.
 private boolean askAgentPassword() {

 if (user == null) {
 return true; // opened without login (WindowBuilder / main method)
 }

 JPasswordField passwordField = new JPasswordField();

 int answer = JOptionPane.showConfirmDialog(
 this,
 new Object[] { "Agent password to exit Client Mode:", passwordField },
 "Exit Client Mode",
 JOptionPane.OK_CANCEL_OPTION,
 JOptionPane.PLAIN_MESSAGE
 );

 if (answer != JOptionPane.OK_OPTION) {
 return false;
 }

 // BACKEND: check the password against the MySQL "users" table
 String password = new String(passwordField.getPassword());
 User checked = AppContext.userService().login(user.getUsername(), password);

 if (checked == null) {
 JOptionPane.showMessageDialog(
 this,
 "Wrong password. Client Mode stays on.",
 "Exit Client Mode",
 JOptionPane.WARNING_MESSAGE
 );
 return false;
 }

 return true;
 }

 private void updateModeControls() {

 // Client Mode: hide the agent's counters (properties, clients, appointments, deals)
 if (statsPanel != null) {
 statsPanel.setVisible(!clientMode);
 }

 if (addButton != null) {
 addButton.setVisible(!clientMode);
 }

 if (editButton != null) {
 editButton.setVisible(!clientMode);
 }

 if (deleteButton != null) {
 deleteButton.setVisible(!clientMode);
 }

 if (deleteTopButton != null) {
 deleteTopButton.setVisible(!clientMode);
 }
 }

 // PLACEHOLDER PAGES
 private void showPlaceholderPage(String pageName) {

 pageHost.removeAll();

 JPanel placeholder = new JPanel(
 new BorderLayout()
 );

 placeholder.setBackground(page);

 placeholder.setBorder(
 new EmptyBorder(35, 35, 35, 35)
 );

 JPanel card = new JPanel();

 card.setBackground(dark2);

 card.setLayout(
 new BoxLayout(card, BoxLayout.Y_AXIS)
 );

 card.setBorder(
 new EmptyBorder(35, 35, 35, 35)
 );

 JLabel title = new JLabel(pageName);

 title.setFont(
 new Font("Arial", Font.BOLD, 30)
 );

 title.setForeground(gold);

 JLabel message = new JLabel(
 "The " + pageName
 + " page will be developed next."
 );

 message.setFont(
 new Font("Arial", Font.PLAIN, 15)
 );

 message.setForeground(muted);

 card.add(title);

 card.add(Box.createVerticalStrut(15));

 card.add(message);

 placeholder.add(card, BorderLayout.NORTH);

 pageHost.add(placeholder, BorderLayout.CENTER);

 pageHost.revalidate();

 pageHost.repaint();
 }

 // BUTTON FACTORY
 private JButton makeButton(
 String text, Color background, Color foreground) {

 JButton button = new JButton(text);

 button.setFont(
 new Font("Arial", Font.BOLD, 13)
 );

 button.setForeground(foreground);

 button.setBackground(background);

 button.setOpaque(true);

 button.setContentAreaFilled(true);

 button.setBorderPainted(false);

 button.setFocusPainted(false);

 button.setCursor(
 new Cursor(Cursor.HAND_CURSOR)
 );

 button.setBorder(
 new EmptyBorder(12, 17, 12, 17)
 );

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

 // PROPERTY CLASS
 private static class Property {

 String id;

 String title;

 String location;

 String type;

 double price;

 String status;

 Property(
 String id,
 String title,
 String location,
 String type,
 double price,
 String status) {

 this.id = id;

 this.title = title;

 this.location = location;

 this.type = type;

 this.price = price;

 this.status = status;
 }
 }

 // MAIN METHOD
 public static void main(String[] args) {

 SwingUtilities.invokeLater(() -> {

 try {

 UIManager.setLookAndFeel(
 UIManager.getCrossPlatformLookAndFeelClassName()
 );

 } catch (Exception ex) {

 ex.printStackTrace();
 }

 AgentDashboard frame = new AgentDashboard();

 frame.setVisible(true);
 });
 }
}
