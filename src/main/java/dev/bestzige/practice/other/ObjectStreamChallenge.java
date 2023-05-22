package dev.bestzige.practice.other;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ObjectStreamChallenge {
    public static void main(String[] args) {

        String FILE_PATH = "output2.txt";
        List<Student> students = new ArrayList<>();

        Student s1 = new Student(1, "Test1");
        Student s2 = new Student(2, "Test2");
        Student s3 = new Student(3, "Test3");
        students.add(s1);
        students.add(s2);
        students.add(s3);
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            output.writeObject(students);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            List<Student> data = (List<Student>) input.readObject();
            for(Student student : data) {
                System.out.println(student.getId() + ": " + student.getName());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
