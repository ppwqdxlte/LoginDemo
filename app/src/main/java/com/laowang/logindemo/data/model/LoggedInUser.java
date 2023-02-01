package com.laowang.logindemo.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String userId;
    private String displayName;

    private String password;
    private String level;
    private String date;

    public LoggedInUser(String userId, String displayName) {
        this.userId = userId;
        this.displayName = displayName;
    }

    public LoggedInUser(String userId, String displayName, String password, String level, String date) {
        this.userId = userId;
        this.displayName = displayName;
        this.password = password;
        this.level = level;
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPassword() {
        return password;
    }

    public String getLevel() {
        return level;
    }

    public String getDate() {
        return date;
    }
}