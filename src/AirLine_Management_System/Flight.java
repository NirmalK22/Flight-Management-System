package AirLine_Management_System;

import com.github.lgooddatepicker.components.TimePicker;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class Flight extends JPanel {
    private JLabel nameLabel, destinationLabel, seatsLabel, dateLabel, timeLabel;
    private JTextField nameField, destinationField, seatsField;
    private JDatePickerImpl datePicker;
    private TimePicker timePicker;
    private JButton submitButton, updateButton, deleteButton;
    private JTable flightTable;
    private DefaultTableModel tableModel;
    public Flight() {
        setLayout(new BorderLayout());

        JPanel northPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        nameLabel = new JLabel("Flight Code");
        nameField = new JTextField(15);
        destinationLabel = new JLabel("Destination");
        destinationField = new JTextField(15);
        seatsLabel = new JLabel("Number of Seats");
        seatsField = new JTextField(15);
        dateLabel = new JLabel("Date");
        timeLabel = new JLabel("Time");

        UtilDateModel dateModel = new UtilDateModel();
        Properties dateProperties = new Properties();
        JDatePanelImpl datePanel = new JDatePanelImpl(dateModel, dateProperties);
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        timePicker = new TimePicker();

        addToPanelCustom(northPanel, gbc, nameLabel, 0, 0);
        addToPanelCustom(northPanel, gbc, nameField, 1, 0);
        addToPanelCustom(northPanel, gbc, destinationLabel, 2, 0);
        addToPanelCustom(northPanel, gbc, destinationField, 3, 0);
        addToPanelCustom(northPanel, gbc, seatsLabel, 4, 0);
        addToPanelCustom(northPanel, gbc, seatsField, 5, 0);

        addToPanelCustom(northPanel, gbc, dateLabel, 0, 1);
        addToPanelCustom(northPanel, gbc, datePicker, 1, 1);
        addToPanelCustom(northPanel, gbc, timeLabel, 2, 1);
        addToPanelCustom(northPanel, gbc, timePicker, 3, 1);

        submitButton = new JButton("Add Flights");
        updateButton = new JButton("Update Flights");
        deleteButton = new JButton("Remove Flights");

        addButtonToPanel(northPanel, gbc, submitButton, 1, 2, GridBagConstraints.HORIZONTAL);
        addButtonToPanel(northPanel, gbc, updateButton, 2, 2, GridBagConstraints.HORIZONTAL);
        addButtonToPanel(northPanel, gbc, deleteButton, 3, 2, GridBagConstraints.HORIZONTAL);

        Dimension btnSize = new Dimension(200, 30);
        submitButton.setPreferredSize(btnSize);
        updateButton.setPreferredSize(btnSize);
        deleteButton.setPreferredSize(btnSize);
//Delete Flights
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection connection = connectToDatabase();

                    int selectedRow = flightTable.getSelectedRow();

                    if (selectedRow == -1) {
                        JOptionPane.showMessageDialog(null, "Please select a row to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    int recordIdToDelete = (int) flightTable.getValueAt(selectedRow, 0);

                    String deleteQuery = "DELETE FROM flight WHERE f_Id = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
                    preparedStatement.setInt(1, recordIdToDelete);

                    int rowsDeleted = preparedStatement.executeUpdate();
                    if (rowsDeleted > 0) {
                        System.out.println("Flight record deleted successfully!");
                        tableModel.removeRow(selectedRow); // Remove row from table
                        JOptionPane.showMessageDialog(null, "Flight record deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        System.out.println("Failed to delete flight record.");
                        JOptionPane.showMessageDialog(null, "Failed to delete flight record.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    preparedStatement.close();
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        //Submit Flight details
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String flightName = nameField.getText();
                String destination = destinationField.getText();
                String seatNumbers = seatsField.getText();

                if (flightName.isEmpty() || destination.isEmpty() || seatNumbers.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int numberOfSeats;
                try {
                    numberOfSeats = Integer.parseInt(seatNumbers);
                    if (numberOfSeats <= 0) {
                        JOptionPane.showMessageDialog(null, "Please enter a valid positive number for seats.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid number for seats.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                //Date & Time Validate
                Object selectedDate = getDatePickerValue();
                Object selectedTime = getTimePickerValue();

                if (selectedDate == null || selectedTime == null) {
                    JOptionPane.showMessageDialog(null, "Please select date and time.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    Connection connection = connectToDatabase();
                    // Formatting date and time as required
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String formattedDate = dateFormat.format((Date) selectedDate);
                    String formattedTime = selectedTime.toString();

                    String insertQuery = "INSERT INTO flight (f_code, destination, flight_date, flight_time, seats) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                    preparedStatement.setString(1, flightName);
                    preparedStatement.setString(2, destination);
                    preparedStatement.setString(3, formattedDate);
                    preparedStatement.setString(4, formattedTime);
                    preparedStatement.setInt(5, numberOfSeats);

                    int rowsInserted = preparedStatement.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("Data inserted successfully!");
                        displayFlightDetailsInTable();
                        JOptionPane.showMessageDialog(null, "Data inserted successfully!");

                        nameField.setText("");
                        destinationField.setText("");
                        seatsField.setText("");
                        datePicker.getModel().setValue(null);
                        timePicker.setTime(null);
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to insert data!");
                    }
                    preparedStatement.close();
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "An error occurred while inserting data: " + ex.getMessage());
                }
            }
        });
//Update Flight Details
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection connection = connectToDatabase();
                    int selectedRow = flightTable.getSelectedRow();

                    if (selectedRow == -1) {
                        JOptionPane.showMessageDialog(null, "Please select a row to update.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    int recordIdToUpdate = (int) flightTable.getValueAt(selectedRow, 0);

                    String flightName = getNameFieldText();
                    String destination = getDestinationFieldText();
                    String seatNumbers = getSeatsFieldText();

                    if (flightName.isEmpty() || destination.isEmpty() || seatNumbers.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    int numberOfSeats;
                    try {
                        numberOfSeats = Integer.parseInt(seatNumbers);

                        if (numberOfSeats <= 0) {
                            JOptionPane.showMessageDialog(null, "Please enter a valid positive number for seats.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Please enter a valid number for seats.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Date selectedDate = (Date) getDatePickerValue();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String formattedDate = dateFormat.format(selectedDate);

                    String formattedTime = getTimePickerValue().toString();

                    String updateQuery = "UPDATE flight SET f_Code = ?, destination = ?, flight_date = ?, flight_time = ?, Seats = ? WHERE f_Id = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
                    preparedStatement.setString(1, flightName);
                    preparedStatement.setString(2, destination);
                    preparedStatement.setString(3, formattedDate);
                    preparedStatement.setString(4, formattedTime);
                    preparedStatement.setString(5, seatNumbers);
                    preparedStatement.setInt(6, recordIdToUpdate);

                    int rowsUpdated = preparedStatement.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Record updated successfully!");

                        flightTable.getModel().setValueAt(recordIdToUpdate, selectedRow, 0);
                        flightTable.getModel().setValueAt(flightName, selectedRow, 1);
                        flightTable.getModel().setValueAt(destination, selectedRow, 2);
                        flightTable.getModel().setValueAt(formattedDate, selectedRow, 3);
                        flightTable.getModel().setValueAt(formattedTime, selectedRow, 4);
                        flightTable.getModel().setValueAt(seatNumbers, selectedRow, 5);

                        JOptionPane.showMessageDialog(null, "Record updated successfully!");

                        nameField.setText("");
                        destinationField.setText("");
                        seatsField.setText("");
                        datePicker.getModel().setValue(null);
                        timePicker.setTime(null);
                    } else {
                        JOptionPane.showMessageDialog(null, "Update failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    preparedStatement.close();
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "An error occurred while updating the record.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        String[] columns = {"Flight ID","Flight Code", "Destination", "Departure Date", "Departure Time", "Seats"};
        tableModel = new DefaultTableModel(columns, 0);
        flightTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(flightTable);
        add(scrollPane, BorderLayout.CENTER);
        displayFlightDetailsInTable();

        add(northPanel, BorderLayout.NORTH);
    }
    //Dispaly the flght details
    public void displayFlightDetailsInTable() {
        try {
            Connection connection = connectToDatabase();
            String selectQuery = "SELECT f_Id,f_code, destination, flight_date, flight_time, Seats FROM flight";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            ResultSet resultSet = preparedStatement.executeQuery();
            tableModel.setRowCount(0);

            while (resultSet.next()) {
                int flightID = resultSet.getInt("f_Id");
                String flightName = resultSet.getString("f_code");
                String destination = resultSet.getString("destination");
                Date date = resultSet.getDate("flight_date");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = dateFormat.format(date);
                Time time = resultSet.getTime("flight_time");
                String formattedTime = time.toString();
                String seatNumbers = resultSet.getString("Seats");

                tableModel.addRow(new Object[]{flightID,flightName, destination, formattedDate, formattedTime, seatNumbers});
            }
            preparedStatement.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
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
    private void addButtonToPanel(JPanel panel, GridBagConstraints gbc, JComponent component, int x, int y, int fill) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        gbc.fill = fill;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(component, gbc);
    }

    public String getNameFieldText() {
        return nameField.getText();
    }
    public String getDestinationFieldText() {
        return destinationField.getText();
    }
    public String getSeatsFieldText() {
        return seatsField.getText();
    }
    public Object getDatePickerValue() {
        return datePicker.getModel().getValue();
    }
    public Object getTimePickerValue() {
        return timePicker.getTime();
    }
}



