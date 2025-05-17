package menu;

import org.json.JSONArray;
import org.json.JSONObject;
import parser.ExcelJSONParser;
import utils.ConsoleCleaner;
import utils.JSONSearch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.util.Collections;

public class LessonMenu {

    private static final String filePath = "src/jsons/lesson.json";
    private static final Scanner scanner = new Scanner(System.in);

    public static String getFilePath() {
        return filePath;
    }

    public static void startMenu() {
        Scanner scanner = new Scanner(System.in);

        boolean running = true;
        while (running) {
            ConsoleCleaner.clearConsole();
            System.out.println("Lesson manager\n" +
                    "1 - Get Schedule\n" +
                    "2 - Add Lesson\n" +
                    "3 - Delete Lesson\n" +
                    "q - Quit menu\n"
            );

            String choice = scanner.nextLine();
            switch(choice) {
                case "1":
                    getLessonByDayAndTeacher();
                    scanner.nextLine();
                    break;
                case "2":
                    addLesson();
                    scanner.nextLine();
                    break;
                case "3":
                    deleteLesson();
                    scanner.nextLine();
                    break;
                case "q":
                    running = false;
                    break;
            }
        }
    }

    public static void addLesson() {
        ConsoleCleaner.clearConsole();
        try {
            int id = (int) (Math.random() * 100000);

            System.out.println("Enter day of the week: ");
            String weekDay = scanner.nextLine();

            System.out.println("Enter lesson number: ");
            int lessonNumber = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Enter teacher id: ");
            long idTeacher = scanner.nextLong();
            scanner.nextLine();

            boolean isFound = JSONSearch.searchInJSON(TeacherMenu.getFilePath(), idTeacher, "id");
            if (!isFound) {
                System.out.println("Couldn't find teacher with the ID: " + idTeacher + ".\nPress any key to return: ");
                return;
            }

            System.out.println("Enter group id: ");
            long idGroup = scanner.nextLong();
            scanner.nextLine();

            isFound = JSONSearch.searchInJSON(GroupMenu.getFilePath(), idGroup, "id");
            if (!isFound) {
                System.out.println("Couldn't find group with the ID: " + idTeacher + ".\nPress any key to return: ");
                return;
            }

            System.out.println("Enter subject id: ");
            long idSubject = scanner.nextLong();
            scanner.nextLine();

            isFound = JSONSearch.searchInJSON(SubjectMenu.getFilePath(), idSubject, "id");
            if (!isFound) {
                System.out.println("Couldn't find subject with the ID: " + idSubject + ".\nPress any key to return: ");
                return;
            }

            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONArray jsonObjects = new JSONArray(content);
            JSONObject newJsonObj = new JSONObject();

            newJsonObj.put("id", id);
            newJsonObj.put("lesson_number", lessonNumber);
            newJsonObj.put("week_day", weekDay);
            newJsonObj.put("id_subject", idSubject);
            newJsonObj.put("id_teacher", idTeacher);
            newJsonObj.put("id_group", idGroup);

            jsonObjects.put(newJsonObj);

            ExcelJSONParser.addToJSON(filePath, jsonObjects);

            System.out.print("Lesson with id: " + id + " was successful added. Press any button to return: ");
        } catch (IOException e) {
            System.out.println("Failed to read file: " + e.getMessage());
        }

        scanner.nextLine();
    }

    public static void deleteLesson() {
        ConsoleCleaner.clearConsole();
        try {
            System.out.println("Enter lesson id: ");
            long id = scanner.nextLong();
            scanner.nextLine();

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
                System.out.println("Couldn't find student with the ID: " + id + "\nPress any button to return: ");
                return;
            }

            ExcelJSONParser.addToJSON(filePath, jsonObjects);
            System.out.print("Lesson with id: " + id + " was successful removed. Press any button to return: ");
        } catch (IOException e) {
            System.out.println("Failed to read file: " + e.getMessage());
        }

        scanner.nextLine();
    }
    public static void getLessonByDayAndTeacher() {
        ConsoleCleaner.clearConsole();
        try {
            System.out.println("Enter teacher id: ");
            long idTeacher = scanner.nextLong();
            scanner.nextLine();

            System.out.println("Enter day of the week: ");
            String weekDay = scanner.nextLine();

            System.out.println("Schedule information");
            System.out.println("-------------------------------------------------------------------");

            String headerFormat = "%-15s | %-15s | %-15s | %-7s%n";
            System.out.printf(headerFormat, "Week day", "Lesson number", "Subject", "Group");
            System.out.println("-------------------------------------------------------------------");

            String rowFormat = "%-15s | %-15d | %-15s | %-7s%n";

            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONArray jsonObjects = new JSONArray(content);

            ArrayList<JSONObject> foundObjects = new ArrayList<>();
            for (int i = 0; i < jsonObjects.length(); i++) {
                JSONObject jsonObj = jsonObjects.optJSONObject(i);
                long jsonIdTeacher = jsonObj.getLong("id_teacher");
                String jsonWeekDay = jsonObj.getString("week_day");
                if (jsonIdTeacher == idTeacher && weekDay.equals(jsonWeekDay)) {
                    foundObjects.add(jsonObj);
                }
            }

            Collections.sort(foundObjects, new Comparator<JSONObject>() {
                public int compare(JSONObject o1, JSONObject o2) {
                    if (o1.getLong("lesson_number") > o2.getLong("lesson_number")) {
                        return 1;
                    }
                    return -1;
                }
            });

            String subjectContent = new String(Files.readAllBytes(Paths.get(SubjectMenu.getFilePath())));
            JSONArray subjectJSONObjects = new JSONArray(subjectContent);
            ArrayList<JSONObject> subjects = new ArrayList<>();
            for (int i = 0; i < subjectJSONObjects.length(); i++) {
                subjects.add((JSONObject) subjectJSONObjects.optJSONObject(i));
            }

            String groupContent = new String(Files.readAllBytes(Paths.get(GroupMenu.getFilePath())));
            JSONArray groupJSONObjects = new JSONArray(groupContent);
            ArrayList<JSONObject> groups = new ArrayList<>();
            for (int i = 0; i < groupJSONObjects.length(); i++) {
                groups.add((JSONObject) groupJSONObjects.optJSONObject(i));
            }

            for (JSONObject jsonObject : foundObjects) {
                String dayOfTheWeek = jsonObject.getString("week_day");

                int lessonNumber = jsonObject.getInt("lesson_number");

                JSONObject subject = subjects.stream()
                        .filter(j -> (j.getLong("id") == (jsonObject.getLong("id_subject"))))
                        .findFirst().orElse(new JSONObject());

                JSONObject group = groups.stream()
                        .filter(j -> (j.getLong("id") == (jsonObject.getLong("id_group"))))
                        .findFirst().orElse(new JSONObject());

                String subjectName = subject.getString("name");

                String groupName = group.getString("name");

                System.out.printf(rowFormat, dayOfTheWeek, lessonNumber, subjectName, groupName);
            }

            System.out.println("-------------------------------------------------------------------");
        } catch (IOException e) {
            System.out.println("Failed to read file: " + e.getMessage());
        }

        System.out.print("Press any button to return: ");
        scanner.nextLine();
    }
}
