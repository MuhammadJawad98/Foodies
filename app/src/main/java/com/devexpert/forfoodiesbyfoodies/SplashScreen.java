package com.devexpert.forfoodiesbyfoodies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.devexpert.forfoodiesbyfoodies.activities.DashBoardActivity;
import com.devexpert.forfoodiesbyfoodies.activities.LoginActivity;
import com.devexpert.forfoodiesbyfoodies.services.FireStore;
import com.devexpert.forfoodiesbyfoodies.services.YourPreference;
import com.devexpert.forfoodiesbyfoodies.utils.CommonFunctions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        runAnimation();
        YourPreference yourPreference = YourPreference.getInstance(getApplicationContext());

        String value = yourPreference.getData("userId");
        System.out.println("value>>>>>>"+value);

//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent;
            if (value.isEmpty()) {
                intent = new Intent(getApplicationContext(), LoginActivity.class);
            } else {
                intent = new Intent(getApplicationContext(), DashBoardActivity.class);
            }
            startActivity(intent);
            finish();
        }, 3000);
    }

    private void runAnimation() {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.text_animation);
        a.reset();
        TextView splashScreenText = findViewById(R.id.brandNameTextView_id);
        splashScreenText.clearAnimation();
        splashScreenText.startAnimation(a);
    }
}