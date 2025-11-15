package SkillForge;


import java.io.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class JsonDatabase {
    private String fileName;

    public JsonDatabase(String fileName) {
        this.fileName = fileName;
        createFileIfNotExisted();
    }

    public void createFileIfNotExisted() {
        File file = new File(fileName);
        try {
            if (!file.exists()) {
                file.createNewFile();
                FileWriter fw = new FileWriter(fileName);
                fw.write("[]");
                fw.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JSONArray readJsonArrayFromFile() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            StringBuilder jsonToString = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                jsonToString.append(line);
            }

            br.close();
            if (jsonToString.length() == 0) {
                return new JSONArray();
            } else {
                return new JSONArray(jsonToString.toString());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new JSONArray();
        } catch (IOException e) {
            e.printStackTrace();
           return new JSONArray();
        }


    }

    public void writeJsonArrayToFile(JSONArray jsonArray) {
        try {
            BufferedWriter br = new BufferedWriter(new FileWriter(fileName));
            br.write(jsonArray.toString(4)); //4 means print jsonArray  with 4 spaces per level
            br.flush();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public abstract JSONArray loadAll();

    public abstract void saveObject(Object obj);


    public abstract Object getObjectById(String id);
}
