package SkillForge;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class User {
    public abstract  JSONObject toJson(); //from Object to JSONobject
    public  abstract  Object fromJson(JSONObject obj); //from JSONobject to object
}
