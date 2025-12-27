package AirLine_Management_System;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Profile extends JPanel {
    private JLabel nameLabel, passportLabel, nationalityLabel, ageLabel, usernameLabel, passwordLabel;
    private JTextField nameField, passportField, nationalityField, ageField, usernameField;
    private JPasswordField passwordField;
    private JButton submitButton;
    private JPanel centerPanel;

    public Profile() {
        setLayout(new BorderLayout());

        JPanel northPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        nameLabel = new JLabel("Passenger Name");
        nameField = new JTextField(15);
        passportLabel = new JLabel("Passport Number");
        passportField = new JTextField(15);
        nationalityLabel = new JLabel("Nationality");
        nationalityField = new JTextField(15);
        ageLabel = new JLabel("Age");
        ageField = new JTextField(15);
        usernameLabel = new JLabel("Username");
        usernameField = new JTextField(15);
        passwordLabel = new JLabel("Password");
        passwordField = new JPasswordField(15);

        addToPanelCustom(northPanel, gbc, nameLabel, 0, 0);
        addToPanelCustom(northPanel, gbc, nameField, 1, 0);
        addToPanelCustom(northPanel, gbc, passportLabel, 2, 0);
        addToPanelCustom(northPanel, gbc, passportField, 3, 0);
        addToPanelCustom(northPanel, gbc, nationalityLabel, 4, 0);
        addToPanelCustom(northPanel, gbc, nationalityField, 5, 0);
        addToPanelCustom(northPanel, gbc, ageLabel, 0, 1);
        addToPanelCustom(northPanel, gbc, ageField, 1, 1);
        addToPanelCustom(northPanel, gbc, usernameLabel, 2, 1);
        addToPanelCustom(northPanel, gbc, usernameField, 3, 1);
        addToPanelCustom(northPanel, gbc, passwordLabel, 4, 1);
        addToPanelCustom(northPanel, gbc, passwordField, 5, 1);

        submitButton = new JButton("Update Your Details");
        Color lightBlue = new Color(173, 216, 230);
        submitButton.setBackground(lightBlue);
        submitButton.addActionListener(e -> {
            String name = nameField.getText();
            String passport = passportField.getText();
            String nationality = nationalityField.getText();
            String age = ageField.getText();
            String username = usernameField.getText();
            String password = String.valueOf(passwordField.getPassword());

            try (Connection connection = connectToDatabase()) {
                if (connection != null) {
                    String updateQuery = "UPDATE passenger SET p_username=?, p_password=?, p_name=?, nationality=?,  age=? WHERE passportNum=?";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                        preparedStatement.setString(1, username);
                        preparedStatement.setString(2, password);
                        preparedStatement.setString(3, name);
                        preparedStatement.setString(4, nationality);
                        preparedStatement.setString(5, age);
                        preparedStatement.setString(6, passport);

                        int rowsUpdated = preparedStatement.executeUpdate();
                        if (rowsUpdated > 0) {
                            JOptionPane.showMessageDialog(null, "Your Details are Updated!");
                            centerPanel.removeAll();
                            centerPanel.revalidate();
                            centerPanel.repaint();
                            displayUserData(username);

                            nameField.setText("");
                            passportField.setText("");
                            nationalityField.setText("");
                            ageField.setText("");
                            usernameField.setText("");
                            passwordField.setText("");

                        } else {
                            JOptionPane.showMessageDialog(null, "Passport number not found for updating details!");
                        }
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error updating details in the database!");
            }
        });

        addToPanelCustom(northPanel, gbc, submitButton, 1, 3);

        add(northPanel, BorderLayout.NORTH);

        centerPanel = new JPanel(new BorderLayout());
        add(centerPanel, BorderLayout.CENTER);

        String loggedInUsername = performLogin();

        if (loggedInUsername != null) {
            displayUserData(loggedInUsername);
        } else {
            JOptionPane.showMessageDialog(null, "Please check UserName", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private String performLogin() {
        return JOptionPane.showInputDialog("Enter Your Username");
    }
    private void displayUserData(String username) {
        String query = "SELECT * FROM passenger WHERE p_username = ?";
        try (Connection connection = connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String passengerName = "Passenger Name:" + resultSet.getString("p_name");
                String passportNumber = "Passport Number:" + resultSet.getString("passportNum");
                String nationality = "Nationality:" + resultSet.getString("nationality");
                String age = "Age: " + resultSet.getString("age");


                Font titleFont = new Font("Arial", Font.BOLD, 16);
                Font detailsFont = new Font("Arial", Font.PLAIN, 14);

                JPanel innerPanel = new JPanel(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.anchor = GridBagConstraints.WEST;

                JLabel titleLabel = new JLabel("Your Details");
                titleLabel.setFont(titleFont);
                titleLabel.setForeground(Color.BLUE);
                innerPanel.add(titleLabel, gbc);

                gbc.gridy++;
                JLabel nameLabel = new JLabel(passengerName);
                nameLabel.setFont(detailsFont);
                innerPanel.add(nameLabel, gbc);

                gbc.gridy++;
                JLabel passportLabel = new JLabel(passportNumber);
                passportLabel.setFont(detailsFont);
                innerPanel.add(passportLabel, gbc);

                gbc.gridy++;
                JLabel nationalityLabel = new JLabel(nationality);
                nationalityLabel.setFont(detailsFont);
                innerPanel.add(nationalityLabel, gbc);

                gbc.gridy++;
                JLabel ageLabel = new JLabel(age);
                ageLabel.setFont(detailsFont);
                innerPanel.add(ageLabel, gbc);

                centerPanel.removeAll();
                centerPanel.setLayout(new BorderLayout());
                centerPanel.add(innerPanel, BorderLayout.NORTH);
            } else {
                JOptionPane.showMessageDialog(null, "User data not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading user data!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private Connection connectToDatabase() {
        Connection connection = null;
        try {
            String url = "jdbc:mysql://localhost:3306/airline_managment";
            String user = "root";
            String password = "nirmal";

            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return connection;
    }
    private void addToPanelCustom(JPanel panel, GridBagConstraints gbc, Component component, int gridx, int gridy) {
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        panel.add(component, gbc);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Profile profile = new Profile();
            JFrame frame = new JFrame("Profile");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(profile);
            int width = 1000;
            int height = 600;
            frame.setSize(width, height);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
