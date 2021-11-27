package com.devexpert.forfoodiesbyfoodies.models;

import java.io.Serializable;
import java.util.List;

public class Restaurant implements Serializable {
    private String restaurantImageUrl;
    private String restaurantDescription;
    private String restaurantName;
    private List<Review> restaurantReview;
    private String id;

    public Restaurant(String restaurantImageUrl, String restaurantDescription, String restaurantName, String id) {
        this.restaurantImageUrl = restaurantImageUrl;
        this.restaurantDescription = restaurantDescription;
        this.restaurantName = restaurantName;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRestaurantImageUrl() {
        return restaurantImageUrl;
    }

    public void setRestaurantImageUrl(String restaurantImageUrl) {
        this.restaurantImageUrl = restaurantImageUrl;
    }

    public String getRestaurantDescription() {
        return restaurantDescription;
    }

    public void setRestaurantDescription(String restaurantDescription) {
        this.restaurantDescription = restaurantDescription;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public List<Review> getRestaurantReview() {
        return restaurantReview;
    }

    public void setRestaurantReview(List<Review> restaurantReview) {
        this.restaurantReview = restaurantReview;
    }
}
