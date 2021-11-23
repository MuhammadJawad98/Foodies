package com.devexpert.forfoodiesbyfoodies.models;

import java.io.Serializable;

public class User implements Serializable {
    private String firstName;
    private String lastName;
    private String email;
    private String userId;
    private String password;
    private String imageUrl;
    private String documentId;
    private boolean isUser;
    private boolean isCritic;
    private boolean isAdmin;

    public User() {
    }


    public User(String firstName, String lastName, String email, String userId, String password, String imageUrl, boolean isUser, boolean isCritic, boolean isAdmin) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userId = userId;
        this.password = password;
        this.imageUrl = imageUrl;
        this.isUser = isUser;
        this.isCritic = isCritic;
        this.isAdmin = isAdmin;
    }

    public User(String firstName, String lastName, String email, String userId, String imageUrl, boolean isUser, boolean isCritic, boolean isAdmin) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userId = userId;
        this.imageUrl = imageUrl;
        this.isUser = isUser;
        this.isCritic = isCritic;
        this.isAdmin = isAdmin;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isUser() {
        return isUser;
    }

    public void setUser(boolean user) {
        isUser = user;
    }

    public boolean isCritic() {
        return isCritic;
    }

    public void setCritic(boolean critic) {
        isCritic = critic;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
