package com.hanapbahay.ui;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;

import com.hanapbahay.AppContext;
import com.hanapbahay.model.User;

public class LoginFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox rememberMe;
    private boolean passwordVisible = false;

   
    private final Color BACKGROUND = new Color(23, 26, 33);
    private final Color PANEL = new Color(32, 35, 43);
    private final Color GOLD = new Color(198, 161, 91);
    private final Color WHITE = new Color(245, 245, 245);
    private final Color MUTED = new Color(160, 164, 174);
    private final Color FIELD = new Color(43, 46, 55);

    public LoginFrame() {
        setTitle("Estate Manager - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 620));
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 0, 0));
        mainPanel.setBackground(BACKGROUND);

        // LEFT SIDE: property photo with text overlay
        PhotoPanel leftPanel = new PhotoPanel("/images/estate-house.jpg");
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setBorder(new EmptyBorder(35, 42, 35, 35));

        JPanel brandPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        brandPanel.setOpaque(false);

        JLabel logo = new JLabel("⌂");
        logo.setFont(new Font("Serif", Font.BOLD, 42));
        logo.setForeground(GOLD);

        JLabel brandName = new JLabel("ESTATE MANAGER");
        brandName.setFont(new Font("SansSerif", Font.BOLD, 19));
        brandName.setForeground(WHITE);

        brandPanel.add(logo);
        brandPanel.add(brandName);
        leftPanel.add(brandPanel, BorderLayout.NORTH);

        JPanel leftCaption = new JPanel();
        leftCaption.setOpaque(false);
        leftCaption.setLayout(new BoxLayout(leftCaption, BoxLayout.Y_AXIS));

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
            + "in one convenient platform.</html>"
        );
        description.setFont(new Font("SansSerif", Font.PLAIN, 14));
        description.setForeground(WHITE);
        description.setAlignmentX(Component.LEFT_ALIGNMENT);

        leftCaption.add(smallHeading);
        leftCaption.add(Box.createVerticalStrut(10));
        leftCaption.add(mainHeading);
        leftCaption.add(Box.createVerticalStrut(14));
        leftCaption.add(description);

        leftPanel.add(leftCaption, BorderLayout.SOUTH);

        // RIGHT SIDE: neatly aligned login form
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(BACKGROUND);
        rightPanel.setBorder(new EmptyBorder(35, 45, 35, 45));

        JPanel loginCard = new JPanel(new GridBagLayout());
        loginCard.setBackground(PANEL);
        loginCard.setBorder(new CompoundBorder(
            new LineBorder(new Color(65, 62, 54), 1),
            new EmptyBorder(38, 42, 35, 42)
        ));
        loginCard.setPreferredSize(new Dimension(480, 535));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 0, 0);

        JLabel loginTitle = new JLabel("Welcome Back!");
        loginTitle.setFont(new Font("Serif", Font.BOLD, 31));
        loginTitle.setForeground(WHITE);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 8, 0);
        loginCard.add(loginTitle, gbc);

        JLabel loginSubtitle = new JLabel("Log in to access your account.");
        loginSubtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        loginSubtitle.setForeground(MUTED);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 32, 0);
        loginCard.add(loginSubtitle, gbc);

        JLabel loginLabel = new JLabel("LOG IN");
        loginLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        loginLabel.setForeground(WHITE);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 22, 0);
        loginCard.add(loginLabel, gbc);

        JLabel usernameLabel = createLabel("USERNAME");
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 8, 0);
        loginCard.add(usernameLabel, gbc);

        usernameField = new JTextField();
        styleField(usernameField);
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 22, 0);
        loginCard.add(usernameField, gbc);

        JLabel passwordLabel = createLabel("PASSWORD");
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 8, 0);
        loginCard.add(passwordLabel, gbc);

        passwordField = new JPasswordField();
        styleField(passwordField);
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 10, 0);
        loginCard.add(passwordField, gbc);

        JCheckBox showPassword = new JCheckBox("Show password");
        showPassword.setFont(new Font("SansSerif", Font.PLAIN, 12));
        showPassword.setForeground(MUTED);
        showPassword.setBackground(PANEL);
        showPassword.setFocusPainted(false);
        showPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        showPassword.addActionListener(e -> togglePassword());
        gbc.gridy = 7;
        gbc.insets = new Insets(0, 0, 17, 0);
        loginCard.add(showPassword, gbc);

        rememberMe = new JCheckBox("Remember me");
        rememberMe.setFont(new Font("SansSerif", Font.PLAIN, 12));
        rememberMe.setForeground(MUTED);
        rememberMe.setBackground(PANEL);
        rememberMe.setFocusPainted(false);

        JButton forgotPassword = new JButton("Forgot Password?");
        styleTextButton(forgotPassword);
        forgotPassword.addActionListener(e -> JOptionPane.showMessageDialog(
            this,
            "Please contact your system administrator for password assistance.",
            "Forgot Password",
            JOptionPane.INFORMATION_MESSAGE
        ));

        JPanel optionsPanel = new JPanel(new BorderLayout());
        optionsPanel.setOpaque(false);
        optionsPanel.add(rememberMe, BorderLayout.WEST);
        optionsPanel.add(forgotPassword, BorderLayout.EAST);

        gbc.gridy = 8;
        gbc.insets = new Insets(0, 0, 24, 0);
        loginCard.add(optionsPanel, gbc);

        JButton loginButton = new JButton("SIGN IN");
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        loginButton.setForeground(BACKGROUND);
        loginButton.setBackground(GOLD);
        loginButton.setBorder(new EmptyBorder(13, 15, 13, 15));
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setPreferredSize(new Dimension(300, 48));
        loginButton.addActionListener(e -> login());
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loginButton.setBackground(new Color(220, 187, 125));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(GOLD);
            }
        });

        gbc.gridy = 9;
        gbc.insets = new Insets(0, 0, 25, 0);
        loginCard.add(loginButton, gbc);
        getRootPane().setDefaultButton(loginButton); // pressing Enter clicks SIGN IN

        JLabel bottomText = new JLabel("AUTHORIZED USERS ONLY");
        bottomText.setFont(new Font("SansSerif", Font.BOLD, 10));
        bottomText.setForeground(MUTED);
        gbc.gridy = 10;
        gbc.insets = new Insets(0, 0, 0, 0);
        loginCard.add(bottomText, gbc);

        rightPanel.add(loginCard);

        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);
        setContentPane(mainPanel);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        label.setForeground(GOLD);
        return label;
    }

    private void styleField(JTextField field) {
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBackground(FIELD);
        field.setForeground(WHITE);
        field.setCaretColor(GOLD);
        field.setBorder(new CompoundBorder(
            new LineBorder(new Color(75, 75, 80), 1),
            new EmptyBorder(10, 12, 10, 12)
        ));
        field.setPreferredSize(new Dimension(350, 46));
        field.setMinimumSize(new Dimension(250, 46));
    }

    private void styleTextButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.PLAIN, 12));
        button.setForeground(GOLD);
        button.setBackground(PANEL);
        button.setBorder(null);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void togglePassword() {
        if (passwordVisible) {
            passwordField.setEchoChar('•');
            passwordVisible = false;
        } else {
            passwordField.setEchoChar((char) 0);
            passwordVisible = true;
        }
    }

    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        User user = AppContext.userService().login(username, password); // calls the backend

        if (user != null) {
            MainFrame main = new MainFrame();
            main.showFor(user);
            dispose(); // close the login window
        } else {
            JOptionPane.showMessageDialog(this, AppContext.userService().getMessage(), "Cannot log in",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    // Displays the selected property photo and darkens it slightly for readable text.
    private class PhotoPanel extends JPanel {
        private static final long serialVersionUID = 1L;
        private BufferedImage photo;

        PhotoPanel(String resourcePath) {
            try {
                photo = ImageIO.read(LoginFrame.class.getResource(resourcePath));
            } catch (Exception ex) {
                System.out.println("Property image not found: " + resourcePath);
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
                    (double) panelH / photo.getHeight()
                );
                int imageW = (int) (photo.getWidth() * scale);
                int imageH = (int) (photo.getHeight() * scale);
                int x = (panelW - imageW) / 2;
                int y = (panelH - imageH) / 2;

                g2.drawImage(photo, x, y, imageW, imageH, this);
                g2.setColor(new Color(10, 12, 17, 125));
                g2.fillRect(0, 0, panelW, panelH);
            } else {
                g2.setColor(BACKGROUND);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(GOLD);
                g2.setFont(new Font("SansSerif", Font.BOLD, 16));
                g2.drawString("Add your property photo here", 35, getHeight() / 2);
            }
            g2.dispose();
        }
    }
}
