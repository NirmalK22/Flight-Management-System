package AirLine_Management_System;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;

public class Passenger extends JPanel {
    private JLabel nameLabel, passportLabel, nationalityLabel, ageLabel, usernameLabel, passwordLabel;
    private JTextField nameField, passportField, nationalityField, ageField, usernameField;
    private JPasswordField passwordField;
    private JButton submitButton, upadateButton, deleteButton;
    private JTable table;
    private DefaultTableModel tableModel;

    public Passenger(int retrievedId, String retrievedName) {
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

        upadateButton = new JButton("Update Passenger Details");
        upadateButton.addActionListener(e -> {
            String name = nameField.getText();
            String passport = passportField.getText();
            String nationality = nationalityField.getText();
            String age = ageField.getText();
            String username = usernameField.getText();
            String password = String.valueOf(passwordField.getPassword());

            // Validation the form
            if (!age.matches("\\d+")) {
                JOptionPane.showMessageDialog(null, "Please enter a valid age (numeric value only)!","Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (name.isEmpty() || passport.isEmpty() || nationality.isEmpty() || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill in all fields!","Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
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
                            JOptionPane.showMessageDialog(null, "Details updated in the database!","Success", JOptionPane.INFORMATION_MESSAGE);

                            displayPassengerDetails();

                            nameField.setText("");
                            passportField.setText("");
                            nationalityField.setText("");
                            ageField.setText("");
                            usernameField.setText("");
                            passwordField.setText("");
                        } else {
                            JOptionPane.showMessageDialog(null, "Passport number not found for updating details!","Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error updating details in the database!","Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        //Delete the Passenger Details
        deleteButton = new JButton("Delete Passenger Details");
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Please select a row to delete.","Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this record?", "Warning", JOptionPane.YES_NO_OPTION);

                if (dialogResult == JOptionPane.YES_OPTION) {
                    try (Connection connection = connectToDatabase()) {
                        if (connection != null) {
                            int idColumnIndex = 0;
                            int idToDelete = (int) table.getValueAt(selectedRow, idColumnIndex);

                            String deleteQuery = "DELETE FROM passenger WHERE p_id = ?";
                            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                                preparedStatement.setInt(1, idToDelete);
                                int affectedRows = preparedStatement.executeUpdate();

                                if (affectedRows > 0) {
                                    JOptionPane.showMessageDialog(null, "Record deleted successfully!","Success", JOptionPane.INFORMATION_MESSAGE);
                                    displayPassengerDetails();
                                } else {
                                    JOptionPane.showMessageDialog(null, "Failed to delete the record.","Error", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error deleting passenger details: " + ex.getMessage());
                    }
                }
            }
        });
        //Submit the Passenger Details
        submitButton = new JButton("Add Passenger Details");
        submitButton.addActionListener(e -> {
            String name = nameField.getText();
            String passport = passportField.getText();
            String nationality = nationalityField.getText();
            String age = ageField.getText();
            String username = usernameField.getText();
            String password = String.valueOf(passwordField.getPassword());

            // Validate input fields
            if (name.isEmpty() || passport.isEmpty() || nationality.isEmpty() || age.isEmpty() || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill in all fields.","Error", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    int ageValue = Integer.parseInt(age);

                    try (Connection connection = connectToDatabase()) {
                        if (connection != null) {
                            String insertQuery = "INSERT INTO passenger (p_username, p_password, p_name, passportNum, nationality,  age) VALUES (?, ?, ?, ?, ?, ?)";
                            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                                preparedStatement.setString(1, username);
                                preparedStatement.setString(2, password);
                                preparedStatement.setString(3, name);
                                preparedStatement.setString(4, passport);
                                preparedStatement.setString(5, nationality);
                                preparedStatement.setInt(6, ageValue);
                                preparedStatement.executeUpdate();
                                JOptionPane.showMessageDialog(null, "Details added to the database!","Success", JOptionPane.INFORMATION_MESSAGE);

                                displayPassengerDetails();

                                nameField.setText("");
                                passportField.setText("");
                                nationalityField.setText("");
                                ageField.setText("");
                                usernameField.setText("");
                                passwordField.setText("");
                            }
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error adding details to the database!","Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid age.","Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        addToPanelCustom(northPanel, gbc, submitButton, 1, 3);
        addToPanelCustom(northPanel, gbc, upadateButton, 2, 3);
        addToPanelCustom(northPanel, gbc, deleteButton, 3, 3);

        add(northPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);

        displayPassengerDetails();
    }
    private void displayPassengerDetails() {
        try (Connection connection = connectToDatabase()) {
            if (connection != null) {
                String query ="SELECT p_Id, p_username, p_password, p_name, passportNum, nationality,  age FROM passenger";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                     ResultSet resultSet = preparedStatement.executeQuery()) {

                    ResultSetMetaData metaData = resultSet.getMetaData();
                    int columnCount = metaData.getColumnCount();

                    HashMap<String, String> columnNamesMap = new HashMap<>();
                    columnNamesMap.put("p_id", "Passenger ID");
                    columnNamesMap.put("p_username", "Username");
                    columnNamesMap.put("p_password", "Password");
                    columnNamesMap.put("p_name", "Full Name");
                    columnNamesMap.put("passportnum", "Passport Number");
                    columnNamesMap.put("nationality", "Nationality");
                    columnNamesMap.put("age", "Age");

                    tableModel.setColumnCount(0);
                    tableModel.setRowCount(0);

                    for (int i = 1; i <= columnCount; i++) {
                        String originalColumnName = metaData.getColumnName(i).toLowerCase();
                        String mappedColumnName = columnNamesMap.getOrDefault(originalColumnName, originalColumnName);
                        tableModel.addColumn(mappedColumnName);
                    }

                    while (resultSet.next()) {
                        Object[] rowData = new Object[columnCount];
                        for (int i = 0; i < columnCount; i++) {
                            rowData[i] = resultSet.getObject(i + 1);
                        }
                        tableModel.addRow(rowData);
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error displaying passenger details!");
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
    private void addToPanelCustom(JPanel panel, GridBagConstraints gbc, JComponent component, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(component, gbc);
    }
}
