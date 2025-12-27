package AirLine_Management_System;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.List;

import static AirLine_Management_System.DatabaseOperations.updateBooking;

public class Bookdetails extends JPanel {
    private JLabel idLabel, pnameLabel, nameLabel, detailsLabel, codeLabel;
    private JComboBox<String> idField, nameField;
    private JTextField pnameField, detailsField,codeField;;
    private JButton submitButton, updateButton, deleteButton;
    private JTable bookingTable;

    public Bookdetails() {
        setLayout(new BorderLayout());

        JPanel northPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        idLabel = new JLabel("Passenger ID");
        idField = new JComboBox<>();
        pnameLabel = new JLabel("Passenger Name");
        pnameField = new JTextField(15);
        pnameField.setEditable(false); //Can't edit field this.
        nameLabel = new JLabel("Flight Code");
        nameField = new JComboBox<>();
        detailsLabel = new JLabel("Booking Details");
        detailsField = new JTextField(15);
        codeLabel = new JLabel("Flight ID");
        codeField = new JTextField(15);

        addToPanelCustom(northPanel, gbc, idLabel, 0, 0);
        addToPanelCustom(northPanel, gbc, idField, 1, 0);
        addToPanelCustom(northPanel, gbc, pnameLabel, 3, 0);
        addToPanelCustom(northPanel, gbc, pnameField, 4, 0);
        addToPanelCustom(northPanel, gbc, detailsLabel, 5, 0);
        addToPanelCustom(northPanel, gbc, detailsField, 6, 0);
        addToPanelCustom(northPanel, gbc, codeLabel, 3, 1);
        addToPanelCustom(northPanel, gbc, codeField, 4, 1);
        addToPanelCustom(northPanel, gbc, nameLabel, 1, 1);
        addToPanelCustom(northPanel, gbc, nameField, 2, 1);

        submitButton = new JButton("Submit");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");

        addButtonToPanel(northPanel, gbc, submitButton, 1, 2, GridBagConstraints.HORIZONTAL);
        addButtonToPanel(northPanel, gbc, updateButton, 2, 2, GridBagConstraints.HORIZONTAL);
        addButtonToPanel(northPanel, gbc, deleteButton, 3, 2, GridBagConstraints.HORIZONTAL);

        Dimension btnSize = new Dimension(100, 30);
        submitButton.setPreferredSize(btnSize);
        updateButton.setPreferredSize(btnSize);
        deleteButton.setPreferredSize(btnSize);

        add(northPanel, BorderLayout.NORTH);

        bookingTable = new JTable();

        displayBookingDetails();

        JScrollPane scrollPane = new JScrollPane(bookingTable);
        add(scrollPane, BorderLayout.CENTER);

        List<Integer> passengerIDs = DatabaseOperations.populatePassengerIDs();
        for (int id : passengerIDs) {
            idField.addItem(String.valueOf(id));
        }
        // Action Listener for ID Field
        idField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String selectedPassengerID = idField.getSelectedItem().toString();
                String passengerName = DatabaseOperations.fetchPassengerName(Integer.parseInt(selectedPassengerID));
                pnameField.setText(passengerName);
            }
        });
        // Populate flight codes in nameField JComboBox
        List<String> flightCodes = DatabaseOperations.getFlightCodes();
        for (String code : flightCodes) {
            nameField.addItem(code);
        }
        // Action Listener for nameField JComboBox (Flight Code)
        nameField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedFlightCode = nameField.getSelectedItem().toString();
                int flightId = DatabaseOperations.getFlightIdFromCode(selectedFlightCode);
                if (flightId != -1) {
                    codeField.setText(String.valueOf(flightId)); // Set the flight ID in codeField
                } else {
                    JOptionPane.showMessageDialog(null, "Flight ID not found for selected code.");
                }
            }
        });
        // Action  for Submit Button
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String passengerID = idField.getSelectedItem().toString();
                String flightID = codeField.getText().toString();
                String flightName = nameField.getSelectedItem().toString();
                String bookingDetails = detailsField.getText();
                String passengerName = pnameField.getText();

                // Perform insertion into the database
                boolean insertionSuccessful = DatabaseOperations.insertBooking(passengerID, flightID, flightName, bookingDetails, passengerName);

                if (insertionSuccessful) {
                    JOptionPane.showMessageDialog(null, "Booking details added successfully!");
                    detailsField.setText("");
                    displayBookingDetails();
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to add booking details. Please try again.");
                }
            }
        });
