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
import com.devexpert.forfoodiesbyfoodies.adapters.ReviewRecyclerviewAdapter;
import com.devexpert.forfoodiesbyfoodies.models.Restaurant;
import com.devexpert.forfoodiesbyfoodies.models.Review;
import com.devexpert.forfoodiesbyfoodies.services.FireStore;
import com.devexpert.forfoodiesbyfoodies.utils.Constants;
import com.devexpert.forfoodiesbyfoodies.utils.CustomDialogClass;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ReviewsActivity extends AppCompatActivity implements ReviewRecyclerviewAdapter.ItemClickListener {
    ReviewRecyclerviewAdapter adapter;
    private TextView ratingTv;
    private TextView ratingPeoplesTv;
    private RatingBar ratingBar;
    private Restaurant restaurant;
    RecyclerView recyclerView;
    List<Review> reviewList = new ArrayList();
    private String from;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        initView();

        Intent intent = getIntent();
        restaurant = (Restaurant) intent.getSerializableExtra("details");
        from = intent.getExtras().getString("from");
        String rootCollection;
        if (from.equals(Constants.restaurantDetailActivity)) {
            rootCollection = Constants.rootCollectionRestaurant;
        } else {
            rootCollection = Constants.rootCollectionStreetFood;

        }
        listenNewReview(rootCollection, restaurant.getId());


    }

    @Override
    public void onItemClick(View view, int position) {
        CustomDialogClass cdd = new CustomDialogClass(this, reviewList.get(position).getId(), restaurant.getId());
        cdd.show();
    }

    void initView() {
        ratingTv = findViewById(R.id.ratingTv_id);
        ratingPeoplesTv = findViewById(R.id.ratingPeopleTv_id);
        ratingBar = findViewById(R.id.ratingBar);
        recyclerView = findViewById(R.id.reviewRecyclerview_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new ReviewRecyclerviewAdapter(getApplicationContext(), reviewList);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(this);
    }

    private void listenNewReview(String rootCollection, String documentId) {
        FireStore.db.collection(rootCollection).document(documentId).collection("reviews").addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null) {
            return;
        }
        if (value != null) {
            int count = reviewList.size();
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    String reviewUserName = documentChange.getDocument().getData().get("name").toString();
                    String reviewUserId = documentChange.getDocument().getData().get("id").toString();
                    String reviewId = documentChange.getDocument().getId();
                    String reviewComment = documentChange.getDocument().getData().get("comment").toString();
                    String profileUrl = documentChange.getDocument().getData().get("profileUrl").toString();
                    double rating = Double.parseDouble(documentChange.getDocument().getData().get("rating").toString());
                    double reviewRating = Double.parseDouble(documentChange.getDocument().getData().get("reviewRating").toString());
                    Review review = new Review(reviewUserName, reviewId, reviewUserId, reviewComment, profileUrl, rating, reviewRating);
                    reviewList.add(review);
                }
                if (documentChange.getType() == DocumentChange.Type.MODIFIED) {
//                    String docID = documentChange.getDocument().getId();
//                    Review review = documentChange.getDocument().toObject(Review.class);
//                    if (documentChange.getOldIndex() == documentChange.getNewIndex()) {
//                        // Item changed but remained in same position
//                        reviewList.set(documentChange.getOldIndex(), review);
//                        adapter.notifyItemChanged(documentChange.getOldIndex());
//                    }
                }
            }
            updateRating();
            if (count == 0) {
                adapter.notifyDataSetChanged();
            } else {
                adapter.notifyItemRangeInserted(reviewList.size(), reviewList.size());
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
        ratingTv.setText(rating + "");
        ratingPeoplesTv.setText("From " + reviewList.size() + " people");
        ratingBar.setRating(rating);
    }

}