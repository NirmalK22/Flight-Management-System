package AirLine_Management_System;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class PassengerMainScreen {
    private final JPanel sideMenu;
    private final JPanel mainPanel;
    private final int sideMenuWidth = 250;
    private final String loggedInUsername;

    public PassengerMainScreen(JFrame frame, String loggedInUsername) {
        this.loggedInUsername = loggedInUsername;

        sideMenu = new JPanel();
        mainPanel = new JPanel();
        frame.add(sideMenu, BorderLayout.WEST);
        frame.add(mainPanel, BorderLayout.CENTER);

        sideMenu.setLayout(new GridBagLayout());
        sideMenu.setPreferredSize(new Dimension(sideMenuWidth, frame.getHeight()));
        sideMenu.setBackground(Color.getHSBColor(0.6f, 1.0f, 1.0f));

        JButton button1 = new JButton("Flight Schedules");
        JButton button2 = new JButton("Add Booking");
        JButton button3 = new JButton("Profile Setting");
        JButton button4 = new JButton("LogOut");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 20, 5, 20);

        sideMenu.add(button1, gbc);
        gbc.gridy = 1;
        sideMenu.add(button2, gbc);
        gbc.gridy = 2;
        sideMenu.add(button3, gbc);
        gbc.gridy = 3;
        sideMenu.add(button4, gbc);

        mainPanel.setBackground(Color.LIGHT_GRAY);

        button1.addActionListener(e -> {
            mainPanel.removeAll();
            mainPanel.setLayout(new BorderLayout());

            FlightDetailsRetriever flightDetailsRetriever = new FlightDetailsRetriever();
            flightDetailsRetriever.setPreferredSize(new Dimension(930, 600));

            mainPanel.add(flightDetailsRetriever, BorderLayout.CENTER);
            mainPanel.revalidate();
            mainPanel.repaint();
        });

        button2.addActionListener(e -> {
            mainPanel.removeAll();
            UserBooking bookingForm = new UserBooking();

            mainPanel.add(bookingForm, BorderLayout.CENTER);
            mainPanel.revalidate();
            mainPanel.repaint();
        });

        button3.addActionListener(e -> {
            mainPanel.removeAll();
            Profile profileForm = new Profile();
            profileForm.setPreferredSize(new Dimension(800, 600));
            mainPanel.add(profileForm, BorderLayout.CENTER);
            mainPanel.revalidate();
            mainPanel.repaint();
        });

        button4.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to log out?", "Log Out", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                frame.dispose();
                SwingUtilities.invokeLater(Login::new);
            }
        });

        JPanel dashboardPanel = new JPanel();
        dashboardPanel.setBackground(Color.WHITE);
        dashboardPanel.setLayout(new BorderLayout());

        ImageIcon imageIcon = new ImageIcon("C:\\Users\\ASUS\\Pictures\\3.jpg");
        Image img = imageIcon.getImage();

        Image scaledImage = img.getScaledInstance(800, 500, Image.SCALE_SMOOTH);

        ImageIcon resizedImageIcon = new ImageIcon(scaledImage);
        JLabel imageLabel = new JLabel(resizedImageIcon);

        JLabel dashboardLabel = getPassengerDetailsLabel(loggedInUsername);
        dashboardLabel.setHorizontalAlignment(SwingConstants.CENTER);

        dashboardPanel.add(imageLabel, BorderLayout.SOUTH);
        dashboardPanel.add(dashboardLabel, BorderLayout.CENTER);

        showPanel(dashboardPanel);
    }
    private JLabel getPassengerDetailsLabel(String username) {
        JLabel passengerDetailsLabel = new JLabel();

        String url = "jdbc:mysql://localhost:3306/airline_managment";
        String dbUsername = "root";
        String dbPassword = "nirmal";

        try (Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            String query = "SELECT * FROM passenger WHERE p_username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("p_name");
                String welcomeMessage = "<html><div style='text-align: center;font-size: 18px;'>"
                        + "<b>Welcome to " + name + "<br> WingSyc Airline Management System</b><br>"
                        + "We always support your seamless flying experience!<br></div></html>";
                passengerDetailsLabel.setText(welcomeMessage);
            } else {
                passengerDetailsLabel.setText("User not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return passengerDetailsLabel;
    }
    private void showPanel(JPanel panel) {
        mainPanel.removeAll();
        mainPanel.add(panel);
        mainPanel.revalidate();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Passenger Main Screen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);

        // Simulated logged-in user; replace this with the actual logged-in user's username
        String loggedInUsername = "nir";

        PassengerMainScreen passengerMainScreen = new PassengerMainScreen(frame, loggedInUsername);

        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}
