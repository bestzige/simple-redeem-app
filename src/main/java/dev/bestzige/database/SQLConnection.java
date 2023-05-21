package dev.bestzige.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SQLConnection {
    public static Connection connect() {
        String url = "jdbc:mysql://localhost:3306/redeemapp";
        String username = "root";
        String password = "";
        Connection connection = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException exception) {
            Logger.getLogger(SQLConnection.class.getName()).log(Level.SEVERE, null, exception);
        }

        return connection;
    }
}