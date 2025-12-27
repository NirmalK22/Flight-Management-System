package AirLine_Management_System;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.awt.BorderLayout.*;

public class AdminMainScreen {
    private JPanel sideMenu,mainPanel;
    private boolean isOpen = false;
    private final int sideMenuWidth = 250;

    public AdminMainScreen(JFrame frame) {
        sideMenu = new JPanel();
        mainPanel = new JPanel();
        frame.add(sideMenu, WEST);
        frame.add(mainPanel, CENTER);

        // Setting up side menu appearance
        sideMenu.setLayout(new GridBagLayout());
        sideMenu.setPreferredSize(new Dimension(sideMenuWidth, frame.getHeight()));
        sideMenu.setBackground(Color.getHSBColor(0.6f, 1.0f, 1.0f));

        JButton button1 = new JButton("Manage Flight Details");
        JButton button2 = new JButton("Manage Passenger Details");
        JButton button3 = new JButton("Manage Booking Details");
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

        mainPanel.setBackground(Color.WHITE);
        isOpen = true;
        //LogOut Button Action
        button4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to log out?", "Log Out", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    frame.dispose();
                    SwingUtilities.invokeLater(AdminLogin::new);
                }
            }
        });
//Flight Details
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanel.removeAll();
                Flight flightForm = new Flight();
                mainPanel.add(flightForm, CENTER);
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });
        //Passbegr Details
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanel.removeAll();
                int retrievedId = 0;
                String retrievedName = null;
                Passenger passengerForm = new Passenger(retrievedId, retrievedName);
                mainPanel.add(passengerForm, CENTER);
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });
//Booking Details
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanel.removeAll();
                Booking bookingForm = new Booking();
                mainPanel.add(bookingForm, CENTER);
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });
        JPanel dashboardPanel = new JPanel();
        dashboardPanel.setBackground(Color.LIGHT_GRAY);
        dashboardPanel.setLayout(new BorderLayout());
        JLabel dashboardLabel = new JLabel("<html><div style='text-align: center; font-size: 16px;'>" +
                "<h1 style='font-size: 20px;'>Welcome, Administrator!</h1>" +
                "<p>Manage your airline operations efficiently.</p>" +
                "<p>Review and update flight, passenger, and booking details.</p>" +
                "<p>Stay informed about the latest system updates and notifications.</p>" +
                "<p>Use the side menu to navigate through different management options.</p>" +
                "<p>For assistance, refer to the user manual or contact support.</p>" +
                "</div></html>");
        dashboardLabel.setHorizontalAlignment(SwingConstants.CENTER);

        dashboardLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dashboardPanel.add(dashboardLabel, CENTER);

        ImageIcon imageIcon = new ImageIcon("C:\\Users\\ASUS\\Pictures\\Admin.jpg");
        Image image = imageIcon.getImage();
        Image scaledImage = image.getScaledInstance(600, 400, Image.SCALE_SMOOTH);
        ImageIcon scaledImageIcon = new ImageIcon(scaledImage);

        JLabel imageLabel = new JLabel(scaledImageIcon);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        dashboardPanel.setLayout(new BorderLayout());
        dashboardPanel.add(dashboardLabel, NORTH);
        dashboardPanel.add(imageLabel, CENTER);

        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(dashboardPanel, CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Admin Main Screen");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1300, 800);

            AdminMainScreen adminMainScreen = new AdminMainScreen(frame);

            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
        });
    }
}
