package menu;

import org.json.JSONArray;
import org.json.JSONObject;
import parser.ExcelJSONParser;
import utils.ConsoleCleaner;
import utils.JSONSearch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class StudentMenu {

    private static final String filePath = "src/jsons/student.json";
    private static final Scanner scanner = new Scanner(System.in);

    public static String getFilePath() {
        return filePath;
    }

    public static void startMenu() {
        Scanner scanner = new Scanner(System.in);

        boolean running = true;
        while (running) {
            ConsoleCleaner.clearConsole();
            System.out.println("Student manager\n" +
                    "1 - Add student\n" +
                    "2 - Delete student\n" +
                    "q - Quit menu\n"
            );

            String choice = scanner.nextLine();
            switch(choice) {
                case "1":
                    addStudent();
                    scanner.nextLine();
                    break;
                case "2":
                    deleteStudent();
                    scanner.nextLine();
                    break;
                case "q":
                    running = false;
                    break;
            }
        }
    }

    public static void addStudent() {
        ConsoleCleaner.clearConsole();
        try {
            long id = (long) (Math.random() * 100000);

            System.out.println("Enter student name: ");
            String name = scanner.nextLine();

            System.out.println("Enter group ID: ");
            long groupId = scanner.nextLong();
            scanner.nextLine();

            boolean isFound = JSONSearch.searchInJSON(GroupMenu.getFilePath(), groupId, "id");
            if (!isFound) {
                System.out.println("Couldn't find group with the ID: " + groupId + "\nPress any button to return: ");
                return;
            }

            String studentContent = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONArray jsonObjects = new JSONArray(studentContent);
            JSONObject newJsonObj = new JSONObject();

            newJsonObj.put("id", Long.toString(id));
            newJsonObj.put("name", name);
            newJsonObj.put("id_group", Long.toString(groupId));

            jsonObjects.put(newJsonObj);

            ExcelJSONParser.addToJSON(filePath, jsonObjects);

            System.out.print("Student with id: " + id + " was successful added. Press any button to return: ");
        } catch (IOException e) {
            System.out.println("Failed to read file: " + e.getMessage());
        }
    }

    public static void deleteStudent() {
        ConsoleCleaner.clearConsole();
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONArray jsonObjects = new JSONArray(content);

            System.out.println("Enter student ID you want to delete: ");
            long id = scanner.nextLong();
            scanner.nextLine();

            boolean isDeleted = false;
            for (int i = 0; i < jsonObjects.length() && !isDeleted; i++) {
                JSONObject jsonObj = jsonObjects.optJSONObject(i);
                long jsonId = jsonObj.getLong("id");
                if (id == jsonId) {
                    jsonObjects.remove(i);
                    isDeleted = true;
                    break;
                }
            }

            if (!isDeleted) {
                System.out.println("Couldn't find student with the ID: " + id + "\nPress any button to return: ");
                return;
            }

            ExcelJSONParser.addToJSON(filePath, jsonObjects);

            System.out.print("Student with id: " + id + " was successful removed. Press any button to return: ");
        } catch (IOException e) {
            System.out.println("Failed to read file: " + e.getMessage());
        }
    }
}
