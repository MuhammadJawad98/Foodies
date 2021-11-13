package com.devexpert.forfoodiesbyfoodies.models;

public class User {
    String firstName;
    String lastName;
    String email;
    String userId;
    boolean isUser;
    boolean isCritic;
    boolean isAdmin;

    public User(String firstName, String lastName, String email, String userId, boolean isUser, boolean isCritic, boolean isAdmin) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userId = userId;
        this.isUser = isUser;
        this.isCritic = isCritic;
        this.isAdmin = isAdmin;
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
