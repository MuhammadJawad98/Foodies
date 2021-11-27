package com.devexpert.forfoodiesbyfoodies.models;


import java.util.Date;

public class Chat {
    String text;
    Date timestamp;
    String userId;
    String userName;
    String messageId;

    public Chat() {
    }

    public Chat(String text, Date timestamp, String userId, String userName) {
        this.text = text;
        this.timestamp = timestamp;
        this.userId = userId;
        this.userName = userName;
    }


    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
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
