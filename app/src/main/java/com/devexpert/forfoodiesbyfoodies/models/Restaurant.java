package com.devexpert.forfoodiesbyfoodies.models;

import java.io.Serializable;

public class Restaurant implements Serializable {
    String restaurantImageUrl;
    String restaurantDescription;

    public Restaurant(String restaurantImageUrl, String restaurantDescription) {
        this.restaurantImageUrl = restaurantImageUrl;
        this.restaurantDescription = restaurantDescription;
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
}
