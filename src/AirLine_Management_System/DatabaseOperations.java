package AirLine_Management_System;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class DatabaseOperations {
    private static final String URL = "jdbc:mysql://localhost:3306/airline_managment";
    private static final String USER = "root";
    private static final String PASSWORD = "nirmal";
    //Getting Details for the Fields
    public static List<Integer> populatePassengerIDs() {
        List<Integer> passengerIDs = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "SELECT p_Id FROM passenger";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    int passengerID = resultSet.getInt("p_Id");
                    passengerIDs.add(passengerID);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return passengerIDs;
    }
    public static List<String> getFlightCodes() {
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
    public static String fetchPassengerName(int passengerID) {
        String passengerName = "";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "SELECT p_name FROM passenger WHERE p_Id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, passengerID);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    passengerName = resultSet.getString("p_name");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return passengerName;
    }
    //Booking Details Adding
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
//Delete the booking details
    public static boolean deleteBooking(int bookingID) {
        String deleteQuery = "DELETE FROM booking WHERE b_Id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {

            preparedStatement.setInt(1, bookingID);

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
//Update the Booking details
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
}
