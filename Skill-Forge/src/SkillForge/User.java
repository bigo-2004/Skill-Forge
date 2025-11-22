package SkillForge;

import org.json.JSONArray;
import org.json.JSONObject;

public  class User {
    private String userId;
    private String userName;
    private String email;
    private String password;
    private String role;

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public User(String userId, String userName, String email, String password, String role) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.role = role;

    }

    public User(String userId, String userName, String email) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public   JSONObject toJson() {  //from Object to JSONobject
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", this.userId);
        jsonObject.put("username", this.userName);
        jsonObject.put("email", this.email);
        jsonObject.put("passwordHash", this.password);
        jsonObject.put("role", this.role);
        return jsonObject;
    }

    public static User fromJson(JSONObject obj) {  //from JSONobject to object
        return new User(obj.getString("id"), obj.getString("username"), obj.getString("email"), obj.getString("passwordHash"),obj.getString("role"));
    }






}
