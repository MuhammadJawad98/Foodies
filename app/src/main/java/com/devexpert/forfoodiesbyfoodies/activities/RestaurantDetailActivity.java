package com.devexpert.forfoodiesbyfoodies.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.devexpert.forfoodiesbyfoodies.R;
import com.devexpert.forfoodiesbyfoodies.models.Restaurant;
import com.devexpert.forfoodiesbyfoodies.models.User;
import com.devexpert.forfoodiesbyfoodies.services.FireStore;
import com.devexpert.forfoodiesbyfoodies.services.CustomSharedPreference;
import com.devexpert.forfoodiesbyfoodies.utils.AddReviewDialogue;
import com.devexpert.forfoodiesbyfoodies.utils.CommonFunctions;
import com.devexpert.forfoodiesbyfoodies.utils.Constants;
import com.squareup.picasso.Picasso;

public class RestaurantDetailActivity extends AppCompatActivity {
    private ImageView restaurantImageView;
    private TextView restaurantTextView;
    private TextView restaurantNameTextView;
    private Button btnReservation;
    private Button btnReview;
    private Button btnAddReview;
    private User user;
    private String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);
        initView();

        CustomSharedPreference yourPreference = CustomSharedPreference.getInstance(getApplicationContext());

        String userId = yourPreference.getData(Constants.userId);
        Intent intent = getIntent();
        from = intent.getExtras().getString(Constants.from);
        Restaurant restaurant = (Restaurant) intent.getSerializableExtra(Constants.details);


        FireStore.getData(userId, users -> {
            user = users;
            if (from.equals(Constants.restaurantDetailActivity)) {
                if (users.isCritic()) {
                    btnAddReview.setOnClickListener(view -> {
                        AddReviewDialogue reviewDialogue = new AddReviewDialogue(this, restaurant.getId(), user, from);
                        reviewDialogue.show();
                    });
                } else {
                    CommonFunctions.customLog("Alert: Only critic is allowed to add review");
                }
            } else {
                btnAddReview.setOnClickListener(view -> {
                    AddReviewDialogue reviewDialogue = new AddReviewDialogue(this, restaurant.getId(), user, from);
                    reviewDialogue.show();
                });
            }
        });


        if (from.equals(Constants.streetFoodActivity)) {
            btnReservation.setVisibility(View.GONE);
        } else {
            btnReservation.setVisibility(View.VISIBLE);
        }

        Picasso.get().load(restaurant.getRestaurantImageUrl()).fit().centerCrop().placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image).into(restaurantImageView);
        restaurantTextView.setText(restaurant.getRestaurantDescription());
        restaurantNameTextView.setText(restaurant.getRestaurantName());

        btnReservation.setOnClickListener(view -> {
            Intent intent1 = new Intent(getApplicationContext(), ReservationActivity.class);
            startActivity(intent1);
        });
        btnReview.setOnClickListener(view -> {
            Intent intent2 = new Intent(getApplicationContext(), ReviewsActivity.class);
            intent2.putExtra(Constants.details, restaurant);
            intent2.putExtra(Constants.from, from);
            intent2.putExtra(Constants.user, user);
            startActivity(intent2);
        });
    }

    void initView() {
        restaurantImageView = findViewById(R.id.restaurantImageView_id);
        restaurantTextView = findViewById(R.id.restaurantDescriptionTextView_id);
        restaurantNameTextView = findViewById(R.id.restaurantNameTextView_id);
        btnReservation = findViewById(R.id.btnReservation_id);
        btnReview = findViewById(R.id.btnViewReview_id);
        btnAddReview = findViewById(R.id.btnAddReview_id);
    }
}