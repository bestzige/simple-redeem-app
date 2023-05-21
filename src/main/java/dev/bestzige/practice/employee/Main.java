package dev.bestzige.practice.employee;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static final EmployeeManager employeeManager = new EmployeeManager();
    private static final Scanner scanner = new Scanner(System.in);

    private static final HashMap<Integer, String> optionMap = new HashMap<>(
            Map.of(
                    1, "Add employee",
                    2, "View employee",
                    3, "Delete employee",
                    4, "List employee",
                    5, "Exit"
            )
    );

    public static void main(String[] args) {
        while (true) {
            System.out.println("-------------------------");
            System.out.println("Please select an options:");
            optionMap.forEach((key, value) -> System.out.println(key + ". " + value));
            System.out.println("-------------------------");
            System.out.println("Enter your option:");
            int option = scanner.nextInt();
            scanner.nextLine();

            System.out.println();
            System.out.println("You selected: " + optionMap.get(option));
            System.out.println();

            switch (option) {
                case 1 -> addEmployee();
                case 2 -> viewEmployee();
                case 3 -> deleteEmployee();
                case 4 -> listEmployee();
                case 5 -> {
                    System.out.println("Existing this app...");
                    System.exit(0);
                }
                default -> System.out.println("No Action");
            }
        }
    }

    private static void addEmployee() {
        System.out.println("Enter employee [name, age, department]: (Example type: BestZige 19 IT): ");
        String name = scanner.next();
        int age = Integer.parseInt(scanner.next());
        String department = scanner.next();

        employeeManager.addEmployee(name, age, department);
    }

    private static void viewEmployee() {
        System.out.println("Enter employee id that you want to view infomation: ");
        int employeeId = scanner.nextInt();

        Employee employee = employeeManager.viewEmployee(employeeId);
        if(employee == null) {
            System.out.println("Employee not found");
            return;
        }
        System.out.println(employee);
    }

    private static void deleteEmployee() {
        System.out.println("Enter employee id that you want to delete: ");
        int employeeId = scanner.nextInt();

        Employee employee = employeeManager.viewEmployee(employeeId);
        employeeManager.deleteEmployee(employeeId);
        System.out.println("Deleted " + employee);
    }

    private static void listEmployee() {
        System.out.println("List of employees: ");
        employeeManager.getEmployeeList().forEach(System.out::println);
    }
}
