package parser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;
import org.json.JSONArray;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelJSONParser{
    public static void parse(String filePath, String[] sheetNames) throws Exception {
        FileInputStream fis = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(fis);

        for (String sheetName : sheetNames) {
            Sheet sheet = workbook.getSheet(sheetName);

            Iterator<Row> rowIterator = sheet.iterator();

            boolean isStart = true;

            ArrayList<String> columnValues = new ArrayList<>();
            JSONArray jsonObjects = new JSONArray();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();

                if (isStart) {
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        DataFormatter dataFormatter = new DataFormatter();
                        String cellValue = dataFormatter.formatCellValue(cell);

                        columnValues.add(cellValue);
                    }
                    isStart = false;
                } else {
                    int columnValuesIndex = 0;
                    Map<String, Object> orderedMap = new LinkedHashMap<>();
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        DataFormatter dataFormatter = new DataFormatter();
                        String cellValue = dataFormatter.formatCellValue(cell);

                        orderedMap.put(columnValues.get(columnValuesIndex), cellValue);
                        columnValuesIndex++;
                    }
                    jsonObjects.put(new JSONObject(orderedMap));
                }
            }

            try {
                String jsonFilePath = "src/jsons/" + sheetName + ".json";
                addToJSON(jsonFilePath, jsonObjects);
            } catch (IOException e) {
                System.out.println("Failed to parse files: " + e.getMessage());
            }
        }

        fis.close();
    }

    public static void addToJSON(String filePath, JSONArray jsonObjects) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            String jsonString = jsonObjects.toString(2);
            byte[] jsonBytes = jsonString.getBytes(StandardCharsets.UTF_8);

            fos.write(jsonBytes);
        }
    }
}
