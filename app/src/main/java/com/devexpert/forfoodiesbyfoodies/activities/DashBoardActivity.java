package com.devexpert.forfoodiesbyfoodies.activities;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.devexpert.forfoodiesbyfoodies.R;
import com.devexpert.forfoodiesbyfoodies.fragments.AddUserByAdminFragment;
import com.devexpert.forfoodiesbyfoodies.fragments.ChatForumFragment;
import com.devexpert.forfoodiesbyfoodies.fragments.ProfileFragment;
import com.devexpert.forfoodiesbyfoodies.fragments.RestaurantsFragment;
import com.devexpert.forfoodiesbyfoodies.fragments.StreetFoodFragment;
import com.devexpert.forfoodiesbyfoodies.models.User;
import com.devexpert.forfoodiesbyfoodies.services.FireStore;
import com.devexpert.forfoodiesbyfoodies.services.CustomSharedPreference;
import com.devexpert.forfoodiesbyfoodies.utils.Constants;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;


public class DashBoardActivity extends AppCompatActivity {
    private DrawerLayout dLayout;
    private TextView textViewName;
    private ImageView imageView;
    private User userData;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        //get userId from sharedPreference
        CustomSharedPreference yourPreference = CustomSharedPreference.getInstance(getApplicationContext());
        String userId = yourPreference.getData(Constants.userId);

        //gets user data from firestore
        FireStore.getData(userId, user -> {
            userData = user;
            if (user.getFirstName().isEmpty()) {
                textViewName.setText(R.string.user_name);
            } else {
                textViewName.setText(user.getFirstName().concat(" ").concat(user.getLastName()));
                Picasso.get().load(user.getImageUrl()).fit().centerCrop().
                        placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.error_image).into(imageView);
                if (userData.isAdmin()) {
                    menu.setGroupCheckable(R.id.second_group, true, true);
                    menu.setGroupVisible(R.id.second_group, true);
                }

            }
        });
        //set drawer (left side menu)
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
        imageView = headerView.findViewById(R.id.profile_image);

        loadFragment(new RestaurantsFragment());        //default fragment is Restaurant fragment
        menu = navView.getMenu();


        // implement setNavigationItemSelectedListener event on NavigationView
        navView.setNavigationItemSelectedListener(menuItem -> {
            Fragment frag; // create a Fragment Object
            int itemId = menuItem.getItemId(); // get selected menu item's id
            // check selected menu item's id and replace a Fragment Accordingly
            if (itemId == R.id.restaurantFragment_id) {
                frag = new RestaurantsFragment();
            } else if (itemId == R.id.profileFragment_id) {
                frag = new ProfileFragment();
            } else if (itemId == R.id.streetFoodFragment_id) {
                frag = new StreetFoodFragment(userData);
            } else if (itemId == R.id.addCritics_id) {
                frag = new AddUserByAdminFragment();
            } else if (itemId == R.id.channelFragment_id) {
                frag = new ChatForumFragment();
            } else {
                frag = new RestaurantsFragment();
            }
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

    @Override
    public void onBackPressed() {
        //when you click back on dashboard it will ask you to exist or not
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage("Do you want to Exist? ");
        builder.setPositiveButton("Yes", (dialog, id) -> finish());
        builder.setNegativeButton("No", (dialog, id) -> {
        });
        builder.show();
    }
}