package AirLine_Management_System;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Login extends JFrame {
    private JFrame frame;
    private JPanel westPanel,centerPanel;
    private JButton signupButton;

    public Login() {
        frame = new JFrame("Airline Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        createWestPanel();
        createCenterPanel();

        frame.add(westPanel, BorderLayout.WEST);
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
    private Connection connectToDatabase() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/airline_managment";
        String user = "root";
        String password = "nirmal";
        return DriverManager.getConnection(url, user, password);
    }
    private boolean validateLogin(String username, String password) {
        String query = "SELECT * FROM passenger WHERE p_username = ? AND p_password = ?";
        try (Connection connection = connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    private void createWestPanel() {
        westPanel = new JPanel();
        westPanel.setBackground(Color.getHSBColor(0.6f, 1.0f, 1.0f));
        westPanel.setPreferredSize(new Dimension(300, 600));
        westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS));

        ImageIcon imageIcon = new ImageIcon("C:\\Users\\ASUS\\Pictures\\fl.png");
        int originalWidth = imageIcon.getIconWidth();
        int originalHeight = imageIcon.getIconHeight();

        int scaledWidth = 200;
        int scaledHeight = 200;

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

        JLabel loginLabel = new JLabel("Login");
        loginLabel.setFont(new Font("Arial", Font.BOLD, 24));
        loginLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(20);
        usernameField.setMaximumSize(usernameField.getPreferredSize());

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setMaximumSize(passwordField.getPreferredSize());

        JButton loginButton = new JButton("Login");
        signupButton = new JButton("Sign Up");
        JButton adminLoginButton = new JButton("Admin Login");

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
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(adminLoginButton);
        centerPanel.add(Box.createVerticalGlue());

        loginLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signupButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        adminLoginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (validateLogin(username, password)) {
                JFrame passengerMainScreenFrame = new JFrame("Passenger Main Screen");
                PassengerMainScreen passengerMainScreen = new PassengerMainScreen(passengerMainScreenFrame, username);

                passengerMainScreenFrame.setSize(1200, 800);
                passengerMainScreenFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                passengerMainScreenFrame.setVisible(true);
                passengerMainScreenFrame.setLocationRelativeTo(null);
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid username or password. Please try again.");
            }
        });
        // Open AdminLogin screen
        adminLoginButton.addActionListener(e -> {
            AdminLogin adminLogin = new AdminLogin();
            adminLogin.setVisible(true);              
            frame.dispose();
        });
        signupButton.addActionListener(e -> openSignUpForm());
    }
    private void openSignUpForm() {
        SignUp signUp = new SignUp();
        signUp.setVisible(true);
        frame.setVisible(false);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Login::new);
    }
}
