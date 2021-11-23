package com.devexpert.forfoodiesbyfoodies.models;

import com.google.type.DateTime;

public class Chat {
    String text;
    String timeStamp;
    String userId;
    String userName;

    public Chat() {
    }

    public Chat(String text, String timeStamp, String userId, String userName) {
        this.text = text;
        this.timeStamp = timeStamp;
        this.userId = userId;
        this.userName = userName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
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
