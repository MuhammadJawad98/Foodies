package com.devexpert.forfoodiesbyfoodies.utils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.devexpert.forfoodiesbyfoodies.R;
import com.devexpert.forfoodiesbyfoodies.models.User;
import com.devexpert.forfoodiesbyfoodies.services.FireStore;

public class AddReviewDialogue extends Dialog implements
        android.view.View.OnClickListener {

    public Activity activity;
    public Dialog dialog;
    public Button btnYes, btnNo;
    public RatingBar ratingBar;
    public EditText edtReview;
    String resDocId;
    float ratingValue;
    User user;
    String from;

    public AddReviewDialogue(Activity a) {
        super(a);
        this.activity = a;
    }

    public AddReviewDialogue(Activity activity, String id, User user, String from) {
        super(activity);
        this.activity = activity;
        this.resDocId = id;
        this.user = user;
        this.from = from;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_add_review_dialogue);
        btnYes = findViewById(R.id.btn_yes);
        btnNo = findViewById(R.id.btn_no);
        ratingBar = findViewById(R.id.ratingBar);
        edtReview = findViewById(R.id.edt_review_id);
        ratingBar.setOnRatingBarChangeListener((ratingBar, v, b) -> {
            System.out.println("rating value ====>>> " + v);
            ratingValue = v;
        });
        btnYes.setOnClickListener(this);
        btnNo.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                String review = edtReview.getText().toString().trim();
                if (from.equals(Constants.restaurantDetailActivity)) {
                    FireStore.addRestaurantReview(Constants.rootCollectionRestaurant, resDocId, review, user.getUserId(), user.getFirstName(), user.getImageUrl(), ratingValue);
                } else {
                    FireStore.addRestaurantReview(Constants.rootCollectionStreetFood, resDocId, review, user.getUserId(), user.getFirstName(), user.getImageUrl(), ratingValue);
                }
                dismiss();
                break;
            default:
                dismiss();
                break;
        }
    }
}