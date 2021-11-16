package com.devexpert.forfoodiesbyfoodies.interfaces;

import com.devexpert.forfoodiesbyfoodies.models.StreetFood;

import java.util.List;

public interface StreetFoodResult {
    void onComplete(List<StreetFood> streetFoods);
}
