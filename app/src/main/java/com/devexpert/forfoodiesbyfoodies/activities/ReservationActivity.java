package com.devexpert.forfoodiesbyfoodies.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.devexpert.forfoodiesbyfoodies.R;
import com.devexpert.forfoodiesbyfoodies.utils.CommonFunctions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReservationActivity extends AppCompatActivity {
    WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        webView = findViewById(R.id.webview_id);
        //gives the current date like 2021-11-27
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        //gives you the current year like 2021
        String currentYear = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());

        String url = "https://www.opentable.com/s/?covers=2&dateTime=" + currentDate + "%" + currentYear + "%3A00&metroId=72&regionIds=5316&pinnedRids%5B0%5D=87967&enableSimpleCuisines=true&includeTicketedAvailability=true&pageType=0";
        CommonFunctions.customLog(url);     //prints url in log
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl(url);
    }
}