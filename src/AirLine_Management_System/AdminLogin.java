package AirLine_Management_System;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AdminLogin extends JFrame {
    private JPanel westPanel, centerPanel;
    private JButton signupButton;
    private JTextField usernameField;
    private JPasswordField passwordField;
    public AdminLogin() {
        setTitle("Airline Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        createWestPanel();
        createCenterPanel();
        setLocationRelativeTo(null);
        add(westPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);

        setVisible(true);
    }
    private Connection connectToDatabase() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/airline_managment";
        String user = "root";
        String password = "nirmal";
        return DriverManager.getConnection(url, user, password);
    }
    //Check login details
    private boolean validateLogin(String username, String password) {
        String query = "SELECT * FROM admin WHERE username = ? AND password = ?";
        try (Connection connection = connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next(); // If a row exists, credentials are correct
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    private void showAdminMainScreen() {
        JFrame adminMainFrame = new JFrame("Admin Main Screen");
        adminMainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        adminMainFrame.setSize(1300, 800);

        AdminMainScreen adminMainScreen = new AdminMainScreen(adminMainFrame);

        adminMainFrame.setVisible(true);
        adminMainFrame.setLocationRelativeTo(null);
        dispose();
    }
    private void createWestPanel() {
        westPanel = new JPanel();
        westPanel.setBackground(Color.getHSBColor(0.6f, 1.0f, 1.0f));
        westPanel.setPreferredSize(new Dimension(300, 600));
        westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS));

        ImageIcon imageIcon = new ImageIcon("C:\\Users\\ASUS\\Pictures\\fl.png");
        int originalWidth = imageIcon.getIconWidth();
        int originalHeight = imageIcon.getIconHeight();

        int scaledWidth = 150;
        int scaledHeight = 150;

// Scale the image to the desired dimensions
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
    }
    private void createCenterPanel() {
        centerPanel = new JPanel();
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        JLabel loginLabel = new JLabel("Admin Login");
        loginLabel.setFont(new Font("Arial", Font.BOLD, 24));
        loginLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(20);
        usernameField.setMaximumSize(usernameField.getPreferredSize());

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);
        passwordField.setMaximumSize(passwordField.getPreferredSize());

        JButton loginButton = new JButton("Login");
        signupButton = new JButton("Sign Up");

        JButton homeButton = new JButton("Main Login");

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(loginLabel);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(usernameLabel);
        centerPanel.add(usernameField);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(passwordLabel);
        centerPanel.add(passwordField);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(loginButton);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(signupButton);
        centerPanel.add(Box.createVerticalStrut(150));
        centerPanel.add(homeButton);
        centerPanel.add(Box.createVerticalGlue());

        loginLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signupButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        homeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
//Login button action
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (validateLogin(username, password)) {
                    showAdminMainScreen();

                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password. Please try again.", "Login Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
//SignUp Button
            signupButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    openSignUpForm();
                }
            });
//Back to Main Login
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Login loginScreen = new Login();
                loginScreen.setVisible(true);
                dispose();
            }
        });
    }
    private void openSignUpForm() {
            AdminSignUp signUp = new AdminSignUp();
            signUp.setVisible(true);
            dispose();
        }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminLogin::new);
    }
}
