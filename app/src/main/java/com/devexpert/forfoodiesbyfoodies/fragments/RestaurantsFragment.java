package com.devexpert.forfoodiesbyfoodies.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.devexpert.forfoodiesbyfoodies.R;
import com.devexpert.forfoodiesbyfoodies.activities.AddRestaurantActivity;
import com.devexpert.forfoodiesbyfoodies.adapters.RestaurantRecyclerViewAdapter;
import com.devexpert.forfoodiesbyfoodies.models.Restaurant;
import com.devexpert.forfoodiesbyfoodies.services.FireStore;
import com.devexpert.forfoodiesbyfoodies.services.CustomSharedPreference;
import com.devexpert.forfoodiesbyfoodies.utils.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class RestaurantsFragment extends Fragment {
    private RestaurantRecyclerViewAdapter adapter;
        private final List<Restaurant> restaurantList = new ArrayList<>();
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    public RestaurantsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resturants, container, false);
        listenNewRestaurant();      //listen all restaurant in db and also new added restaurant too

        //get user id from local storage
        CustomSharedPreference sharedPreference = CustomSharedPreference.getInstance(getContext());
        String userId = sharedPreference.getData(Constants.userId);

        //get user data from firebase
        FireStore.getData(userId, users -> {
            //if user ia admin then show option for adding new restaurant other wise hide the option
            if (users.isAdmin()) {
                fab.setVisibility(View.VISIBLE);
                fab.setOnClickListener(view1 -> {
                    //navigate to AddRestaurantActivity
                    Intent intent = new Intent(getContext(), AddRestaurantActivity.class);
                    startActivity(intent);
                });
            }
        });
        progressBar = view.findViewById(R.id.progressbar_id);
        fab = view.findViewById(R.id.fab);
        recyclerView = view.findViewById(R.id.restaurantRecyclerview_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RestaurantRecyclerViewAdapter(getContext(), restaurantList);
        recyclerView.setAdapter(adapter);

        return view;
    }


    private void listenNewRestaurant() {
        FireStore.db.collection(Constants.rootCollectionRestaurant).orderBy(Constants.restaurantName, Query.Direction.ASCENDING).addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null) {
            return;
        }
        if (value != null) {
            int count = restaurantList.size();
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    String imageUrl = documentChange.getDocument().getData().get("restaurantImageUrl").toString();
                    String description = documentChange.getDocument().getData().get("restaurantDescription").toString();
                    String name = documentChange.getDocument().getData().get(Constants.restaurantName).toString();
                    String id = documentChange.getDocument().getData().get(Constants.id).toString();

                    Restaurant restaurant = new Restaurant(imageUrl, description, name, id);
                    restaurantList.add(restaurant);
                }
            }
            if (count == 0) {
                adapter.notifyDataSetChanged();
            } else {
                adapter.notifyItemRangeInserted(restaurantList.size(), restaurantList.size());
            }
            recyclerView.setVisibility(View.VISIBLE);
        }
        progressBar.setVisibility(View.GONE);

    };

}