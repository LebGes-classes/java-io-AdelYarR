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

public class SubjectMenu {

    private static final String filePath = "src/jsons/subject.json";
    private static final Scanner scanner = new Scanner(System.in);

    public static String getFilePath() {
        return filePath;
    }

    public static void startMenu() {
        Scanner scanner = new Scanner(System.in);

        boolean running = true;
        while (running) {
            ConsoleCleaner.clearConsole();
            System.out.println("Subject manager\n" +
                    "1 - Add subject\n" +
                    "2 - Delete subject\n" +
                    "q - Quit menu\n"
            );

            String choice = scanner.nextLine();
            switch(choice) {
                case "1":
                    addSubject();
                    scanner.nextLine();
                    break;
                case "2":
                    deleteSubject();
                    scanner.nextLine();
                    break;
                case "q":
                    running = false;
                    break;
            }
        }
    }

    public static void addSubject() {
        ConsoleCleaner.clearConsole();

        long id = (long) (Math.random() * 100000);

        System.out.println("Enter subject name: ");
        String name = scanner.nextLine();

        try {
            String subjectContent = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONArray subjects = new JSONArray(subjectContent);
            JSONObject subject = new JSONObject();

            subject.put("id", Long.toString(id));
            subject.put("name", name);

            subjects.put(subject);

            ExcelJSONParser.addToJSON(filePath, subjects);

            System.out.print("Subject with id: " + id + " was successful added. Press any button to return: ");
        } catch (IOException e) {
            System.out.println("Failed to read file: " + e.getMessage());
        }
    }

    public static void deleteSubject() {
        ConsoleCleaner.clearConsole();
        try {
            System.out.println("Enter subject ID you want to delete: ");
            long id = scanner.nextLong();
            scanner.nextLine();

            boolean isFound = JSONSearch.searchInJSON(LessonMenu.getFilePath(), id, "id_subject");
            if (isFound) {
                System.out.println("Couldn't delete subject. First, delete all lessons associated with this group.\nPress any button to return: ");
                return;
            }

            String subjectContent = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONArray subjects = new JSONArray(subjectContent);

            boolean isDeleted = false;
            for (int i = 0; i < subjects.length() && !isDeleted; i++) {
                JSONObject jsonObj = subjects.optJSONObject(i);
                long jsonId = jsonObj.getLong("id");
                if (id == jsonId) {
                    subjects.remove(i);
                    isDeleted = true;
                    break;
                }
            }

            if (!isDeleted) {
                System.out.println("Couldn't find subject with the ID: " + id + "\nPress any button to return: ");
                return;
            }

            ExcelJSONParser.addToJSON(filePath, subjects);

            System.out.print("Subject with id: " + id + " was successful removed. Press any button to return: ");
        } catch (IOException e) {
            System.out.println("Failed to read file: " + e.getMessage());
        }
    }
}
