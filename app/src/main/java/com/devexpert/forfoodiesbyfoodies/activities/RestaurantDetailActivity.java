package com.devexpert.forfoodiesbyfoodies.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.devexpert.forfoodiesbyfoodies.R;
import com.devexpert.forfoodiesbyfoodies.interfaces.FirebaseUserDataResult;
import com.devexpert.forfoodiesbyfoodies.models.Restaurant;
import com.devexpert.forfoodiesbyfoodies.models.User;
import com.devexpert.forfoodiesbyfoodies.services.FireStore;
import com.devexpert.forfoodiesbyfoodies.services.YourPreference;
import com.devexpert.forfoodiesbyfoodies.utils.CustomDialogClass;
import com.squareup.picasso.Picasso;

public class RestaurantDetailActivity extends AppCompatActivity {
    private ImageView restaurantImageView;
    private TextView restaurantTextView;
    private TextView restaurantNameTextView;
    private Button btnReservation;
    private Button btnReview;
    private Button btnAddReview;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);
        initView();

        YourPreference yourPreference = YourPreference.getInstance(getApplicationContext());

        String userId = yourPreference.getData("userId");
        System.out.println("value>>>>>>" + userId);

        FireStore.getData(userId, users -> {
            user = users;
            System.out.println("######1##" + user.getFirstName() + "  " + user.isUser()+" "+user.isCritic()+" "+user.isAdmin());

//                if (user.isAdmin()) {
//                    Log.d("is user::: ", user.isUser() + "");
//                    btnAddReview.setVisibility(View.VISIBLE);
//                }
        });


        Intent intent = getIntent();
        Restaurant restaurant = (Restaurant) intent.getSerializableExtra("details");
        Picasso.get().load(restaurant.getRestaurantImageUrl()).fit().centerCrop().
                placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image).into(restaurantImageView);
        restaurantTextView.setText(restaurant.getRestaurantDescription());
        restaurantNameTextView.setText(restaurant.getRestaurantName());
//        btnAddReview.setOnClickListener(view -> {
//            CustomDialogClass cdd = new CustomDialogClass(this);
//            cdd.show();
//        });
        btnReservation.setOnClickListener(view -> {
            Intent intent1 = new Intent(getApplicationContext(), ReservationActivity.class);
            startActivity(intent1);
        });
        btnReview.setOnClickListener(view -> {
            Intent intent2 = new Intent(getApplicationContext(), ReviewsActivity.class);
            intent2.putExtra("details", restaurant);
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