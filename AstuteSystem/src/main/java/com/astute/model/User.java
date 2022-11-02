package com.astute.model;

public class User {
    int userId;
    String username;
    String password;
    String name;
    String sessionId;

    public User(int userId, String username, String password, String name, String sessionId) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.name = name;

    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
