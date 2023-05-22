package dev.bestzige.practice.other;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TryInputStreamReader {
    public static void main(String[] args) {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Enter your name: ");
            String name = bufferedReader.readLine();

            System.out.println("Your name is: " + name);

            String message;
            while (true) {
                message = bufferedReader.readLine();
                if (message == null || message.isEmpty()) {
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }

    }
}
