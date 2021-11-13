package com.devexpert.forfoodiesbyfoodies.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.devexpert.forfoodiesbyfoodies.R;
import com.devexpert.forfoodiesbyfoodies.models.Restaurant;
import com.squareup.picasso.Picasso;

public class RestaurantDetailActivity extends AppCompatActivity {
    private ImageView restaurantImageView;
    private TextView restaurantTextView;
    private Button btnReservation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);
        initView();
        Intent intent = getIntent();
        Restaurant restaurant = (Restaurant) intent.getSerializableExtra("details");
        Picasso.get().load(restaurant.getRestaurantImageUrl()).fit().centerCrop().
                placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image).into(restaurantImageView);
        restaurantTextView.setText(restaurant.getRestaurantDescription());
        btnReservation.setOnClickListener(view -> {
            Intent intent1 = new Intent(getApplicationContext(), ReservationActivity.class);
            startActivity(intent1);
        });

    }

    void initView() {
        restaurantImageView = findViewById(R.id.restaurantImageView_id);
        restaurantTextView = findViewById(R.id.restaurantDescriptionTextView_id);
        btnReservation = findViewById(R.id.btnReservation_id);
    }
}