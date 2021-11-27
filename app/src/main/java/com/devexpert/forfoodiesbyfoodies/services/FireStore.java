package com.devexpert.forfoodiesbyfoodies.services;

import android.content.Context;
import android.util.Log;

import com.devexpert.forfoodiesbyfoodies.interfaces.FirebaseUserDataResult;
import com.devexpert.forfoodiesbyfoodies.interfaces.OnResult;
import com.devexpert.forfoodiesbyfoodies.models.Channels;
import com.devexpert.forfoodiesbyfoodies.models.Chat;
import com.devexpert.forfoodiesbyfoodies.models.Restaurant;
import com.devexpert.forfoodiesbyfoodies.models.StreetFood;
import com.devexpert.forfoodiesbyfoodies.models.User;
import com.devexpert.forfoodiesbyfoodies.utils.CommonFunctions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class FireStore {
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static void addUserToFireStore(User user) {
        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(documentReference -> CommonFunctions.customLog("Document added successfully" + documentReference.getId()))
                .addOnFailureListener(e ->
                        CommonFunctions.customLog("Error adding document" + e));
    }


    public static String getCurrentUserUUid() {
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        return currentFirebaseUser.getUid();
    }

    public static void getData(String userId, FirebaseUserDataResult resultListener) {
        db.collection("users")
                .whereEqualTo("userId", userId)
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

    public static void addRating(String rootCollection, String restaurantId, String reviewId, float rating, Context context) {
        YourPreference yourPreference = YourPreference.getInstance(context);
        String userId = yourPreference.getData("userId");
        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("rating", rating);
        db.collection(rootCollection).document(restaurantId).collection("reviews").document(reviewId).collection("rating").document().set(data).addOnSuccessListener(aVoid -> Log.d("TAG", "DocumentSnapshot successfully written!")).addOnFailureListener(e -> Log.w("TAG", "Error writing document", e));
        rateReview(rootCollection, restaurantId, reviewId, rating);
    }


    public static void addStreetFoodStall(StreetFood streetFood, OnResult onResult) {
        db.collection("street_food").add(streetFood).addOnSuccessListener(documentReference ->
                onResult.onComplete()).
                addOnFailureListener(e -> onResult.onFailure());
    }

    public static void updateUserData(String documentId, User user, OnResult onResult) {
        db.collection("users").
                document(documentId).
                set(user).
                addOnSuccessListener(aVoid -> onResult.onComplete()).
                addOnFailureListener(e -> onResult.onFailure());
    }

    public static void rateReview(String rootCollection, String restaurantDocId, String reviewDocId, float rate) {
        try {
            DocumentReference snapshot =
                    db.collection(rootCollection).document(restaurantDocId).
                            collection("reviews").document(reviewDocId);
            snapshot.get().addOnCompleteListener(task -> {
                String reviewRating = task.getResult().get("reviewRating").toString();
                Map<String, Object> data = new HashMap<>();

                if (reviewRating != null) {
                    double rating = Double.parseDouble(reviewRating);
                    double total_rating = (rating + rate) / 2;
                    data.put("reviewRating", total_rating);
                } else {
                    data.put("reviewRating", rate);
                }

                snapshot.update(data);

            });
        } catch (Exception e) {
            CommonFunctions.customLog("Error rate review: " + e.toString());
        }
    }

    public static void addRestaurant(String restaurantImageUrl, String restaurantDescription, String restaurantName) {
        DocumentReference snapshot = db.collection("restaurants").document();
        String id = snapshot.getId();
        Restaurant restaurant = new Restaurant(restaurantImageUrl, restaurantDescription, restaurantName, id);
        snapshot.set(restaurant).addOnSuccessListener(aVoid ->
                CommonFunctions.customLog("Restaurant: Successfully added restaurant")).
                addOnFailureListener(e ->
                        CommonFunctions.customLog("Restaurant: Fail to added restaurant"));
    }

    public static void addRestaurantReview(String rootCollection, String documentPath, String comment, String id, String name, String profileUrl, float rating) {
        Map<String, Object> data = new HashMap<>();
        data.put("comment", comment);
        data.put("id", id);
        data.put("name", name);
        data.put("profileUrl", profileUrl);
        data.put("rating", rating);
        data.put("reviewRating", 0.0);

        db.collection(rootCollection).document(documentPath).collection("reviews").document().set(data).
                addOnSuccessListener(aVoid -> CommonFunctions.customLog("Add review to res Successfully added")).
                addOnFailureListener(e -> CommonFunctions.customLog("Add review to res Fail to added"));
    }


    public static void sendMessage(String docId, Chat chat) {
        db.collection("channels").document(docId).collection("messages").add(chat).
                addOnSuccessListener(documentReference ->
                        CommonFunctions.customLog("Message Creation: Successfully added message")
                ).addOnFailureListener(e ->
                CommonFunctions.customLog("Message Creation: message sending fail"));
    }

    public static void createNewTopic(String topic) {
        String id = db.collection("channels").document().getId();
        Channels channel = new Channels(id, topic);
        db.collection("channels").document(id).set(channel).addOnSuccessListener(aVoid -> CommonFunctions.customLog("Channel: Successfully added")).addOnFailureListener(e -> CommonFunctions.customLog("Channel: Fail to added"));

    }
}

