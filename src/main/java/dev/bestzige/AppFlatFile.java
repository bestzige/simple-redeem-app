package dev.bestzige;

import dev.bestzige.manager.FlatFileData;
import dev.bestzige.model.Redeem;
import dev.bestzige.model.User;

import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

public class AppFlatFile {

    private static final Scanner scanner = new Scanner(System.in);
    private static final FlatFileData flatFileDataManager = new FlatFileData();
    static List<User> users = flatFileDataManager.getAllUsers();
    static List<Redeem> redeems = flatFileDataManager.getAllRedeems();

    public static void main(String[] args) {
        printSuccess("Welcome to User Redeem App!");

        while (true) {
            System.out.println("Please choose an option:");
            System.out.println("1. New User");
            System.out.println("2. Log In");
            System.out.println("3. Exit");

            System.out.println("Enter option:");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> newUser();
                case 2 -> login(null);
                case 3 -> {
                    printError("Exiting...");
                    System.exit(0);
                }
                default -> printError("Invalid choice. Please try again.");
            }
        }
    }

    private static void newUser() {
        System.out.println("Enter username:");
        String username = scanner.nextLine();

        for (User user : users) {
            if (user.getUsername().equals(username)) {
                printError("User already exists.");
                return;
            }
        }

        int userId = users.size() + 1;

        User newUser = new User(userId, username, 0);

        flatFileDataManager.createUser(newUser);

        printSuccess("User created successfully.");

        login(username);
    }

    private static void login(String username) {
        if (username == null) {
            System.out.println("Enter username:");
            username = scanner.nextLine();
        }

        User currentUser = null;
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                currentUser = user;
                break;
            }
        }

        if (currentUser == null) {
            printError("User does not exist.");
            return;
        }

        printSuccess("Logged in as " + currentUser.getUsername());

        while (true) {
            System.out.println("Please choose an option:");
            System.out.println("1. Use Redeem Code");
            System.out.println("2. Generate Redeem Code");
            System.out.println("3. List Redeem Codes");
            System.out.println("4. Get User Info");
            System.out.println("5. Logout");

            int loginChoice = scanner.nextInt();
            scanner.nextLine();

            switch (loginChoice) {
                case 1 -> useRedeemCode(currentUser);
                case 2 -> generateRedeemCode();
                case 3 -> listRedeems();
                case 4 -> getUserInfo(currentUser.getUsername());
                case 5 -> {
                    printError("Logged out.");
                    return;
                }
                default -> printError("Invalid choice. Please try again.");
            }
        }
    }

    private static void listRedeems() {
        printSuccess("Redeem Codes:");
        for (Redeem redeem : redeems) {
            printSuccess(redeem.getCode() + " " + redeem.getCredit());
        }
    }

    private static void getUserInfo(String username) {
        User currentUser = null;
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                currentUser = user;
                break;
            }
        }
        if (currentUser == null) {
            printError("User does not exist.");
            return;
        }
        printSuccess("User Info:");
        printSuccess("Username: " + currentUser.getUsername());
        printSuccess("Credit: " + currentUser.getCredit());
    }

    private static void generateRedeemCode() {
        System.out.println("Enter credit:");
        int credit = scanner.nextInt();

        String code = generateUniqueCode();

        Redeem newRedeem = new Redeem(code, credit);

        flatFileDataManager.createRedeem(newRedeem);

        printSuccess("Code: " + code);
        printSuccess("Redeem code generated successfully.");
    }

    private static String generateUniqueCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();

        while (true) {
            for (int i = 0; i < 6; i++) {
                int randomIndex = (int) (Math.random() * characters.length());
                code.append(characters.charAt(randomIndex));
            }

            boolean isUnique = true;
            for (Redeem redeem : redeems) {
                if (redeem.getCode().equals(code.toString())) {
                    isUnique = false;
                    break;
                }
            }

            if (isUnique) {
                break;
            }

            code.setLength(0);
        }

        return code.toString();
    }

    private static void useRedeemCode(User currentUser) {
        System.out.println("Enter redeem code:");
        String redeemCode = scanner.nextLine();

        Redeem currentRedeem = null;
        for (Redeem redeem : redeems) {
            if (redeem.getCode().equals(redeemCode)) {
                currentRedeem = redeem;
                break;
            }
        }

        if (currentRedeem == null) {
            printError("Redeem code does not exist.");
            return;
        }

        String message = flatFileDataManager.useRedeem(currentRedeem, currentUser);

        printSuccess(message);
    }

    private static void printError(String message) {
        PrintStream out = new PrintStream(System.out, true);
        out.printf("%c[31m" + message + "%c[0m", 0x1B, 0x1B);
        System.out.println();
    }

    private static void printSuccess(String message) {
        PrintStream out = new PrintStream(System.out, true);
        out.printf("%c[32m" + message + "%c[0m", 0x1B, 0x1B);
        System.out.println();
    }

}