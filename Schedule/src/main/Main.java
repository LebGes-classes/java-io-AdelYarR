package main;

import menu.MainMenu;
import parser.ExcelJSONParser;

public class Main {
    public static void main(String[] args) {
        String[] sheetNames = {"lesson", "teacher", "student", "group", "score", "subject"};

        try {
            ExcelJSONParser.parse("src/data/excelIO.xlsx", sheetNames);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        MainMenu.startMenu();
}