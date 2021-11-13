package com.devexpert.forfoodiesbyfoodies.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import com.devexpert.forfoodiesbyfoodies.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReservationActivity extends AppCompatActivity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        webView = findViewById(R.id.webview_id);
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String currentYear = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());

        String url = "https://www.opentable.com/s/?covers=2&dateTime=" + currentDate + "%" + currentYear + "%3A00&metroId=72&regionIds=5316&pinnedRids%5B0%5D=87967&enableSimpleCuisines=true&includeTicketedAvailability=true&pageType=0";
        Log.d("Webview Url::::", url);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl(url);
    }
}