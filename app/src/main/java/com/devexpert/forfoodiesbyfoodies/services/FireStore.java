package com.devexpert.forfoodiesbyfoodies.services;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.devexpert.forfoodiesbyfoodies.adapters.RecyclerViewAdapter;
import com.devexpert.forfoodiesbyfoodies.interfaces.FirebaseResultListener;
import com.devexpert.forfoodiesbyfoodies.interfaces.FirebaseUserDataResult;
import com.devexpert.forfoodiesbyfoodies.interfaces.RestaurantReviewResult;
import com.devexpert.forfoodiesbyfoodies.models.Restaurant;
import com.devexpert.forfoodiesbyfoodies.models.Review;
import com.devexpert.forfoodiesbyfoodies.models.User;
import com.devexpert.forfoodiesbyfoodies.utils.CommonFunctions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FireStore {
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static void addUserToFireStore(User user) {
        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TAG", "Data save to db: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error adding document", e);
                    }
                });
    }

    public static void getRestaurantFromFirebase(Context context, FirebaseResultListener callback) {
        List<Restaurant> restaurantList = new ArrayList<>();
        db.collection("restaurants").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        if (documentSnapshots.isEmpty()) {
                            Log.d("FireStore Data: ", "onSuccess: LIST EMPTY");
                        } else {
                            Log.d("FireStore Data: ", documentSnapshots.getDocuments().size() + "");
                            for (DocumentChange documentChange : documentSnapshots.getDocumentChanges()) {
                                // Convert the whole Query Snapshot to a list
                                // of objects directly! No need to fetch each
                                // document.
                                String imageUrl = documentChange.getDocument().getData().get("restaurantImageUrl").toString();
                                String description = documentChange.getDocument().getData().get("restaurantDescription").toString();
                                String name = documentChange.getDocument().getData().get("restaurantName").toString();
                                String id = documentChange.getDocument().getData().get("id").toString();
                                List<Review> reviewsList = new ArrayList<>();

                                Log.d("Restaurants details: ", documentChange.getDocument().getId());
                                Restaurant restaurant = new Restaurant(imageUrl, description, name, id);
                                // Add all to your list
                                restaurantList.add(restaurant);
                            }
                            callback.onComplete(restaurantList);
                            Log.d("FireStore Data:", "onSuccess: " + restaurantList.toString());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onComplete(restaurantList);
                CommonFunctions.showToast("Error while fetching data.", context);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void getReviews(String documentId, RestaurantReviewResult restaurantReviewResult) {
        System.out.println(">>>>>> id:" + db.collection("restaurants").document(documentId).getId());
        Query query = db.collection("restaurants").document(documentId).collection("reviews");
        ListenerRegistration registration = query.addSnapshotListener(
                (value, error) -> {
                    if (value != null) {
                        List<Review> reviewList = new ArrayList<>();

                        Log.d(">>>>>>>>>>>>>>", +value.getDocuments().size() + "");
                        value.getDocuments().forEach(documentSnapshot -> {
                            try {
                                String reviewUserName = documentSnapshot.getData().get("name").toString();
                                String reviewUserId = documentSnapshot.getData().get("id").toString();
                                String reviewId = documentSnapshot.getId();
                                String reviewComment = documentSnapshot.getData().get("comment").toString();
                                double reviewRating = Double.parseDouble(documentSnapshot.getData().get("rating").toString());
                                Review review = new Review(reviewUserName, reviewId, reviewUserId, reviewComment, reviewRating);
                                reviewList.add(review);
                            } catch (Exception e) {
                                Log.d(">>>>>Error>>>>>>>>>", e.getMessage());
                            }
                        });
                        restaurantReviewResult.onComplete(reviewList);
                    } else {
                        Log.d(">>>>>>>>>>>>>>", "error");

                    }
                });

    }

    public static String getCurrentUserUUid() {
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        return currentFirebaseUser.getUid();
    }

    public static void getData(FirebaseUserDataResult resultListener) {
        final String current = FirebaseAuth.getInstance().getCurrentUser().getUid();//getting unique user id
        String[] data = new String[7];
        db.collection("users")
                .whereEqualTo("userId", current)//looks for the corresponding value with the field in the database
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    User user = new User();

                    Log.d("#########", document.get("firstName").toString());
                    user.setFirstName(document.get("firstName").toString());
                    user.setLastName(document.get("lastName").toString());
                    user.setEmail(document.get("email").toString());
                    user.setUserId(document.get("userId").toString());
                    user.setUser(Boolean.parseBoolean(document.get("user").toString()));
                    user.setCritic(Boolean.parseBoolean(document.get("critic").toString()));
                    user.setAdmin(Boolean.parseBoolean(document.get("admin").toString()));
                    resultListener.onComplete(user);

                }
            }
        });

    }

    public static void addRating() {

    }
}
