package com.devexpert.forfoodiesbyfoodies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.devexpert.forfoodiesbyfoodies.activities.DashBoardActivity;
import com.devexpert.forfoodiesbyfoodies.activities.LoginActivity;
import com.devexpert.forfoodiesbyfoodies.services.YourPreference;

public class SplashScreen extends AppCompatActivity {
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        YourPreference yourPreference = YourPreference.getInstance(getApplicationContext());

        String value = yourPreference.getData("userId");


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

}