package AirLine_Management_System;

import javax.swing.*;

public class Booking extends JPanel {
    private Bookdetails bookPanel;
    public Booking() {
        initializeUI();
    }

    private void initializeUI() {
        bookPanel = new Bookdetails();
        add(bookPanel);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Airline Management System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 600);

            Booking bookingDetails = new Booking();
            frame.add(bookingDetails);
            frame.setVisible(true);
        });
    }
}

