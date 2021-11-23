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
import com.devexpert.forfoodiesbyfoodies.models.Channels;
import com.devexpert.forfoodiesbyfoodies.models.Chat;
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
import com.google.firebase.firestore.DocumentReference;
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
                        try {
                            Log.d("FireStore Data: ", documentSnapshots.getDocuments().size() + "");
                            for (DocumentChange documentChange : documentSnapshots.getDocumentChanges()) {
                                System.out.println("?????????" + documentChange.getType().name());
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
                        } catch (Exception e) {
                            System.out.println("error" + e.toString());
                        }
                    }
                }).addOnFailureListener(e -> {
            callback.onComplete(restaurantList);
            CommonFunctions.showToast("Error while fetching data.", context);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void getReviews(String rootCollection, String documentId, RestaurantReviewResult restaurantReviewResult) {
        db.collection(rootCollection).document(documentId).collection("reviews").addSnapshotListener(
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
                                double reviewRating = Double.parseDouble(documentSnapshot.getData().get("reviewRating").toString());
                                Review review = new Review(reviewUserName, reviewId, reviewUserId, reviewComment, profileUrl, rating, reviewRating);
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

    public static void addRating(String restaurantId, String reviewId, float rating, Context context) {
        YourPreference yourPreference = YourPreference.getInstance(context);
        String userId = yourPreference.getData("userId");
        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("rating", rating);
        db.collection("restaurants").document(restaurantId).collection("reviews").document(reviewId).collection("rating").document().set(data).addOnSuccessListener(aVoid -> Log.d("TAG", "DocumentSnapshot successfully written!")).addOnFailureListener(e -> Log.w("TAG", "Error writing document", e));
        rateReview(restaurantId, reviewId, rating);
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

    public static void rateReview(String restaurantDocId, String reviewDocId, float rate) {
        try {
            DocumentReference snapshot =
                    db.collection("restaurants").document(restaurantDocId).collection("reviews").document(reviewDocId);
            snapshot.get().addOnCompleteListener(task -> {
                double rating = Double.parseDouble(task.getResult().get("reviewRating").toString());
                double total_rating = (rating + rate) / 2;
                Map<String, Object> data = new HashMap<>();
                data.put("reviewRating", total_rating);
                snapshot.update(data);

            });
        } catch (Exception e) {
            System.out.println("error ratereview:::" + e.toString());
        }
    }

    public static void addRestaurant(String restaurantImageUrl, String restaurantDescription, String restaurantName) {
        DocumentReference snapshot = db.collection("restaurants").document();
        String id = snapshot.getId();
        Restaurant restaurant = new Restaurant(restaurantImageUrl, restaurantDescription, restaurantName, id);
        snapshot.set(restaurant).addOnSuccessListener(aVoid -> Log.d("Restaurant: ", "Successfully added restaurant")).
                addOnFailureListener(e -> Log.d("Restaurant: ", "Fail to added restaurant"));
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
                addOnSuccessListener(aVoid -> {
                    Log.d("Add review to res ", "Successfully added");
                }).addOnFailureListener(e -> {
            Log.d("Add review to res ", "Fail to added");

        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void getChannelChat(String docId, FirebaseResultListener resultListener) {
        db.collection("channels").document(docId).collection("messages").addSnapshotListener((value, error) -> {
            System.out.println("channels doc messages" + value.getDocuments().size());
            List list = new ArrayList();
            value.getDocuments().forEach(documentSnapshot -> {

                Chat chat = new Chat();
                chat.setText(documentSnapshot.get("text").toString());
                chat.setTimestamp(documentSnapshot.get("timestamp").toString());
                chat.setUserId(documentSnapshot.get("userId").toString());
                chat.setUserName(documentSnapshot.get("userName").toString());
                list.add(chat);
            });
            resultListener.onComplete(list);
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void getChannels(FirebaseResultListener listener) {
        List<Channels> channelsList = new ArrayList<>();
        db.collection("channels").addSnapshotListener((value, error) -> {
            value.getDocuments().forEach(documentSnapshot -> {
                Channels channel = new Channels();
                channel.setTopic(documentSnapshot.get("topic").toString());
                channel.setId(documentSnapshot.getId());
                channelsList.add(channel);
            });
            listener.onComplete(channelsList);
        });
    }

    public static void sendMessage(String docId, Chat chat, OnResult onResult) {
        db.collection("channels").document(docId).collection("messages").add(chat).addOnSuccessListener(documentReference -> {
            Log.d("Message Creation: ", "Successfully added message");
            onResult.onComplete();
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Message Creation: ", "message sending fail");
                onResult.onFailure();

            }
        });


    }
}

