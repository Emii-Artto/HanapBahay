package login;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;

import com.hanapbahay.AppContext;
import com.hanapbahay.model.User;
import com.hanapbahay.service.UserService;

import dash.AdminDashboard;
import dash.AgentDashboard;

public class LogIn extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleCombo;

    // COLORS
    private final Color BACKGROUND = new Color(23, 26, 33);
    private final Color PANEL = new Color(32, 35, 43);
    private final Color GOLD = new Color(198, 161, 91);
    private final Color WHITE = new Color(245, 245, 245);
    private final Color MUTED = new Color(160, 164, 174);
    private final Color FIELD = new Color(43, 46, 55);

    public LogIn() {

        setTitle("Estate Manager - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 620));
        setLocationRelativeTo(null);

        // MAIN PANEL
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.setBackground(BACKGROUND);

        // LEFT PANEL
        PhotoPanel leftPanel = new PhotoPanel("/images/estate-house.jpg");

        leftPanel.setLayout(new BorderLayout());
        leftPanel.setBorder(new EmptyBorder(35, 42, 35, 35));

        JPanel brandPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 0));

        brandPanel.setOpaque(false);

        JLabel logo = new JLabel("\u2302");
        logo.setFont(new Font("Serif", Font.BOLD, 42));
        logo.setForeground(GOLD);

        JLabel brandName = new JLabel("ESTATE MANAGER");
        brandName.setFont(new Font("SansSerif", Font.BOLD, 19));
        brandName.setForeground(WHITE);

        brandPanel.add(logo);
        brandPanel.add(brandName);

        leftPanel.add(brandPanel, BorderLayout.NORTH);

        // LEFT CAPTION
        JPanel leftCaption = new JPanel();

        leftCaption.setOpaque(false);
        leftCaption.setLayout(
                new BoxLayout(leftCaption, BoxLayout.Y_AXIS));

        JLabel smallHeading = new JLabel("FIND YOUR");
        smallHeading.setFont(new Font("SansSerif", Font.BOLD, 16));
        smallHeading.setForeground(GOLD);
        smallHeading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel mainHeading = new JLabel("Perfect Property.");
        mainHeading.setFont(new Font("Serif", Font.BOLD, 38));
        mainHeading.setForeground(WHITE);
        mainHeading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel description = new JLabel(
                "<html>Manage property listings, connect with clients,<br>"
                + "and organize every real estate transaction<br>"
                + "in one convenient platform.</html>");

        description.setFont(new Font("SansSerif", Font.PLAIN, 14));
        description.setForeground(WHITE);
        description.setAlignmentX(Component.LEFT_ALIGNMENT);

        leftCaption.add(smallHeading);
        leftCaption.add(Box.createVerticalStrut(10));
        leftCaption.add(mainHeading);
        leftCaption.add(Box.createVerticalStrut(14));
        leftCaption.add(description);

        leftPanel.add(leftCaption, BorderLayout.SOUTH);

        // RIGHT PANEL
        JPanel rightPanel = new JPanel(new GridBagLayout());

        rightPanel.setBackground(BACKGROUND);
        rightPanel.setBorder(new EmptyBorder(25, 30, 25, 30));

        // LOGIN CARD
        JPanel loginCard = new JPanel();

        loginCard.setLayout(
                new BoxLayout(loginCard, BoxLayout.Y_AXIS));

        loginCard.setBackground(PANEL);

        loginCard.setBorder(new CompoundBorder(
                new LineBorder(new Color(65, 62, 54), 1),
                new EmptyBorder(35, 35, 25, 35)
        ));

        loginCard.setPreferredSize(new Dimension(440, 570));

        // WELCOME TITLE
        JLabel loginTitle = new JLabel("Welcome Back!");

        loginTitle.setFont(
                new Font("Serif", Font.BOLD, 31));

        loginTitle.setForeground(WHITE);
        loginTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginCard.add(loginTitle);
        loginCard.add(Box.createVerticalStrut(8));

        // SUBTITLE
        JLabel loginSubtitle = new JLabel(
                "Log in to access your account.");

        loginSubtitle.setFont(
                new Font("SansSerif", Font.PLAIN, 13));

        loginSubtitle.setForeground(MUTED);
        loginSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginCard.add(loginSubtitle);
        loginCard.add(Box.createVerticalStrut(35));

        // TOP ROW: LOG IN + ACCOUNT TYPE
        JPanel topRow = new JPanel(new BorderLayout());

        topRow.setOpaque(false);

        topRow.setPreferredSize(new Dimension(350, 55));
        topRow.setMinimumSize(new Dimension(350, 55));
        topRow.setMaximumSize(new Dimension(350, 55));

        topRow.setAlignmentX(Component.CENTER_ALIGNMENT);

        // LOG IN - LEFT SIDE
        JLabel loginLabel = new JLabel("LOG IN");

        loginLabel.setFont(
                new Font("SansSerif", Font.BOLD, 15));

        loginLabel.setForeground(WHITE);

        loginLabel.setHorizontalAlignment(SwingConstants.LEFT);

        topRow.add(loginLabel, BorderLayout.WEST);

        // ACCOUNT TYPE - RIGHT SIDE
        JPanel accountTypePanel = new JPanel();

        accountTypePanel.setOpaque(false);

        accountTypePanel.setLayout(
                new BoxLayout(accountTypePanel, BoxLayout.Y_AXIS));

        JLabel roleLabel = new JLabel("ACCOUNT TYPE");

        roleLabel.setFont(
                new Font("SansSerif", Font.BOLD, 11));

        roleLabel.setForeground(GOLD);

        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        accountTypePanel.add(roleLabel);
        accountTypePanel.add(Box.createVerticalStrut(5));

        roleCombo = new JComboBox<>(
                new String[]{"Admin", "Agent"});

        styleSmallComboBox(roleCombo);

        accountTypePanel.add(roleCombo);

        topRow.add(accountTypePanel, BorderLayout.EAST);

        loginCard.add(topRow);
        loginCard.add(Box.createVerticalStrut(15));

        // USERNAME
        loginCard.add(createLabel("USERNAME"));
        loginCard.add(Box.createVerticalStrut(8));

        usernameField = new JTextField();

        styleField(usernameField);

        loginCard.add(usernameField);
        loginCard.add(Box.createVerticalStrut(18));

        // PASSWORD
        loginCard.add(createLabel("PASSWORD"));
        loginCard.add(Box.createVerticalStrut(8));

        passwordField = new JPasswordField();

        styleField(passwordField);

        loginCard.add(passwordField);
        loginCard.add(Box.createVerticalStrut(8));

        // SHOW PASSWORD - LEFT
        JPanel showPasswordPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 0, 0));

        showPasswordPanel.setOpaque(false);

        showPasswordPanel.setPreferredSize(
                new Dimension(350, 25));

        showPasswordPanel.setMinimumSize(
                new Dimension(350, 25));

        showPasswordPanel.setMaximumSize(
                new Dimension(350, 25));

        showPasswordPanel.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        JCheckBox showPassword = new JCheckBox("Show password");

        showPassword.setFont(
                new Font("SansSerif", Font.PLAIN, 12));

        showPassword.setForeground(MUTED);
        showPassword.setBackground(PANEL);
        showPassword.setFocusPainted(false);

        showPassword.setCursor(
                new Cursor(Cursor.HAND_CURSOR));

        showPassword.addActionListener(e -> {

            if (showPassword.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('\u2022');
            }
        });

        showPasswordPanel.add(showPassword);

        loginCard.add(showPasswordPanel);
        loginCard.add(Box.createVerticalStrut(25));

        // SIGN IN BUTTON
        JButton loginButton = new JButton("SIGN IN");

        styleMainButton(loginButton);

        loginButton.addActionListener(e -> login());

        loginCard.add(loginButton);
        loginCard.add(Box.createVerticalStrut(22));

        // CREATE ACCOUNT
        JLabel createLabel = new JLabel(
                "Don't have an account?");

        createLabel.setFont(
                new Font("SansSerif", Font.PLAIN, 12));

        createLabel.setForeground(MUTED);

        createLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        loginCard.add(createLabel);
        loginCard.add(Box.createVerticalStrut(7));

        JButton createAccountButton = new JButton(
                "CREATE ACCOUNT");

        styleTextButton(createAccountButton);

        createAccountButton.setFont(
                new Font("SansSerif", Font.BOLD, 13));

        createAccountButton.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        createAccountButton.addActionListener(
                e -> showRegistrationForm());

        loginCard.add(createAccountButton);
        loginCard.add(Box.createVerticalStrut(20));

        // AUTHORIZED USERS
        JLabel bottomText = new JLabel(
                "AUTHORIZED USERS ONLY");

        bottomText.setFont(
                new Font("SansSerif", Font.BOLD, 10));

        bottomText.setForeground(MUTED);

        bottomText.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        loginCard.add(bottomText);

        // CENTER LOGIN CARD
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        rightPanel.add(loginCard, gbc);

        // ADD PANELS
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

        setContentPane(mainPanel);
    }

    // CREATE LABEL
    private JLabel createLabel(String text) {

        JLabel label = new JLabel(text);

        label.setFont(
                new Font("SansSerif", Font.BOLD, 12));

        label.setForeground(GOLD);
        label.setHorizontalAlignment(SwingConstants.LEFT);

        label.setPreferredSize(new Dimension(350, 18));
        label.setMinimumSize(new Dimension(350, 18));
        label.setMaximumSize(new Dimension(350, 18));

        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        return label;
    }

    // STYLE TEXT FIELD
    private void styleField(JTextField field) {

        field.setFont(
                new Font("SansSerif", Font.PLAIN, 14));

        field.setBackground(FIELD);
        field.setForeground(WHITE);
        field.setCaretColor(GOLD);

        field.setBorder(new CompoundBorder(
                new LineBorder(new Color(75, 75, 80), 1),
                new EmptyBorder(10, 12, 10, 12)
        ));

        field.setPreferredSize(new Dimension(350, 43));
        field.setMinimumSize(new Dimension(350, 43));
        field.setMaximumSize(new Dimension(350, 43));

        field.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    // SMALL ACCOUNT TYPE DROPDOWN
    private void styleSmallComboBox(JComboBox<String> combo) {

        combo.setFont(
                new Font("SansSerif", Font.PLAIN, 12));

        combo.setBackground(FIELD);
        combo.setForeground(WHITE);
        combo.setFocusable(false);

        combo.setPreferredSize(new Dimension(125, 29));
        combo.setMinimumSize(new Dimension(125, 29));
        combo.setMaximumSize(new Dimension(125, 29));

        combo.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    // STYLE MAIN BUTTON
    private void styleMainButton(JButton button) {

        button.setFont(
                new Font("SansSerif", Font.BOLD, 14));

        button.setForeground(BACKGROUND);
        button.setBackground(GOLD);

        button.setBorder(
                new EmptyBorder(12, 15, 12, 15));

        button.setFocusPainted(false);

        button.setCursor(
                new Cursor(Cursor.HAND_CURSOR));

        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        button.setPreferredSize(new Dimension(350, 46));
        button.setMinimumSize(new Dimension(350, 46));
        button.setMaximumSize(new Dimension(350, 46));

        button.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(
                        new Color(220, 187, 125));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(GOLD);
            }
        });
    }

    // STYLE TEXT BUTTON
    private void styleTextButton(JButton button) {

        button.setFont(
                new Font("SansSerif", Font.PLAIN, 12));

        button.setForeground(GOLD);
        button.setBackground(PANEL);

        button.setBorder(null);
        button.setFocusPainted(false);

        button.setCursor(
                new Cursor(Cursor.HAND_CURSOR));
    }

    // REGISTRATION FORM
    private void showRegistrationForm() {

        JDialog registerDialog = new JDialog(
                this, "Create Account", true);

        registerDialog.setSize(460, 600);
        registerDialog.setResizable(false);
        registerDialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel();

        formPanel.setLayout(
                new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        formPanel.setBackground(PANEL);

        formPanel.setBorder(
                new EmptyBorder(25, 35, 25, 35));

        // TITLE
        JLabel title = new JLabel("Create Your Account");

        title.setFont(
                new Font("Serif", Font.BOLD, 27));

        title.setForeground(WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        formPanel.add(title);
        formPanel.add(Box.createVerticalStrut(7));

        // SUBTITLE
        JLabel subtitle = new JLabel(
                "Register to access Estate Manager.");

        subtitle.setFont(
                new Font("SansSerif", Font.PLAIN, 12));

        subtitle.setForeground(MUTED);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        formPanel.add(subtitle);
        formPanel.add(Box.createVerticalStrut(22));

        // FULL NAME
        formPanel.add(createLabel("FULL NAME"));
        formPanel.add(Box.createVerticalStrut(6));

        JTextField nameField = new JTextField();
        styleField(nameField);

        formPanel.add(nameField);
        formPanel.add(Box.createVerticalStrut(13));

        // USERNAME
        formPanel.add(createLabel("USERNAME"));
        formPanel.add(Box.createVerticalStrut(6));

        JTextField newUsername = new JTextField();
        styleField(newUsername);

        formPanel.add(newUsername);
        formPanel.add(Box.createVerticalStrut(13));

        // PASSWORD
        formPanel.add(createLabel("PASSWORD"));
        formPanel.add(Box.createVerticalStrut(6));

        JPasswordField newPassword = new JPasswordField();
        styleField(newPassword);

        formPanel.add(newPassword);
        formPanel.add(Box.createVerticalStrut(13));

        // CONFIRM PASSWORD
        formPanel.add(createLabel("CONFIRM PASSWORD"));
        formPanel.add(Box.createVerticalStrut(6));

        JPasswordField confirmPassword = new JPasswordField();
        styleField(confirmPassword);

        formPanel.add(confirmPassword);
        formPanel.add(Box.createVerticalStrut(13));

        // ACCOUNT TYPE
        formPanel.add(createLabel("ACCOUNT TYPE"));
        formPanel.add(Box.createVerticalStrut(6));

        JComboBox<String> newRole = new JComboBox<>(
                new String[]{"Agent", "Admin"});

        styleSmallComboBox(newRole);

        formPanel.add(newRole);
        formPanel.add(Box.createVerticalStrut(22));

        // REGISTER BUTTON
        JButton registerButton = new JButton("CREATE ACCOUNT");

        styleMainButton(registerButton);

        registerButton.addActionListener(e -> {

            String fullName = nameField.getText().trim();
            String username = newUsername.getText().trim();

            String password =
                    new String(newPassword.getPassword());

            String confirm =
                    new String(confirmPassword.getPassword());

            String role =
                    (String) newRole.getSelectedItem();

            // BACKEND: UserService checks the fields and saves the account in MySQL
            UserService userService = AppContext.userService();

            if (!userService.register(fullName, username, password, confirm, role)) {

                JOptionPane.showMessageDialog(
                        registerDialog,
                        userService.getMessage(),
                        "Cannot Create Account",
                        JOptionPane.WARNING_MESSAGE);

                return;
            }

            JOptionPane.showMessageDialog(
                    registerDialog,
                    userService.getMessage(),
                    "Registration Successful",
                    JOptionPane.INFORMATION_MESSAGE);

            usernameField.setText(username);
            passwordField.setText("");
            roleCombo.setSelectedItem(role);

            registerDialog.dispose();
        });

        formPanel.add(registerButton);

        registerDialog.setContentPane(formPanel);
        registerDialog.setVisible(true);
    }

    // LOGIN FUNCTION
    private void login() {

        String username = usernameField.getText().trim();

        String password =
                new String(passwordField.getPassword());

        String selectedRole =
                (String) roleCombo.getSelectedItem();

        // BACKEND: UserService looks up the user in the MySQL "users" table
        UserService userService = AppContext.userService();
        User user = userService.login(username, password);

        if (user == null) {

            JOptionPane.showMessageDialog(
                    this,
                    userService.getMessage(),
                    "Login Failed",
                    JOptionPane.WARNING_MESSAGE);

            return;
        }

        // getRole() gives "ADMIN" or "AGENT"; the dropdown says "Admin" or "Agent"
        if (!user.getRole().equalsIgnoreCase(selectedRole)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Incorrect account type.\n"
                    + "Please select your registered account type.",
                    "Account Type Mismatch",
                    JOptionPane.WARNING_MESSAGE);

            return;
        }

        // OPEN THE DASHBOARD FOR THE ROLE
        if (user.getRole().equals("AGENT")) {

            AgentDashboard dashboard = new AgentDashboard(user);
            dashboard.setVisible(true);

        } else {

            AdminDashboard dashboard = new AdminDashboard(user);
            dashboard.setVisible(true);
        }

        dispose(); // close the login window
    }

    // PROPERTY IMAGE PANEL
    private class PhotoPanel extends JPanel {

        private BufferedImage photo;

        public PhotoPanel(String resourcePath) {

            try {

                java.net.URL imageURL =
                        LogIn.class.getResource(resourcePath);

                if (imageURL != null) {
                    photo = ImageIO.read(imageURL);
                }

            } catch (Exception ex) {

                System.out.println(
                        "Property image not found: " + resourcePath);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            if (photo != null) {

                int panelW = getWidth();
                int panelH = getHeight();

                double scale = Math.max(
                        (double) panelW / photo.getWidth(),
                        (double) panelH / photo.getHeight());

                int imageW =
                        (int) (photo.getWidth() * scale);

                int imageH =
                        (int) (photo.getHeight() * scale);

                int x = (panelW - imageW) / 2;
                int y = (panelH - imageH) / 2;

                g2.drawImage(
                        photo, x, y, imageW, imageH, this);

                g2.setColor(
                        new Color(10, 12, 17, 125));

                g2.fillRect(
                        0, 0, panelW, panelH);

            } else {

                g2.setColor(BACKGROUND);

                g2.fillRect(
                        0, 0, getWidth(), getHeight());

                g2.setColor(GOLD);

                g2.setFont(
                        new Font("SansSerif", Font.BOLD, 16));

                g2.drawString(
                        "Add your property photo here",
                        35,
                        getHeight() / 2);
            }

            g2.dispose();
        }
    }

    // MAIN METHOD
    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {

            LogIn window = new LogIn();

            window.setVisible(true);
        });
    }
}
