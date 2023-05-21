package dev.bestzige.manager;

import dev.bestzige.model.Redeem;
import dev.bestzige.model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FlatFileData {
    private static final String USERS_FILE_PATH = "users.txt";
    private static final String REDEEMS_FILE_PATH = "redeems.txt";

    private final List<User> users = new ArrayList<>();
    private final List<Redeem> redeems = new ArrayList<>();

    public FlatFileData() {
        createFilesIfNotExist();
        loadData();
    }

    private void createFilesIfNotExist() {
        createFileIfNotExist(USERS_FILE_PATH);
        createFileIfNotExist(REDEEMS_FILE_PATH);
    }

    private void createFileIfNotExist(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Failed to create file: " + filePath);
            }
        }
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void loadData() {
        // Load User
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                int id = Integer.parseInt(userData[0]);
                String username = userData[1];
                int credit = Integer.parseInt(userData[2]);
                User user = new User(id, username, credit);
                addUser(user);
            }
        } catch (IOException e) {
            System.out.println("Failed to load users from file: " + USERS_FILE_PATH);
        }

        // Load Redeem
        try (BufferedReader reader = new BufferedReader(new FileReader(REDEEMS_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] redeemData = line.split(",");
                String code = redeemData[0];
                int credit = Integer.parseInt(redeemData[1]);
                Redeem redeem = new Redeem(code, credit);
                redeems.add(redeem);
            }
        } catch (IOException e) {
            System.out.println("Failed to load redeems from file: " + REDEEMS_FILE_PATH);
        }
    }

    public List<User> getAllUsers() {
        return users;
    }

    public void createUser(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE_PATH, true))) {
            String userData = user.getId() + "," + user.getUsername() + "," + user.getCredit();
            writer.write(userData);
            writer.newLine();

            addUser(user);
        } catch (IOException e) {
            System.out.println("Failed to create user in file: " + USERS_FILE_PATH);
        }
    }

    public void updateUser(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE_PATH))) {
            for (User u : users) {
                if (u.getId() == user.getId()) {
                    u.setUsername(user.getUsername());
                    u.setCredit(user.getCredit());
                }
                String userData = u.getId() + "," + u.getUsername() + "," + u.getCredit();
                writer.write(userData);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Failed to update user in file: " + USERS_FILE_PATH);
        }
    }

    public List<Redeem> getAllRedeems() {
        return redeems;
    }

    public void createRedeem(Redeem redeem) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(REDEEMS_FILE_PATH, true))) {
            String redeemData = redeem.getCode() + "," + redeem.getCredit();
            writer.write(redeemData);
            writer.newLine();

            redeems.add(redeem);
        } catch (IOException e) {
            System.out.println("Failed to create redeem in file: " + REDEEMS_FILE_PATH);
        }
    }

    public String useRedeem(Redeem redeem, User user) {
        redeems.remove(redeem);
        saveRedeems();
        user.setCredit(user.getCredit() + redeem.getCredit());
        updateUser(user);

        return "User " + user.getUsername() + " has used redeem code " + redeem.getCode() + " and got " + redeem.getCredit() + " credits.";
    }

    public void saveRedeems() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(REDEEMS_FILE_PATH))) {
            for (Redeem redeem : redeems) {
                String redeemData = redeem.getCode() + "," + redeem.getCredit();
                writer.write(redeemData);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Failed to save redeems in file: " + REDEEMS_FILE_PATH);
        }
    }
}
