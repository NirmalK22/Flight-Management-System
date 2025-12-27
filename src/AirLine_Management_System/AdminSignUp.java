package AirLine_Management_System;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AdminSignUp extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public AdminSignUp() {
        super("Admin Sign Up");

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 10, 5, 10);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;

        JLabel usernameLabel = new JLabel("Username:");
        panel.add(usernameLabel, constraints);

        usernameField = new JTextField(20);
        constraints.gridx = 1;
        panel.add(usernameField, constraints);

        JLabel passwordLabel = new JLabel("Password:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(passwordLabel, constraints);

        passwordField = new JPasswordField(20);
        constraints.gridx = 1;
        panel.add(passwordField, constraints);

        JButton signUpButton = new JButton("Sign Up");
        constraints.gridx = 0;
        constraints.gridy = 10;
        constraints.gridwidth = 2;
        panel.add(signUpButton, constraints);

        JButton backButton = new JButton("Back to Login");
        constraints.gridy = 11;
        panel.add(backButton, constraints);

        backButton.addActionListener(e -> openLoginForm());

        JPanel westPanel = createWestPanel();
        add(westPanel, BorderLayout.WEST);

        add(panel);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        signUpButton.addActionListener(e -> signUp());
    }
    private JPanel createWestPanel() {
        JPanel westPanel = new JPanel();
        westPanel.setBackground(Color.getHSBColor(0.6f, 1.0f, 1.0f));
        westPanel.setPreferredSize(new Dimension(300, 600));
        westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS));

        ImageIcon imageIcon = new ImageIcon("C:\\Users\\ASUS\\Pictures\\68.jpg");
        int originalWidth = imageIcon.getIconWidth();
        int originalHeight = imageIcon.getIconHeight();

        int scaledWidth = 200;
        int scaledHeight = 150;

        Image scaledImage = imageIcon.getImage().getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledImageIcon = new ImageIcon(scaledImage);

        JLabel imageLabel = new JLabel(scaledImageIcon);
        imageLabel.setPreferredSize(new Dimension(scaledWidth, scaledHeight));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel welcomeLabel = new JLabel("WingSyc");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 30));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel futureLabel = new JLabel("Future of AirLine");
        futureLabel.setFont(new Font("Arial", Font.ITALIC, 20));
        futureLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel airlineLabel = new JLabel("Booking Management System");
        airlineLabel.setFont(new Font("Roboto", Font.ITALIC, 20));
        airlineLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        westPanel.add(Box.createVerticalGlue());
        westPanel.add(imageLabel);
        westPanel.add(Box.createVerticalStrut(10));
        westPanel.add(welcomeLabel);
        westPanel.add(Box.createVerticalStrut(10));
        westPanel.add(futureLabel);
        westPanel.add(Box.createVerticalStrut(10));
        westPanel.add(airlineLabel);
        westPanel.add(Box.createVerticalGlue());

        return westPanel;
    }
    private void openLoginForm() {
        AdminLogin login = new AdminLogin();
        login.setVisible(true);
        this.dispose();
    }
    private Connection connectToDatabase() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/airline_managment";
        String user = "root";
        String password = "nirmal";
        return DriverManager.getConnection(url, user, password);
    }
    //Passenger SignUP
    private void signUp() {
        try (Connection conn = connectToDatabase()) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            String sql = "INSERT INTO admin (username, password) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Signed Up successfully!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: Couldn't sign up. Please try again.");
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminSignUp::new);
    }
}
