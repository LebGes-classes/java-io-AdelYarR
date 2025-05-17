package utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JSONSearch {

    public static boolean searchInJSON(String path, long id, String valueName) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(path)));
        JSONArray jsonObjects = new JSONArray(content);

        boolean isFound = false;
        for (int i = 0; i < jsonObjects.length() && !isFound; i++) {
            JSONObject jsonObj = jsonObjects.optJSONObject(i);
            long jsonId = jsonObj.getLong(valueName);
            if (id == jsonId) {
                isFound = true;
            }
        }

        return isFound;
    }
}
