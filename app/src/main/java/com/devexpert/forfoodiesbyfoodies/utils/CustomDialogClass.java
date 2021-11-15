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

public class CustomDialogClass extends Dialog implements
        android.view.View.OnClickListener {

    public Activity activity;
    public Dialog dialog;
    public Button yes, no;
    public RatingBar ratingBar;
    String reviewDocId;
    String restaurantId;
    float ratingValue;

    public CustomDialogClass(Activity a) {
        super(a);
        this.activity = a;
    }

    public CustomDialogClass(Activity activity,String id,String restaurantId) {
        super(activity);
        this.activity = activity;
        this.reviewDocId =id;
        this.restaurantId=restaurantId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        yes =  findViewById(R.id.btn_yes);
        no =  findViewById(R.id.btn_no);
        ratingBar= findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener((ratingBar, v, b) -> {
            System.out.println("rating value ====>>> "+v);
            ratingValue=v;
        });
        yes.setOnClickListener(this);
        no.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                //call fireStore add review rating function
                FireStore.addRating(restaurantId,reviewDocId,ratingValue);
                activity.finish();
                break;
            case R.id.btn_no:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}