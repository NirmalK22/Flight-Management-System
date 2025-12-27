package AirLine_Management_System;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class SignUp extends JFrame {
    private JTextField usernameField, nameField, nationalityField, passportNumberField, ageField;
    private JPasswordField passwordField;
    private JLabel nameLabel, nationalityLabel, passportNumberLabel, ageLabel;

    public SignUp() {
        super("Sign Up");

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 10, 5, 10);
        constraints.fill = GridBagConstraints.HORIZONTAL;

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

        nameLabel = new JLabel("Name:");
        constraints.gridx = 0;
        constraints.gridy = 5;
        panel.add(nameLabel, constraints);
        nameField = new JTextField(20);
        constraints.gridx = 1;
        panel.add(nameField, constraints);

        nationalityLabel = new JLabel("Nationality:");
        constraints.gridx = 0;
        constraints.gridy = 7;
        panel.add(nationalityLabel, constraints);
        nationalityField = new JTextField(20);
        constraints.gridx = 1;
        panel.add(nationalityField, constraints);

        passportNumberLabel = new JLabel("Passport Number:");
        constraints.gridx = 0;
        constraints.gridy = 8;
        panel.add(passportNumberLabel, constraints);
        passportNumberField = new JTextField(20);
        constraints.gridx = 1;
        panel.add(passportNumberField, constraints);

        ageLabel = new JLabel("Age:");
        constraints.gridx = 0;
        constraints.gridy = 9;
        panel.add(ageLabel, constraints);
        ageField = new JTextField(20);
        constraints.gridx = 1;
        panel.add(ageField, constraints);

        JButton signUpButton = new JButton("Sign Up");
        constraints.gridx = 0;
        constraints.gridy = 10;
        constraints.gridwidth = 2;
        panel.add(signUpButton, constraints);

        JButton backButton = new JButton("Back to Login");
        constraints.gridy = 11;
        panel.add(backButton, constraints);

        signUpButton.addActionListener(e -> signUp());
        backButton.addActionListener(e -> openLoginForm());

        JPanel westPanel = createWestPanel();
        add(westPanel, BorderLayout.WEST);

        add(panel);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    private JPanel createWestPanel() {
        JPanel westPanel = new JPanel();
        westPanel.setBackground(Color.getHSBColor(0.6f, 1.0f, 1.0f));
        westPanel.setPreferredSize(new Dimension(300, 600));
        westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS));

        ImageIcon imageIcon = new ImageIcon("C:\\Users\\ASUS\\Pictures\\67.jpg");
        int originalWidth = imageIcon.getIconWidth();
        int originalHeight = imageIcon.getIconHeight();

        int scaledWidth = 200;
        int scaledHeight =200;


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
        Login login = new Login();
        login.setVisible(true);
        this.dispose();
    }
    private Connection connectToDatabase() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/airline_managment";
        String user = "root";
        String password = "nirmal";
        return DriverManager.getConnection(url, user, password);
    }
    private void signUp() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        String name = nameField.getText();
        String passportNumber = passportNumberField.getText().trim();
        String nationality = nationalityField.getText().trim();
        String age = ageField.getText().trim();

        if (username.isEmpty() || password.isEmpty() || name.isEmpty() || passportNumber.isEmpty() ||
                nationality.isEmpty() || age.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all the required fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try (Connection connection = connectToDatabase()) {
            String query = "INSERT INTO passenger (p_username, p_password, p_name, passportNum, nationality, age) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, name);
            preparedStatement.setString(4, passportNumber);
            preparedStatement.setString(5, nationality);
            preparedStatement.setString(6, age);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int passengerId = generatedKeys.getInt(1);
                    JOptionPane.showMessageDialog(this, "Your account created successfully!\nKeep in Remember your Password & Username.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    openLoginForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to retrieve Passenger ID.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create passenger account.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to the database or executing SQL query", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(SignUp::new);
    }
}