//Delete button Action
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = bookingTable.getSelectedRow(); // Get the selected row index

                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Please select a row to delete.");
                    return;
                }
                int bookingId = (int) bookingTable.getValueAt(selectedRow, 0); // Get the Booking ID from the selected row

                boolean deletionSuccessful = DatabaseOperations.deleteBooking(bookingId);

                if (deletionSuccessful) {
                    DefaultTableModel model = (DefaultTableModel) bookingTable.getModel();
                    model.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(null, "Booking details deleted successfully!");
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to delete booking details. Please try again.");
                }
            }
        });
//Update Button Action
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = bookingTable.getSelectedRow();

                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Please select a row to update.");
                    return;
                }
                int bookingId = (int) bookingTable.getValueAt(selectedRow, 0);
                String details = detailsField.getText();
                String flightID = codeField.getText().toString();
                String flightName = nameField.getSelectedItem().toString();
                String passengerID = idField.getSelectedItem().toString();
                String passengerName = pnameField.getText();

                boolean updateSuccessful = updateBooking(bookingId, details, flightID, flightName, passengerID, passengerName);

                if (updateSuccessful) {
                    DefaultTableModel model = (DefaultTableModel) bookingTable.getModel();
                    model.setValueAt(details, selectedRow, 1);
                    model.setValueAt(flightID, selectedRow, 2);
                    model.setValueAt(flightName, selectedRow, 3);
                    model.setValueAt(passengerID, selectedRow, 4);
                    model.setValueAt(passengerName, selectedRow, 5);

                    JOptionPane.showMessageDialog(null, "Booking details updated successfully!");
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to update booking details. Please try again.");
                }
            }
        });
    }
    // Method to display booking details in the JTable
    private void displayBookingDetails() {
        try {
            String url = "jdbc:mysql://localhost:3306/airline_managment";
            String username = "root";
            String password = "nirmal";

            Connection connection = DriverManager.getConnection(url, username, password);

            String query = "SELECT * FROM booking";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            DefaultTableModel model = new DefaultTableModel();

            model.addColumn("Booking ID");
            model.addColumn("Details");
            model.addColumn("Flight ID");
            model.addColumn("Flight Code");
            model.addColumn("Passenger ID");
            model.addColumn("Passenger Name");

            while (resultSet.next()) {
                int bookingId = resultSet.getInt("b_Id");
                String details = resultSet.getString("b_details");
                int flightId = resultSet.getInt("f_Id");
                String flightCode = resultSet.getString("f_code");
                int passengerId = resultSet.getInt("p_Id");
                String passengerName = resultSet.getString("p_name");

                model.addRow(new Object[]{bookingId, details, flightId, flightCode, passengerId, passengerName});
            }
            bookingTable.setModel(model);

            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void addToPanelCustom(JPanel panel, GridBagConstraints gbc, JComponent component, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(component, gbc);
    }
    private void addButtonToPanel(JPanel panel, GridBagConstraints gbc, JComponent component, int x, int y, int fill) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        gbc.fill = fill;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(component, gbc);
    }
    public JComboBox<String> getIdField() {
        return idField;
    }
    public JTextField getPnameField() {
        return pnameField;
    }
    public JComboBox<String> getNameField() {
        return nameField;
    }
    public JTextField getDetailsField() {
        return detailsField;
    }
    public JTextField getCodeField() {
        return codeField;
    }
}

