package com.devexpert.forfoodiesbyfoodies.models;

public class StreetFood {
    String description;
    String location;
    String name;
    String picture;
    String type;
    String userId;
    String id;

    public StreetFood() {
    }


    public StreetFood(String name, String description, String location, String picture, String type, String userId) {
        this.description = description;
        this.location = location;
        this.name = name;
        this.picture = picture;
        this.type = type;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
