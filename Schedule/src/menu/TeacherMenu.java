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

public class TeacherMenu {

    private static final String filePath = "src/jsons/teacher.json";
    private static final Scanner scanner = new Scanner(System.in);

    public static String getFilePath() {
        return filePath;
    }

    public static void startMenu() {
        Scanner scanner = new Scanner(System.in);

        boolean running = true;
        while (running) {
            ConsoleCleaner.clearConsole();
            System.out.println("Teacher manager\n" +
                    "1 - Add teacher\n" +
                    "2 - Delete teacher\n" +
                    "q - Quit menu\n"
            );

            String choice = scanner.nextLine();
            switch(choice) {
                case "1":
                    addTeacher();
                    scanner.nextLine();
                    break;
                case "2":
                    deleteTeacher();
                    scanner.nextLine();
                    break;
                case "q":
                    running = false;
                    break;
            }
        }
    }

    public static void addTeacher() {
        ConsoleCleaner.clearConsole();

        long id = (long) (Math.random() * 100000);

        System.out.println("Enter teacher name: ");
        String name = scanner.nextLine();

        try {
            String teacherContent = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONArray jsonObjects = new JSONArray(teacherContent);
            JSONObject newJsonObj = new JSONObject();

            newJsonObj.put("id", Long.toString(id));
            newJsonObj.put("name", name);

            jsonObjects.put(newJsonObj);

            ExcelJSONParser.addToJSON(filePath, jsonObjects);

            System.out.print("Teacher with id: " + id + " was successful added. Press any button to return: ");
        } catch (IOException e) {
            System.out.println("Failed to read file: " + e.getMessage());
        }
    }

    public static void deleteTeacher() {
        ConsoleCleaner.clearConsole();
        try {
            System.out.println("Enter teacher ID you want to delete: ");
            long id = scanner.nextLong();
            scanner.nextLine();

            boolean isFound = JSONSearch.searchInJSON(SubjectMenu.getFilePath(), id, "id_teacher");
            if (isFound) {
                System.out.println("Couldn't delete teacher. First, delete all subjects associated with this teacher.\nPress any button to return: ");
                return;
            }

            isFound = JSONSearch.searchInJSON(LessonMenu.getFilePath(), id, "id_teacher");
            if (isFound) {
                System.out.println("Couldn't delete teacher. First, delete all lessons associated with this teacher.\nPress any button to return: ");
                return;
            }

            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONArray jsonObjects = new JSONArray(content);

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
                System.out.println("Couldn't find teacher with the ID: " + id + "\nPress any button to return: ");
                return;
            }

            ExcelJSONParser.addToJSON(filePath, jsonObjects);

            System.out.print("Teacher with id: " + id + " was successful removed. Press any button to return: ");
        } catch (IOException e) {
            System.out.println("Failed to read file: " + e.getMessage());
        }
    }
}
