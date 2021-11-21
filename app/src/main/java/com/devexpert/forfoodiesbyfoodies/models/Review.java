package com.devexpert.forfoodiesbyfoodies.models;

import java.io.Serializable;
import java.util.List;

public class Review implements Serializable {
    private String name;
    private String lastName;
    private String id;
    private String userId;
    private String comment;
    private String profileUrl;
    private String email;
    private double rating;
    private List reviewRatingList;

    public Review(String name, String id, String userId, String comment, String profileUrl, double rating, List reviewRatingList) {
        this.name = name;
        this.id = id;
        this.userId = userId;
        this.comment = comment;
        this.profileUrl = profileUrl;
        this.rating = rating;
        this.reviewRatingList = reviewRatingList;
    }

//    public Review(String name, String lastName, String id, String userId, String comment, String profileUrl, String email, double rating, List reviewRatingList) {
//        this.name = name;
//        this.lastName = lastName;
//        this.id = id;
//        this.userId = userId;
//        this.comment = comment;
//        this.profileUrl = profileUrl;
//        this.email = email;
//        this.rating = rating;
//        this.reviewRatingList = reviewRatingList;
//    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
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

    public List getReviewRatingList() {
        return reviewRatingList;
    }

    public void setReviewRatingList(List reviewRatingList) {
        this.reviewRatingList = reviewRatingList;
    }
}
