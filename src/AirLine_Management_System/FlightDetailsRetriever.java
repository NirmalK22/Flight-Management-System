package AirLine_Management_System;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class FlightDetailsRetriever extends JPanel {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/airline_managment";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "nirmal";
    private JTextField searchField;
    public FlightDetailsRetriever() {
        setLayout(new BorderLayout());

        JButton searchButton = new JButton("Search Flights");
        JButton resetButton = new JButton("Reset");
        searchField = new JTextField(20);
        searchField.setForeground(Color.GRAY);
        searchField.setText("Enter Destination");

        searchField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (searchField.getText().equals("Enter Destination")) {
                    searchField.setText("");
                }
            }
        });

        JPanel centeringPanel = new JPanel();
        centeringPanel.setLayout(new BoxLayout(centeringPanel, BoxLayout.Y_AXIS));
        centeringPanel.setBackground(new Color(173, 216, 230));
        centeringPanel.setPreferredSize(new Dimension(800, 75));

// Create a panel for horizontal centering
        JPanel horizontalCenteringPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        horizontalCenteringPanel.setBackground(new Color(173, 216, 230));
        horizontalCenteringPanel.add(searchField);
        horizontalCenteringPanel.add(searchButton);
        horizontalCenteringPanel.add(resetButton);

        centeringPanel.add(Box.createVerticalGlue());
        centeringPanel.add(horizontalCenteringPanel);
        centeringPanel.add(Box.createVerticalGlue());

        add(centeringPanel, BorderLayout.NORTH);

        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM flight");

            DefaultTableModel tableModel = new DefaultTableModel(
                    new String[]{"Flight ID", "Flight Code", "Destination", "Number of Seats", "Flight Time", "Flight Date"}, 0);

            while (resultSet.next()) {
                int flightId = resultSet.getInt("f_Id");
                String flightCode = resultSet.getString("f_code");
                String destination = resultSet.getString("destination");
                int seats = resultSet.getInt("Seats");
                Time flightTime = resultSet.getTime("flight_time");
                Date flightDate = resultSet.getDate("flight_date");

                tableModel.addRow(new Object[]{flightId, flightCode, destination, seats, flightTime, flightDate});
            }

            JTable table = new JTable(tableModel);

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            add(scrollPane, BorderLayout.CENTER);

            searchButton.addActionListener(e -> {
                String destinationInput = searchField.getText().trim();

                if (!destinationInput.isEmpty()) {
                    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
                    table.setRowSorter(sorter);

                    RowFilter<DefaultTableModel, Object> filter = RowFilter.regexFilter("(?i)" + destinationInput, 2); // Adding "(?i)" for case-insensitive matching
                    sorter.setRowFilter(filter);

                    if (table.getRowCount() == 0) {
                        JOptionPane.showMessageDialog(this, "Flights to this destination are not available.", "No Flights", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    table.setRowSorter(null);
                }
            });
            resetButton.addActionListener(e -> {
                searchField.setText("Enter Destination");
                TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) table.getRowSorter();
                if (sorter != null) {
                    sorter.setRowFilter(null);
                }
            });
            JPanel centerPanel = new JPanel(new BorderLayout());
            centerPanel.add(scrollPane, BorderLayout.CENTER);

            ImageIcon imageIcon = new ImageIcon("C:\\Users\\ASUS\\Pictures\\flight.jpg");
            Image image = imageIcon.getImage();
            Image newImage = image.getScaledInstance(400, 300, Image.SCALE_SMOOTH);
            ImageIcon scaledImageIcon = new ImageIcon(newImage);

            JLabel imageLabel = new JLabel(scaledImageIcon);
            JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            imagePanel.add(imageLabel);
            centerPanel.add(imagePanel, BorderLayout.NORTH);

            TitledBorder titledBorder = BorderFactory.createTitledBorder("Check Flights Schedules");
            titledBorder.setTitleJustification(TitledBorder.CENTER);
            titledBorder.setTitleColor(Color.BLUE);
            Font titleFont = new Font("Arial", Font.BOLD, 16);
            titledBorder.setTitleFont(titleFont);
            centerPanel.setBorder(titledBorder);
            add(centerPanel, BorderLayout.CENTER);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to retrieve data from the database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Flight Details");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);

            FlightDetailsRetriever panel = new FlightDetailsRetriever();
            frame.add(panel);
            frame.setVisible(true);
        });
    }
}
