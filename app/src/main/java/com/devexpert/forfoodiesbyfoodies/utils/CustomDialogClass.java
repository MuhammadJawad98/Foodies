package com.devexpert.forfoodiesbyfoodies.utils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RatingBar;

import com.devexpert.forfoodiesbyfoodies.R;
import com.devexpert.forfoodiesbyfoodies.services.FireStore;

public class CustomDialogClass extends Dialog implements android.view.View.OnClickListener {

    private final Activity activity;
    private final String reviewDocId;
    private final String restaurantId;
    private float ratingValue;
    private final String rootCollection;


    public CustomDialogClass(Activity activity, String id, String restaurantId, String rootCollection) {
        super(activity);
        this.activity = activity;
        this.reviewDocId = id;
        this.restaurantId = restaurantId;
        this.rootCollection = rootCollection;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        Button yes = findViewById(R.id.btn_yes);
        Button no = findViewById(R.id.btn_no);
        RatingBar ratingBar1 = findViewById(R.id.ratingBar);
        ratingBar1.setOnRatingBarChangeListener((ratingBar, v, b) -> ratingValue = v);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                //call fireStore add review rating function
                if (ratingValue > 0) {
                    FireStore.addRating(rootCollection, restaurantId, reviewDocId, ratingValue, activity.getApplicationContext());
                    dismiss();
                }
                break;
            case R.id.btn_no:
                dismiss();      //close dialogue
                break;
        }
        dismiss();
    }
}