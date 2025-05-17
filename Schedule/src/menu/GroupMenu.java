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

public class GroupMenu{

    private static final String filePath = "src/jsons/group.json";
    private static final Scanner scanner = new Scanner(System.in);

    public static String getFilePath() {
        return filePath;
    }

    public static void startMenu() {
        Scanner scanner = new Scanner(System.in);

        boolean running = true;
        while (running) {
            ConsoleCleaner.clearConsole();
            System.out.println("Group manager\n" +
                    "1 - Add group\n" +
                    "2 - Delete group\n" +
                    "q - Quit menu\n"
            );

            String choice = scanner.nextLine();
            switch(choice) {
                case "1":
                    addGroup();
                    scanner.nextLine();
                    break;
                case "2":
                    deleteGroup();
                    scanner.nextLine();
                    break;
                case "q":
                    running = false;
                    break;
            }
        }
    }

    public static void addGroup() {
        ConsoleCleaner.clearConsole();
        try {
            long id = (long) (Math.random() * 100000);

            System.out.println("Enter group name: ");
            String name = scanner.nextLine();

            String groupContent = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONArray groups = new JSONArray(groupContent);
            JSONObject group = new JSONObject();

            group.put("id", Long.toString(id));
            group.put("name", name);

            groups.put(group);

            ExcelJSONParser.addToJSON(filePath, groups);

            System.out.print("Group with id: " + id + " was successful added. Press any button to return: ");
        } catch (IOException e) {
            System.out.println("Failed to read file: " + e.getMessage());
        }
    }

    public static void deleteGroup() {
        ConsoleCleaner.clearConsole();
        try {
            System.out.println("Enter group ID you want to delete: ");
            long id = scanner.nextLong();
            scanner.nextLine();

            boolean isFound = JSONSearch.searchInJSON(StudentMenu.getFilePath(), id, "id_group");
            if (isFound) {
                System.out.println("Couldn't delete group. First, delete all students associated with this group.\nPress any button to return: ");
                return;
            }

            isFound = JSONSearch.searchInJSON(LessonMenu.getFilePath(), id, "id_group");
            if (isFound) {
                System.out.println("Couldn't delete group. First, delete all lessons associated with this group.\nPress any button to return: ");
                return;
            }

            String groupContent = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONArray groups = new JSONArray(groupContent);

            boolean isDeleted = false;
            for (int i = 0; i < groups.length() && !isDeleted; i++) {
                JSONObject jsonObj = groups.optJSONObject(i);
                long jsonId = jsonObj.getLong("id");
                if (id == jsonId) {
                    groups.remove(i);
                    isDeleted = true;
                    break;
                }
            }

            if (!isDeleted) {
                System.out.println("Couldn't find group with the ID: " + id + "\nPress any button to return: ");
                return;
            }

            ExcelJSONParser.addToJSON(filePath, groups);

            System.out.print("Group with id: " + id + " was successful removed. Press any button to return: ");
        } catch (IOException e) {
            System.out.println("Failed to read file: " + e.getMessage());
        }
    }
}
