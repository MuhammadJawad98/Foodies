package com.devexpert.forfoodiesbyfoodies.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.devexpert.forfoodiesbyfoodies.R;
import com.devexpert.forfoodiesbyfoodies.adapters.ChatAdapter;
import com.devexpert.forfoodiesbyfoodies.adapters.ReviewRecyclerviewAdapter;
import com.devexpert.forfoodiesbyfoodies.models.Restaurant;
import com.devexpert.forfoodiesbyfoodies.models.Review;
import com.devexpert.forfoodiesbyfoodies.models.User;
import com.devexpert.forfoodiesbyfoodies.services.FireStore;
import com.devexpert.forfoodiesbyfoodies.services.YourPreference;
import com.devexpert.forfoodiesbyfoodies.utils.Constants;
import com.devexpert.forfoodiesbyfoodies.utils.CustomDialogClass;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ReviewsActivity extends AppCompatActivity implements ReviewRecyclerviewAdapter.ItemClickListener {
    ReviewRecyclerviewAdapter adapter;
    private TextView ratingTv;
    private TextView ratingPeoplesTv;
    private RatingBar ratingBar;
    private Restaurant restaurant;
    RecyclerView recyclerView;
    List<Review> reviewList = new ArrayList();
    private String from;
    String rootCollection;
    private User user;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        initView();


        Intent intent = getIntent();
        restaurant = (Restaurant) intent.getSerializableExtra("details");
        from = intent.getExtras().getString("from");
        user = (User) intent.getSerializableExtra("user");

        if (from.equals(Constants.restaurantDetailActivity)) {
            rootCollection = Constants.rootCollectionRestaurant;
        } else {
            rootCollection = Constants.rootCollectionStreetFood;

        }
        adapter = new ReviewRecyclerviewAdapter(getApplicationContext(), reviewList, from, restaurant.getId(), user.isAdmin());
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(this);
        listenNewReview(rootCollection, restaurant.getId());


    }

    @Override
    public void onItemClick(View view, int position) {
        CustomDialogClass cdd = new CustomDialogClass(this, reviewList.get(position).getId(), restaurant.getId(), rootCollection);
        cdd.show();
    }


    void initView() {
        ratingTv = findViewById(R.id.ratingTv_id);
        ratingPeoplesTv = findViewById(R.id.ratingPeopleTv_id);
        ratingBar = findViewById(R.id.ratingBar);
        recyclerView = findViewById(R.id.reviewRecyclerview_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

    }

    private void listenNewReview(String rootCollection, String documentId) {
        FireStore.db.collection(rootCollection).document(documentId).collection("reviews").addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null) {
            return;
        }
        if (value != null) {
            String type = "";

            int count = reviewList.size();
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    reviewList.add(getReviewObject(documentChange));
                    type = "ADDED";
                }
                if (documentChange.getType() == DocumentChange.Type.MODIFIED) {

                    try {

                        String docID = documentChange.getDocument().getId();
                        int index = getItemIndex(docID);
                        reviewList.set(index, getReviewObject(documentChange));
                        adapter.notifyItemChanged(index);
                    } catch (Exception e) {
                        System.out.println("<<<<<<<<<<<<<<<<<" + e.getMessage());
                    }
                    type = "MODIFIED";
                }
                if (documentChange.getType() == DocumentChange.Type.REMOVED) {
                    // remove

                    try {

                        String docID = documentChange.getDocument().getId();
                        System.out.println(">>>>> deleted item" + getItemIndex(docID));
                        int index = getItemIndex(docID);
                        reviewList.remove(index);
                        adapter.notifyItemRemoved(index);
                        type = "REMOVED";
                    } catch (Exception e) {
                        System.out.println("<<<<<<<<<<<<<<<<<" + e.getMessage());
                    }
                }
            }
            updateRating();
            if (!type.equals("REMOVED")) {
                if (count == 0) {
                    adapter.notifyDataSetChanged();
                } else {
                    adapter.notifyItemRangeInserted(reviewList.size(), reviewList.size());
                }
            }

        }

    };

    public void updateRating() {
        float rating = 0;
        if (reviewList.size() != 0) {
            for (int i = 0; i < reviewList.size(); i++) {
                rating = (float) (rating + reviewList.get(i).getRating());
            }
            rating = rating / reviewList.size();
        }
        ratingTv.setText(String.format("%.1f", rating) + "");
        ratingPeoplesTv.setText("From " + reviewList.size() + " people");
        ratingBar.setRating(rating);
    }

    private Review getReviewObject(DocumentChange documentChange) {
        String reviewUserName = documentChange.getDocument().getData().get("name").toString();
        String reviewUserId = documentChange.getDocument().getData().get("id").toString();
        String reviewId = documentChange.getDocument().getId();
        String reviewComment = documentChange.getDocument().getData().get("comment").toString();
        String profileUrl = documentChange.getDocument().getData().get("profileUrl").toString();
        double rating = Double.parseDouble(documentChange.getDocument().getData().get("rating").toString());
        double reviewRating = Double.parseDouble(documentChange.getDocument().getData().get("reviewRating").toString());
        return new Review(reviewUserName, reviewId, reviewUserId, reviewComment, profileUrl, rating, reviewRating);
    }

    private int getItemIndex(String docID) {
        int index = -1;
        for (int i = 0; i < reviewList.size(); i++) {
            if (reviewList.get(i).getId().equals(docID)) {
                index = i;
                break;
            }
        }
        return index;
    }
}