package dev.bestzige.practice.employee;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EmployeeManager {
    private final String FILE_PATH = "employee.dat";
    private List<Employee> employeeList = new ArrayList<>();

    public EmployeeManager() {
        loadData();
    }

    private void loadData() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Failed to create file: " + FILE_PATH);
            }
        }

        if (file.exists() && file.length() > 0) {
            try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
                this.employeeList = (List<Employee>) input.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<Employee> getEmployeeList() {
        return this.employeeList;
    }

    public void addEmployee(String name, int age, String department) {
        int maxId = this.employeeList.stream().map(Employee::getId).max(Comparator.naturalOrder()).orElse(0);

        boolean isNameExist = this.employeeList.stream()
                .anyMatch(data -> data.getName().equals(name));
        if (isNameExist) {
            System.out.println("Employee Name already exist");
            return;
        }

        Employee employee = new Employee(maxId + 1, name, age, department);
        this.employeeList.add(employee);
        saveEmployee();
    }

    public Employee viewEmployee(int employeeId) {
        return this.employeeList.stream()
                .filter(data -> data.getId() == employeeId)
                .findFirst()
                .orElse(null);
    }

    public void deleteEmployee(int employeeId) {
        Employee employee = viewEmployee(employeeId);
        if (employee == null || !this.employeeList.contains(employee)) {
            System.out.println("Employee not found");
            return;
        }

        this.employeeList.remove(employee);
        saveEmployee();
    }

    public void saveEmployee() {
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            output.writeObject(this.employeeList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
