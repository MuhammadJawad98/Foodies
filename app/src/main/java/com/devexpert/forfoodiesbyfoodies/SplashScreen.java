package com.devexpert.forfoodiesbyfoodies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.devexpert.forfoodiesbyfoodies.activities.DashBoardActivity;
import com.devexpert.forfoodiesbyfoodies.activities.LoginActivity;
import com.devexpert.forfoodiesbyfoodies.services.CustomSharedPreference;
import com.devexpert.forfoodiesbyfoodies.utils.Constants;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);   //hide the status bar and show the splash screen full
        setContentView(R.layout.activity_splash);

        //getting user id from local storage
        CustomSharedPreference sharedPreference = CustomSharedPreference.getInstance(getApplicationContext());
        String value = sharedPreference.getData(Constants.userId);

        Handler handler = new Handler();
        handler.postDelayed(() -> {     //running a thread that complete after 3 seconds and move to specifc screen
            Intent intent;
            if (value.isEmpty()) {  //if user id is null move to login screen
                intent = new Intent(getApplicationContext(), LoginActivity.class);
            } else {        //otherwise move to dashboard
                intent = new Intent(getApplicationContext(), DashBoardActivity.class);
            }
            startActivity(intent);
            finish();
        }, 3000);
    }

}