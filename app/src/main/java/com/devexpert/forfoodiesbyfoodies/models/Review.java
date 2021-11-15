package com.devexpert.forfoodiesbyfoodies.models;

import java.io.Serializable;

public class Review implements Serializable {
    private String name;
    private String id;
    private String userId;
    private String comment;
    private double rating;

    public Review(String name, String id, String userId, String comment) {
        this.name = name;
        this.id = id;
        this.userId = userId;
        this.comment = comment;
    }

    public Review(String name, String id, String userId, String comment, double rating) {
        this.name = name;
        this.id = id;
        this.userId = userId;
        this.comment = comment;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
