package com.devexpert.forfoodiesbyfoodies.interfaces;

import com.devexpert.forfoodiesbyfoodies.models.Review;
import java.util.List;

public interface RestaurantReviewResult {
    void onComplete(List<Review> reviewList);

}
