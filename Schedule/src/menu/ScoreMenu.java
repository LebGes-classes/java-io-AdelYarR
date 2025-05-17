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

public class ScoreMenu {

    private static final String filePath = "src/jsons/score.json";
    private static final Scanner scanner = new Scanner(System.in);

    public static String getFilePath() {
        return filePath;
    }

    public static void startMenu() {
        Scanner scanner = new Scanner(System.in);

        boolean running = true;
        while (running) {
            ConsoleCleaner.clearConsole();
            System.out.println("Score manager\n" +
                    "1 - Put score\n" +
                    "2 - Change score\n" +
                    "q - Quit menu\n"
            );

            String choice = scanner.nextLine();
            switch(choice) {
                case "1":
                    putScore();
                    scanner.nextLine();
                    break;
                case "2":
                    changeScore();
                    scanner.nextLine();
                    break;
                case "q":
                    running = false;
                    break;
            }
        }
    }

    public static void putScore() {
        ConsoleCleaner.clearConsole();
        try {
            long id = (long) (Math.random() * 100000);

            System.out.println("Enter student ID: ");
            long studentId = scanner.nextLong();
            scanner.nextLine();

            boolean isFound = JSONSearch.searchInJSON(StudentMenu.getFilePath(), studentId, "id");
            if (!isFound) {
                System.out.println("Couldn't find student with the ID: " + studentId + ".\nPress any key to return: ");
                return;
            }

            System.out.println("Enter subject ID: ");
            long subjectId = scanner.nextLong();
            scanner.nextLine();

            isFound = JSONSearch.searchInJSON(SubjectMenu.getFilePath(), subjectId, "id");
            if (!isFound) {
                System.out.println("Couldn't find subject with the ID: " + subjectId + ".\nPress any key to return: ");
                return;
            }

            System.out.println("Enter value of score: ");
            int value = scanner.nextInt();
            scanner.nextLine();

            String scoreContent = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONArray scores = new JSONArray(scoreContent);
            JSONObject newJsonObj = new JSONObject();

            newJsonObj.put("id", Long.toString(id));
            newJsonObj.put("id_student", Long.toString(studentId));
            newJsonObj.put("id_subject", Long.toString(subjectId));
            newJsonObj.put("value", Integer.toString(value));

            scores.put(newJsonObj);

            ExcelJSONParser.addToJSON(filePath, scores);

            System.out.print("Score with id: " + id + " was successful added. Press any button to return: ");
        } catch (IOException e) {
            System.out.println("Failed to read file: " + e.getMessage());
        }
    }

    public static void changeScore() {
        ConsoleCleaner.clearConsole();
        try {
            System.out.println("Enter score ID: ");
            long scoreId = scanner.nextLong();
            scanner.nextLine();

            String scoreContent = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONArray scores = new JSONArray(scoreContent);

            JSONObject currScore = new JSONObject();
            boolean isFound = false;
            for (int i = 0; i < scores.length() && !isFound; i++) {
                JSONObject jsonObj = scores.optJSONObject(i);
                long jsonId = jsonObj.getLong("id");
                if (scoreId == jsonId) {
                    currScore = jsonObj;
                    isFound = true;
                }
            }

            if (!isFound) {
                System.out.println("Couldn't find score with the ID: " + scoreId + ".\nPress any key to return: ");
                return;
            }

            System.out.println("Enter value: ");
            int value = scanner.nextInt();
            scanner.nextLine();

            currScore.put("value", Integer.toString(value));

            ExcelJSONParser.addToJSON(filePath, scores);

            System.out.print("Score with id: " + scoreId + " was successful changed. Press any button to return: ");
        } catch (IOException e) {
            System.out.println("Failed to read file: " + e.getMessage());
        }
    }
}
