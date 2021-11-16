package com.devexpert.forfoodiesbyfoodies.activities;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.devexpert.forfoodiesbyfoodies.R;
import com.devexpert.forfoodiesbyfoodies.fragments.RestaurantsFragment;
import com.devexpert.forfoodiesbyfoodies.fragments.StreetFoodFragment;
import com.devexpert.forfoodiesbyfoodies.interfaces.FirebaseUserDataResult;
import com.devexpert.forfoodiesbyfoodies.models.User;
import com.devexpert.forfoodiesbyfoodies.services.FireStore;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

public class DashBoardActivity extends AppCompatActivity {
    DrawerLayout dLayout;
    TextView textViewName;
    String name="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        FireStore.getData(new FirebaseUserDataResult() {
            @Override
            public void onComplete(User user) {
//                name=user.getFirstName();
                if (user.getFirstName().isEmpty()) {
                    textViewName.setText("User name");

                } else {
                    System.out.println("!@!@!@!@!@!@!@"+user.getFirstName());
                    textViewName.setText(user.getFirstName()+" "+user.getLastName());

                }
            }
        });
        setNavigationDrawer(); // call method
    }

    private void setNavigationDrawer() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dLayout = findViewById(R.id.drawer_layout); // initiate a DrawerLayout

        //toggle button in appbar
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, dLayout, toolbar, R.string.main_drawer_open, R.string.main_drawer_close);
        dLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        NavigationView navView = findViewById(R.id.navigation); // initiate a Navigation View
        View headerView = navView.getHeaderView(0);
        textViewName = headerView.findViewById(R.id.name);

        loadFragment(new RestaurantsFragment());

        // implement setNavigationItemSelectedListener event on NavigationView
        navView.setNavigationItemSelectedListener(menuItem -> {
            Fragment frag = null; // create a Fragment Object
            int itemId = menuItem.getItemId(); // get selected menu item's id
            // check selected menu item's id and replace a Fragment Accordingly
            if (itemId == R.id.restaurantFragment_id) {
                frag = new RestaurantsFragment();
            } else if (itemId == R.id.profileFragment_id) {
                frag = new RestaurantsFragment();
            } else if (itemId == R.id.streetFoodFragment_id) {
                frag = new StreetFoodFragment();
            }
            // display a toast message with menu item's title
            Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_SHORT).show();
            loadFragment(frag);
            return false;
        });
    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragment);
        transaction.commit();
        dLayout.closeDrawers(); // close the all open Drawer Views

    }
}