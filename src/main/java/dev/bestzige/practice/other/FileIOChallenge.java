package dev.bestzige.practice.other;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileIOChallenge {
    public static void main(String[] args) {
        String FILE_PATH = "output.txt";

        Scanner scanner = new Scanner(System.in);

        List<String> lines = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH));
            String tmpLine;
            while ((tmpLine = reader.readLine()) != null) {
                lines.add(tmpLine);
            }

            for(String line : lines) {
                System.out.println(line);
            }

            System.out.println("Enter text: ");
            String text = scanner.nextLine();
            lines.add(text);

            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true));
            writer.write(text);
            writer.newLine();

            for(String line : lines) {
                System.out.println(line);
            }

            reader.close();
            writer.close();
        } catch (FileNotFoundException e) {
           throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}