package com.devexpert.forfoodiesbyfoodies.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class ReviewsActivity extends AppCompatActivity implements ReviewRecyclerviewAdapter.ItemClickListener {
    ReviewRecyclerviewAdapter adapter;
    private TextView ratingTv;
    private TextView ratingPeoplesTv;
    private RatingBar ratingBar;
    private Restaurant restaurant;
    RecyclerView recyclerView;
    List<Review> list = new ArrayList();
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
        FireStore.getReviews(rootCollection, restaurant.getId(), reviewList -> {
            list = reviewList;
            float rating = 0;
            System.out.println("?????????" + reviewList.size());
            if (reviewList.size() != 0) {
                for (int i = 0; i < reviewList.size(); i++) {
                    rating = (float) (rating + reviewList.get(i).getRating());
                }
                rating = rating / reviewList.size();

            }

            ratingTv.setText(rating + "");
            ratingPeoplesTv.setText("From " + reviewList.size() + " people");
            ratingBar.setRating(rating);

//            System.out.println("^^^^^^^^^^^^^^^"+reviewList.get(0).getReviewRating().size());
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            adapter = new ReviewRecyclerviewAdapter(getApplicationContext(), reviewList);
            //add ItemDecoration
            recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                    DividerItemDecoration.VERTICAL));
            adapter.setClickListener(this);
            recyclerView.setAdapter(adapter);

        });


    }

    @Override
    public void onItemClick(View view, int position) {
        System.out.println(list.get(position).getId() + "*****************************" + restaurant.getId());

        CustomDialogClass cdd = new CustomDialogClass(this, list.get(position).getId(), restaurant.getId());
        cdd.show();
    }

    void initView() {
        ratingTv = findViewById(R.id.ratingTv_id);
        ratingPeoplesTv = findViewById(R.id.ratingPeopleTv_id);
        ratingBar = findViewById(R.id.ratingBar);
        recyclerView = findViewById(R.id.reviewRecyclerview_id);

    }
}