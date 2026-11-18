package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String url = "jdbc:mysql://localhost:3306/college";
    private static final String username = "root";
    private static final String password = "@mitsingh4217";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
    // try {
    // Connection connection = DriverManager.getConnection(url, username, password);
    // System.out.println("Database Connected Successfully!");

    // } catch (SQLException e) {
    // System.out.println("Database Connection Failed!");
    // e.printStackTrace();
    // }

}
