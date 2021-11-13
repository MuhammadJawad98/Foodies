package com.devexpert.forfoodiesbyfoodies.services;

import android.content.Context;
import android.util.Log;
import android.widget.Adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devexpert.forfoodiesbyfoodies.adapters.RecyclerViewAdapter;
import com.devexpert.forfoodiesbyfoodies.interfaces.FirebaseResultListener;
import com.devexpert.forfoodiesbyfoodies.models.Restaurant;
import com.devexpert.forfoodiesbyfoodies.models.User;
import com.devexpert.forfoodiesbyfoodies.utils.CommonFunctions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
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
                                Restaurant restaurant = new Restaurant(imageUrl, description);
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

    public static String getCurrentUserUUid() {
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        return currentFirebaseUser.getUid();
    }
}
