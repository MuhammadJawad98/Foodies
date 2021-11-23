package com.devexpert.forfoodiesbyfoodies.models;

public class Chat {
    String text;
    String timestamp;
    String userId;
    String userName;

    public Chat() {
    }

    public Chat(String text, String timestamp, String userId, String userName) {
        this.text = text;
        this.timestamp = timestamp;
        this.userId = userId;
        this.userName = userName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
