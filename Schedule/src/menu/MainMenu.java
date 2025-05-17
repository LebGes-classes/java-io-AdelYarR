package menu;

import utils.ConsoleCleaner;

import java.util.Scanner;

public class MainMenu {

    public static void startMenu() {
        Scanner scanner = new Scanner(System.in);

        boolean running = true;
        while (running) {
            ConsoleCleaner.clearConsole();
            System.out.println("Class Schedule Application\n" +
                    "1 - Lesson\n" +
                    "2 - Teacher\n" +
                    "3 - Student\n" +
                    "4 - Group\n" +
                    "5 - Score\n" +
                    "6 - Subject\n" +
                    "q - Quit menu\n"
            );

            String choice = scanner.nextLine();
            switch(choice) {
                case "1":
                    LessonMenu.startMenu();
                    break;
                case "2":
                    TeacherMenu.startMenu();
                    break;
                case "3":
                    StudentMenu.startMenu();
                    break;
                case "4":
                    GroupMenu.startMenu();
                    break;
                case "5":
                    ScoreMenu.startMenu();
                    break;
                case "6":
                    SubjectMenu.startMenu();
                    break;
                case "q":
                    running = false;
                    break;
            }
        }
    }
}
