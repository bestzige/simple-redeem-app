package dev.bestzige.manager;

import dev.bestzige.database.SQLConnection;
import dev.bestzige.model.Redeem;
import dev.bestzige.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLData {
    private final List<User> users = new ArrayList<>();
    private final List<Redeem> redeems = new ArrayList<>();

    public MySQLData() {
        createTablesIfNotExist();
        loadData();
    }

    private void createTablesIfNotExist() {
        try (Connection connection = SQLConnection.connect();
             Statement statement = connection.createStatement()) {
            // Create users table
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS users (" +
                    "id INT PRIMARY KEY, " +
                    "username VARCHAR(255) NOT NULL, " +
                    "credit INT NOT NULL)");

            // Create redeems table
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS redeems (" +
                    "code VARCHAR(255) PRIMARY KEY, " +
                    "credit INT NOT NULL)");
        } catch (SQLException e) {
            System.out.println("Create tables failed: " + e.getMessage());
        }
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void loadData() {
        try (Connection connection = SQLConnection.connect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM users")) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                int credit = resultSet.getInt("credit");
                User user = new User(id, username, credit);
                addUser(user);
            }
        } catch (SQLException e) {
            System.out.println("Load users failed: " + e.getMessage());
        }

        try (Connection connection = SQLConnection.connect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM redeems")) {
            while (resultSet.next()) {
                String code = resultSet.getString("code");
                int credit = resultSet.getInt("credit");
                Redeem redeem = new Redeem(code, credit);
                redeems.add(redeem);
            }
        } catch (SQLException e) {
            System.out.println("Load redeems failed: " + e.getMessage());
        }
    }

    public List<User> getAllUsers() {
        return users;
    }

    public void createUser(User user) {
        try (Connection connection = SQLConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO users (id, username, credit) VALUES (?, ?, ?)")) {
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setInt(3, user.getCredit());
            preparedStatement.executeUpdate();

            addUser(user);
        } catch (SQLException e) {
            System.out.println("Create user failed: " + e.getMessage());
        }
    }

    public void updateUser(User user) {
        try (Connection connection = SQLConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE users SET username = ?, credit = ? WHERE id = ?")) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setInt(2, user.getCredit());
            preparedStatement.setInt(3, user.getId());
            preparedStatement.executeUpdate();

            for (User u : users) {
                if (u.getId() == user.getId()) {
                    u.setUsername(user.getUsername());
                    u.setCredit(user.getCredit());
                    break;
                }
            }
        } catch (SQLException e) {
            System.out.println("Update user failed: " + e.getMessage());
        }
    }

    public void deleteUser(User user) {
        try (Connection connection = SQLConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM users WHERE id = ?")) {
            preparedStatement.setInt(1, user.getId());
            preparedStatement.executeUpdate();

            users.remove(user);
        } catch (SQLException e) {
            System.out.println("Delete user failed: " + e.getMessage());
        }
    }

    public List<Redeem> getAllRedeems() {
        return redeems;
    }

    public void createRedeem(Redeem redeem) {
        try (Connection connection = SQLConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO redeems (code, credit) VALUES (?, ?)")) {
            preparedStatement.setString(1, redeem.getCode());
            preparedStatement.setInt(2, redeem.getCredit());
            preparedStatement.executeUpdate();

            redeems.add(redeem);
        } catch (SQLException e) {
            System.out.println("Create redeem failed: " + e.getMessage());
        }
    }

    public String useRedeem(Redeem redeem, User user) {
        try (Connection connection = SQLConnection.connect();
             PreparedStatement updateUserStatement = connection.prepareStatement(
                     "UPDATE users SET credit = credit + ? WHERE id = ?");
             PreparedStatement deleteRedeemStatement = connection.prepareStatement(
                     "DELETE FROM redeems WHERE code = ?")) {

            connection.setAutoCommit(false);

            updateUserStatement.setInt(1, redeem.getCredit());
            updateUserStatement.setInt(2, user.getId());
            updateUserStatement.executeUpdate();

            deleteRedeemStatement.setString(1, redeem.getCode());
            deleteRedeemStatement.executeUpdate();

            connection.commit();

            redeems.remove(redeem);
            user.setCredit(user.getCredit() + redeem.getCredit());
            updateUser(user);

            return "User " + user.getUsername() + " has used redeem code " + redeem.getCode() +
                    " and got " + redeem.getCredit() + " credits.";
        } catch (SQLException e) {
            System.out.println("Failed to use redeem: " + e.getMessage());
            return "Failed to use redeem.";
        }
    }
}
