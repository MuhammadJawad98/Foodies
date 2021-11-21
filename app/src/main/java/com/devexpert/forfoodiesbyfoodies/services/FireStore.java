package com.devexpert.forfoodiesbyfoodies.services;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.devexpert.forfoodiesbyfoodies.interfaces.FirebaseResultListener;
import com.devexpert.forfoodiesbyfoodies.interfaces.FirebaseUserDataResult;
import com.devexpert.forfoodiesbyfoodies.interfaces.OnResult;
import com.devexpert.forfoodiesbyfoodies.interfaces.RestaurantReviewResult;
import com.devexpert.forfoodiesbyfoodies.interfaces.StreetFoodResult;
import com.devexpert.forfoodiesbyfoodies.models.Restaurant;
import com.devexpert.forfoodiesbyfoodies.models.Review;
import com.devexpert.forfoodiesbyfoodies.models.StreetFood;
import com.devexpert.forfoodiesbyfoodies.models.User;
import com.devexpert.forfoodiesbyfoodies.utils.CommonFunctions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FireStore {
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static void addUserToFireStore(User user) {
        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(documentReference -> Log.d("TAG", "Data save to db: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w("TAG", "Error adding document", e));
    }

    public static void getRestaurantFromFirebase(Context context, FirebaseResultListener callback) {
        List<Restaurant> restaurantList = new ArrayList<>();
        db.collection("restaurants").get()
                .addOnSuccessListener(documentSnapshots -> {
                    if (documentSnapshots.isEmpty()) {
                        Log.d("FireStore Data: ", "onSuccess: LIST EMPTY");
                    } else {
                        Log.d("FireStore Data: ", documentSnapshots.getDocuments().size() + "");
                        for (DocumentChange documentChange : documentSnapshots.getDocumentChanges()) {
                            String imageUrl = documentChange.getDocument().getData().get("restaurantImageUrl").toString();
                            String description = documentChange.getDocument().getData().get("restaurantDescription").toString();
                            String name = documentChange.getDocument().getData().get("restaurantName").toString();
                            String id = documentChange.getDocument().getData().get("id").toString();

                            Log.d("Restaurants details: ", documentChange.getDocument().getId());
                            Restaurant restaurant = new Restaurant(imageUrl, description, name, id);
                            restaurantList.add(restaurant);
                        }
                        callback.onComplete(restaurantList);
                        Log.d("FireStore Data:", "onSuccess: " + restaurantList.toString());
                    }
                }).addOnFailureListener(e -> {
            callback.onComplete(restaurantList);
            CommonFunctions.showToast("Error while fetching data.", context);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void getReviews(String documentId, RestaurantReviewResult restaurantReviewResult) {
        System.out.println("????????????????????documentId: " + documentId);
        db.collection("restaurants").document(documentId).collection("reviews").addSnapshotListener(
                (value, error) -> {
                    if (value != null) {
                        List<Review> reviewList = new ArrayList<>();

                        value.getDocuments().forEach(documentSnapshot -> {
                            try {
                                String reviewUserName = documentSnapshot.getData().get("name").toString();
                                String reviewUserId = documentSnapshot.getData().get("id").toString();
                                String reviewId = documentSnapshot.getId();
                                String reviewComment = documentSnapshot.getData().get("comment").toString();
                                String profileUrl = documentSnapshot.getData().get("profileUrl").toString();
                                double rating = Double.parseDouble(documentSnapshot.getData().get("rating").toString());
                                List ratingList = new ArrayList<>();
                                try {
                                    documentSnapshot.getReference().collection("rating").addSnapshotListener((value1, error1) -> {
                                        value1.getDocuments().forEach(documentSnapshot1 -> {
                                            ratingList.add(documentSnapshot1.get("rating"));
                                        });
                                    });
                                } catch (Exception e) {
                                    System.out.println("Error ::::::::" + e.getMessage());
                                }
                                Review review = new Review(reviewUserName, reviewId, reviewUserId, reviewComment, profileUrl, rating, ratingList);
                                reviewList.add(review);
                            } catch (Exception e) {
                                Log.d("Error:::", e.getMessage());
                            }
                        });
                        restaurantReviewResult.onComplete(reviewList);
                    } else {
                        Log.d("Error:::", "Something went wrong");
                    }
                });

    }

    public static String getCurrentUserUUid() {
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        return currentFirebaseUser.getUid();
    }

    public static void getData(String userId,FirebaseUserDataResult resultListener) {
//        final String current = FirebaseAuth.getInstance().getCurrentUser().getUid();//getting unique user id
        db.collection("users")
                .whereEqualTo("userId", userId)//looks for the corresponding value with the field in the database
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    User user = new User();
                    user.setFirstName(document.get("firstName").toString());
                    user.setLastName(document.get("lastName").toString());
                    user.setEmail(document.get("email").toString());
                    user.setUserId(document.get("userId").toString());
                    user.setPassword(document.get("password").toString());
                    user.setImageUrl(document.get("imageUrl").toString());
                    user.setUser(Boolean.parseBoolean(document.get("user").toString()));
                    user.setCritic(Boolean.parseBoolean(document.get("critic").toString()));
                    user.setAdmin(Boolean.parseBoolean(document.get("admin").toString()));
                    user.setDocumentId(document.getId());

                    resultListener.onComplete(user);

                }
            }
        });

    }

    public static void addRating(String restaurantId, String reviewId, float rating) {
        String userId = getCurrentUserUUid();
        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("rating", rating);
        db.collection("restaurants").document(restaurantId).collection("reviews").document(reviewId).collection("rating").document().set(data).addOnSuccessListener(aVoid -> Log.d("TAG", "DocumentSnapshot successfully written!")).addOnFailureListener(e -> Log.w("TAG", "Error writing document", e));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void getStreetFoodData(StreetFoodResult result) {
        List<StreetFood> foodList = new ArrayList<>();
        db.collection("street_food").addSnapshotListener((value, error) -> {
            foodList.clear();
            value.getDocuments().forEach(documentSnapshot -> {
                StreetFood streetFood = new StreetFood();
                streetFood.setDescription(documentSnapshot.get("description").toString());
                streetFood.setLocation(documentSnapshot.get("location").toString());
                streetFood.setName(documentSnapshot.get("name").toString());
                streetFood.setPicture(documentSnapshot.get("picture").toString());
                streetFood.setType(documentSnapshot.get("type").toString());
                streetFood.setUserId(documentSnapshot.get("userId").toString());
                streetFood.setId(documentSnapshot.getId());
                foodList.add(streetFood);
            });
            result.onComplete(foodList);

        });
    }

    public static void addStreetFoodStall(StreetFood streetFood, OnResult onResult) {
        db.collection("street_food").add(streetFood).addOnSuccessListener(documentReference ->
        {
            onResult.onComplete();
        }).
                addOnFailureListener(e -> {
                    onResult.onFailure();
                });
    }

    public static void updateUserData(String documentId, User user, OnResult onResult) {
        db.collection("users").
                document(documentId).
                set(user).
                addOnSuccessListener(aVoid -> {
                    onResult.onComplete();
                }).
                addOnFailureListener(e -> {
                    onResult.onFailure();
                });
    }
}

