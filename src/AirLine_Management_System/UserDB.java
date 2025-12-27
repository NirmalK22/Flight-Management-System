package AirLine_Management_System;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDB {
    private static final String URL = "jdbc:mysql://localhost:3306/airline_managment";
    private static final String USER = "root";
    private static final String PASSWORD = "nirmal";
    public static List<String> getFlightCodes(List<Integer> flightIds) {
        List<String> flightCodes = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "SELECT f_code FROM flight";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String flightCode = resultSet.getString("f_code");
                    flightCodes.add(flightCode);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return flightCodes;
    }
    public static int getFlightIdFromCode(String flightCode) {
        int flightId = -1;
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "SELECT f_Id FROM flight WHERE f_code = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, flightCode);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    flightId = resultSet.getInt("f_Id");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return flightId;
    }
    public static Map<String, String> getPassengerDetails(String username) {
        Map<String, String> passengerDetails = new HashMap<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "SELECT p_Id, p_name FROM passenger WHERE p_username = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    String passengerId = resultSet.getString("p_Id");
                    String passengerName = resultSet.getString("p_name");
                    passengerDetails.put("p_Id", passengerId);
                    passengerDetails.put("p_name", passengerName);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return passengerDetails;
    }
    public static List<Map<String, Object>> getPassengerBookings(String passengerID) {
        List<Map<String, Object>> bookings = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "SELECT b_Id, b_details, f_code, f_Id,p_name FROM booking WHERE p_Id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, passengerID);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Map<String, Object> booking = new HashMap<>();
                booking.put("b_Id", resultSet.getInt("b_Id"));
                booking.put("b_details", resultSet.getString("b_details"));
                booking.put("f_code", resultSet.getString("f_code"));
                booking.put("f_Id", resultSet.getInt("f_Id")); // Corrected column name
                booking.put("p_name", resultSet.getString("p_name"));
                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookings;
    }
    //Booking Details Enter
        public static boolean insertBooking(String passengerID, String flightID, String flightName, String bookingDetails, String passengerName) {
        if (passengerID.isEmpty() || flightID.isEmpty() || flightName.isEmpty() || bookingDetails.isEmpty() || passengerName.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill in all required fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String insertQuery = "INSERT INTO booking (p_ID, f_ID, f_code,  b_details, p_name) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            preparedStatement.setInt(1, Integer.parseInt(passengerID));
            preparedStatement.setInt(2, Integer.parseInt(flightID));
            preparedStatement.setString(3, flightName);
            preparedStatement.setString(4, bookingDetails);
            preparedStatement.setString(5, passengerName);

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    //Delete Booking delete
    private boolean deleteBooking(int bookingId) {
        try {

            String deleteQuery = "DELETE FROM booking WHERE b_Id = ?";
            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {

                preparedStatement.setInt(1, bookingId);

                int rowsAffected = preparedStatement.executeUpdate();

                return rowsAffected > 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    //update Booking Details
    public static boolean updateBooking(int bookingID, String details, String flightID, String flightName, String passengerID, String passengerName) {
        String updateQuery = "UPDATE booking SET b_details = ?, f_ID = ?, f_code = ?, p_ID = ?, p_name = ? WHERE b_Id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            preparedStatement.setString(1, details);
            preparedStatement.setInt(2, Integer.parseInt(flightID));
            preparedStatement.setString(3, flightName);
            preparedStatement.setInt(4, Integer.parseInt(passengerID));
            preparedStatement.setString(5, passengerName);
            preparedStatement.setInt(6, bookingID);

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static List<Integer> getFlightIds() {
        return new ArrayList<>();
    }}

