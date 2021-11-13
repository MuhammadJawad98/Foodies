package com.devexpert.forfoodiesbyfoodies.activities;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.devexpert.forfoodiesbyfoodies.R;
import com.devexpert.forfoodiesbyfoodies.fragments.RestaurantsFragment;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

public class DashBoardActivity extends AppCompatActivity {
    DrawerLayout dLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        setNavigationDrawer(); // call method
    }

    private void setNavigationDrawer() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dLayout = (DrawerLayout) findViewById(R.id.drawer_layout); // initiate a DrawerLayout

        //toggle button in appbar
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, dLayout, toolbar, R.string.main_drawer_open, R.string.main_drawer_close);
        dLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        NavigationView navView = (NavigationView) findViewById(R.id.navigation); // initiate a Navigation View
        loadFragment(new RestaurantsFragment());

        // implement setNavigationItemSelectedListener event on NavigationView
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NotNull MenuItem menuItem) {
                Fragment frag = null; // create a Fragment Object
                int itemId = menuItem.getItemId(); // get selected menu item's id
                // check selected menu item's id and replace a Fragment Accordingly
                if (itemId == R.id.first) {
                    frag = new RestaurantsFragment();
                } else if (itemId == R.id.second) {
                    frag = new RestaurantsFragment();
                } else if (itemId == R.id.third) {
                    frag = new RestaurantsFragment();
                }
                // display a toast message with menu item's title
                Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                loadFragment(frag);
                return false;
            }
        });
    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragment);
        transaction.commit();
        dLayout.closeDrawers(); // close the all open Drawer Views

    }
}