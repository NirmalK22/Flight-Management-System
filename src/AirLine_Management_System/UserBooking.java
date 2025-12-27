package AirLine_Management_System;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import static AirLine_Management_System.DatabaseOperations.deleteBooking;
import static AirLine_Management_System.DatabaseOperations.updateBooking;

public class UserBooking extends JPanel {
    private JLabel pidLabel, nameLabel, detailsLabel, codeLabel, fidLabel;
    private JTextField pidField, detailsField, fidField;
    private static JTextField nameField;
    private JComboBox<String> codeField;
    private JPanel northPanel = new JPanel();
    private GridBagConstraints gbc = new GridBagConstraints();
    private JTable bookingTable;
    private String loggedInUsername;

    private static final String URL = "jdbc:mysql://localhost:3306/airline_managment";
    private static final String USER = "root";
    private static final String PASSWORD = "nirmal";

    public UserBooking() {
        setLayout(new BorderLayout());

        northPanel = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        pidLabel = new JLabel("Passenger ID");
        pidField = new JTextField(15);
        nameLabel = new JLabel("Passenger Name");
        nameField = new JTextField(15);
        detailsLabel = new JLabel("Booking Details");
        detailsField = new JTextField(15);
        codeLabel = new JLabel("Flight Code");
        codeField = new JComboBox<>();
        fidLabel = new JLabel("Flight ID");
        fidField = new JTextField(15);

        addToPanelCustom(northPanel, gbc, pidLabel, 0, 0);
        addToPanelCustom(northPanel, gbc, pidField, 1, 0);
        addToPanelCustom(northPanel, gbc, nameLabel, 2, 0);
        addToPanelCustom(northPanel, gbc, nameField, 3, 0);
        addToPanelCustom(northPanel, gbc, detailsLabel, 4, 0);
        addToPanelCustom(northPanel, gbc, detailsField, 5, 0);
        addToPanelCustom(northPanel, gbc, codeLabel, 0, 1);
        addToPanelCustom(northPanel, gbc, codeField, 1, 1);
        addToPanelCustom(northPanel, gbc, fidLabel, 2, 1);
        addToPanelCustom(northPanel, gbc, fidField, 3, 1);

        JButton submit = new JButton("Add Booking");
        JButton update = new JButton("Change Booking");
        JButton delete = new JButton("Delete Booking");

        addToPanelCustom(northPanel, gbc, submit, 0, 2);
        addToPanelCustom(northPanel, gbc, update, 1, 2);
        addToPanelCustom(northPanel, gbc, delete, 2, 2);

        add(northPanel, BorderLayout.NORTH);

        bookingTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(bookingTable);
        add(scrollPane, BorderLayout.CENTER);

        setPreferredSize(new Dimension(900, 600));
        setVisible(true);

        String loggedInUsername = performLogin();

        if (loggedInUsername != null && !loggedInUsername.isEmpty()) {
            Map<String, String> loggedInPassenger = UserDB.getPassengerDetails(loggedInUsername);
            if (!loggedInPassenger.isEmpty()) {
                pidField.setText(loggedInPassenger.get("p_Id"));
                nameField.setText(loggedInPassenger.get("p_name"));
            }
        }

        List<String> flightCodes = UserDB.getFlightCodes(UserDB.getFlightIds());
        for (String code : flightCodes) {
            codeField.addItem(code);
        }

        codeField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedCode = (String) codeField.getSelectedItem();
                int flightId = UserDB.getFlightIdFromCode(selectedCode);
                fidField.setText(String.valueOf(flightId));
            }
        });

        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String passengerID = pidField.getText();
                String flightID = fidField.getText();
                String flightName = (String) codeField.getSelectedItem();
                String bookingDetails = detailsField.getText();
                String passengerName = nameField.getText();

                boolean insertionSuccess = UserDB.insertBooking(passengerID, flightID, flightName, bookingDetails, passengerName);

                if (insertionSuccess) {
                    JOptionPane.showMessageDialog(null, "Booking added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    displayPassengerBookingDetails();
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to add booking.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        displayPassengerBookingDetails();
//Delete Booking Details
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = bookingTable.getSelectedRow();

                if (selectedRow != -1) {
                    int bookingId = (int) bookingTable.getValueAt(selectedRow, 0);
                    boolean deletionSuccess = deleteBooking(bookingId);

                    if (deletionSuccess) {
                        JOptionPane.showMessageDialog(null, "Booking deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        displayPassengerBookingDetails();
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to delete booking.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a booking to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
//Update Booking Details
        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = bookingTable.getSelectedRow();

                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Please select a row to update.");
                    return;
                }
                int bookingId = (int) bookingTable.getValueAt(selectedRow, 0);
                String details = detailsField.getText();
                String flightCode = codeField.getSelectedItem().toString();
                String flightID = fidField.getText();
                String passengerID = pidField.getText();
                String passengerName = nameField.getText();

                boolean updateSuccessful = updateBooking(bookingId, details, flightID, flightCode, passengerID, passengerName);

                if (updateSuccessful) {
                    DefaultTableModel model = (DefaultTableModel) bookingTable.getModel();
                    model.setValueAt(details, selectedRow, 1);
                    model.setValueAt(flightCode, selectedRow, 2);
                    model.setValueAt(flightID, selectedRow, 3);
                    model.setValueAt(passengerID, selectedRow, 4);


                    JOptionPane.showMessageDialog(null, "Booking details updated successfully!");
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to update booking details. Please try again.");
                }
            }
        });
    }
    //Showing paassenegr Details
    private void displayPassengerBookingDetails() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Booking ID");
        model.addColumn("Booking Details");
        model.addColumn("Flight Code");
        model.addColumn("Flight ID");
        model.addColumn("Passenger Name");

        String loggedInPassengerID = pidField.getText();

        List<Map<String, Object>> bookings = UserDB.getPassengerBookings(loggedInPassengerID);

        for (Map<String, Object> booking : bookings) {
            model.addRow(new Object[]{
                    booking.get("b_Id"),
                    booking.get("b_details"),
                    booking.get("f_code"),
                    booking.get("f_Id"),
                    booking.get("p_name")
            });
        }
        bookingTable.setModel(model);
    }
    private void addToPanelCustom(JPanel panel, GridBagConstraints gbc, Component comp, int gridx, int gridy) {
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        panel.add(comp, gbc);
    }
    private String performLogin() {
        String username;
        boolean isValid = false;

        do {
            username = JOptionPane.showInputDialog("Enter Your Username");

            if (username != null && !username.isEmpty()) {
                isValid = isValidUsername(username);
                if (isValid) {
                    loggedInUsername = username;
                }
            } else {
                break;
            }

            if (!isValid) {
                JOptionPane.showMessageDialog(null, "Invalid username! Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } while (!isValid);

        return loggedInUsername;
    }
    private boolean isValidUsername(String username) {
        Map<String, String> loggedInPassenger = UserDB.getPassengerDetails(username);
        return !loggedInPassenger.isEmpty();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Airline Booking System");
        UserBooking userBooking = new UserBooking();
        frame.getContentPane().add(userBooking);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
