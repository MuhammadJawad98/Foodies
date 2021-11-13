package com.devexpert.forfoodiesbyfoodies.interfaces;

import com.devexpert.forfoodiesbyfoodies.models.Restaurant;

import java.util.List;

public interface FirebaseResultListener {
    void onComplete(List<Restaurant> activityModels);
}