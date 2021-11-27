package com.devexpert.forfoodiesbyfoodies.services;

import android.content.Context;

import com.devexpert.forfoodiesbyfoodies.interfaces.FirebaseUserDataResult;
import com.devexpert.forfoodiesbyfoodies.interfaces.OnResult;
import com.devexpert.forfoodiesbyfoodies.models.Channels;
import com.devexpert.forfoodiesbyfoodies.models.Chat;
import com.devexpert.forfoodiesbyfoodies.models.Restaurant;
import com.devexpert.forfoodiesbyfoodies.models.StreetFood;
import com.devexpert.forfoodiesbyfoodies.models.User;
import com.devexpert.forfoodiesbyfoodies.utils.CommonFunctions;
import com.devexpert.forfoodiesbyfoodies.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class FireStore {
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();

    //Function for add data of user to firebase
    public static void addUserToFireStore(User user) {
        // Add a new document with a generated ID
        db.collection(Constants.rootCollectionUsers)
                .add(user)
                .addOnSuccessListener(documentReference ->
                        CommonFunctions.customLog("Document added successfully" + documentReference.getId()))
                .addOnFailureListener(e ->
                        CommonFunctions.customLog("Error adding document" + e));
    }


    //Function for get current user uuid
    public static String getCurrentUserUUid() {
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        return currentFirebaseUser.getUid();
    }

    //Function for get complete user data
    public static void getData(String userId, FirebaseUserDataResult resultListener) {
        db.collection(Constants.rootCollectionUsers)
                .whereEqualTo(Constants.userId, userId)
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    User user = new User();
                    user.setFirstName(document.get(Constants.firstName).toString());
                    user.setLastName(document.get(Constants.lastName).toString());
                    user.setEmail(document.get(Constants.email).toString());
                    user.setUserId(document.get(Constants.userId).toString());
                    user.setPassword(document.get(Constants.password).toString());
                    user.setImageUrl(document.get(Constants.imageUrl).toString());
                    user.setUser(Boolean.parseBoolean(document.get(Constants.user).toString()));
                    user.setCritic(Boolean.parseBoolean(document.get(Constants.critic).toString()));
                    user.setAdmin(Boolean.parseBoolean(document.get(Constants.admin).toString()));
                    user.setDocumentId(document.getId());

                    resultListener.onComplete(user);

                }
            }
        });

    }

    //Function for add rating of specific restaurant
    public static void addRating(String rootCollection, String restaurantId, String reviewId, float rating, Context context) {
        CustomSharedPreference sharedPreference = CustomSharedPreference.getInstance(context);
        String userId = sharedPreference.getData(Constants.userId);
        Map<String, Object> data = new HashMap<>();
        data.put(Constants.userId, userId);
        data.put(Constants.rating, rating);
        db.collection(rootCollection).document(restaurantId).collection(Constants.reviews).document(reviewId).collection(Constants.rating).
                document().set(data).addOnSuccessListener(aVoid -> CommonFunctions.customLog("DocumentSnapshot successfully written!")).
                addOnFailureListener(e -> CommonFunctions.customLog("Error writing document " + e));
        rateReview(rootCollection, restaurantId, reviewId, rating);
    }

    //Function for adding new street food stall
    public static void addStreetFoodStall(StreetFood streetFood, OnResult onResult) {
        db.collection(Constants.rootCollectionStreetFood).add(streetFood).addOnSuccessListener(documentReference ->
                onResult.onComplete()).
                addOnFailureListener(e -> onResult.onFailure());
    }

    //Function for update user data
    public static void updateUserData(String documentId, User user, OnResult onResult) {
        db.collection(Constants.rootCollectionUsers).
                document(documentId).
                set(user).
                addOnSuccessListener(aVoid -> onResult.onComplete()).
                addOnFailureListener(e -> onResult.onFailure());
    }

    //Function for update the rating
    public static void rateReview(String rootCollection, String restaurantDocId, String reviewDocId, float rate) {
        try {
            DocumentReference snapshot =
                    db.collection(rootCollection).document(restaurantDocId).
                            collection(Constants.reviews).document(reviewDocId);
            snapshot.get().addOnCompleteListener(task -> {
                String reviewRating = task.getResult().get(Constants.reviewRating).toString();
                Map<String, Object> data = new HashMap<>();

                if (reviewRating != null) {
                    double rating = Double.parseDouble(reviewRating);
                    double total_rating = (rating + rate) / 2;
                    data.put(Constants.reviewRating, total_rating);
                } else {
                    data.put(Constants.reviewRating, rate);
                }

                snapshot.update(data);

            });
        } catch (Exception e) {
            CommonFunctions.customLog("Error rate review: " + e.toString());
        }
    }

    //Function for add new restaurant
    public static void addRestaurant(String restaurantImageUrl, String restaurantDescription, String restaurantName) {
        DocumentReference snapshot = db.collection(Constants.rootCollectionRestaurant).document();
        String id = snapshot.getId();
        Restaurant restaurant = new Restaurant(restaurantImageUrl, restaurantDescription, restaurantName, id);
        snapshot.set(restaurant).addOnSuccessListener(aVoid ->
                CommonFunctions.customLog("Restaurant: Successfully added restaurant")).
                addOnFailureListener(e ->
                        CommonFunctions.customLog("Restaurant: Fail to added restaurant"));
    }

    //Function for add restaurant review
    public static void addRestaurantReview(String rootCollection, String documentPath, String comment, String id, String name, String profileUrl, float rating) {
        Map<String, Object> data = new HashMap<>();
        data.put(Constants.comment, comment);
        data.put(Constants.id, id);
        data.put(Constants.name, name);
        data.put(Constants.profileUrl, profileUrl);
        data.put(Constants.rating, rating);
        data.put(Constants.reviewRating, 0.0);

        db.collection(rootCollection).document(documentPath).collection(Constants.reviews).document().set(data).
                addOnSuccessListener(aVoid -> CommonFunctions.customLog("Add review to res Successfully added")).
                addOnFailureListener(e -> CommonFunctions.customLog("Add review to res Fail to added"));
    }

    //Function for send message
    public static void sendMessage(String docId, Chat chat) {
        db.collection(Constants.rootCollectionChannels).document(docId).collection(Constants.messages).add(chat).
                addOnSuccessListener(documentReference ->
                        CommonFunctions.customLog("Message Creation: Successfully added message")
                ).addOnFailureListener(e ->
                CommonFunctions.customLog("Message Creation: message sending fail"));
    }

    //Function for creating new chat topic
    public static void createNewTopic(String topic) {
        String id = db.collection(Constants.rootCollectionChannels).document().getId();
        Channels channel = new Channels(id, topic);
        db.collection(Constants.rootCollectionChannels).document(id).set(channel).addOnSuccessListener(aVoid -> CommonFunctions.customLog("Channel: Successfully added")).addOnFailureListener(e -> CommonFunctions.customLog("Channel: Fail to added"));

    }
}

