package com.devexpert.forfoodiesbyfoodies.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.service.controls.actions.CommandAction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.devexpert.forfoodiesbyfoodies.R;
import com.devexpert.forfoodiesbyfoodies.activities.AddRestaurantActivity;
import com.devexpert.forfoodiesbyfoodies.adapters.RecyclerViewAdapter;
import com.devexpert.forfoodiesbyfoodies.interfaces.FirebaseResultListener;
import com.devexpert.forfoodiesbyfoodies.models.Chat;
import com.devexpert.forfoodiesbyfoodies.models.Restaurant;
import com.devexpert.forfoodiesbyfoodies.models.User;
import com.devexpert.forfoodiesbyfoodies.services.FireStore;
import com.devexpert.forfoodiesbyfoodies.services.YourPreference;
import com.devexpert.forfoodiesbyfoodies.utils.CommonFunctions;
import com.devexpert.forfoodiesbyfoodies.utils.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class RestaurantsFragment extends Fragment
//        implements RecyclerViewAdapter.ItemClickListener
{
    private static final String ARG_PARAM1 = "";
    private static final String ARG_PARAM2 = "";
    private RecyclerViewAdapter adapter;
    private List<Restaurant> restaurantList = new ArrayList<>();
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private User user;

    public RestaurantsFragment() {
        // Required empty public constructor
        Log.d("Restaurant Fragment", "I am here");

    }

    public static RestaurantsFragment newInstance(String param1, String param2) {
        RestaurantsFragment fragment = new RestaurantsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_resturants, container, false);
        listenNewRestaurant();
        YourPreference yourPreference = YourPreference.getInstance(getContext());

        String userId = yourPreference.getData("userId");
        FireStore.getData(userId, users -> {
            user = users;
            System.out.println("User Data: " + user.getFirstName() + "  " + user.isUser() + " " + user.isCritic() + " " + user.isAdmin());
            if (users.isAdmin()) {
                fab.setVisibility(View.VISIBLE);
                fab.setOnClickListener(view1 -> {
                    Intent intent = new Intent(getContext(), AddRestaurantActivity.class);
                    startActivity(intent);
                });
            }
        });
        progressBar = view.findViewById(R.id.progressbar_id);
        fab = view.findViewById(R.id.fab);
        recyclerView = view.findViewById(R.id.restaurantRecyclerview_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecyclerViewAdapter(getContext(), restaurantList);
        recyclerView.setAdapter(adapter);

        return view;
    }


    private void listenNewRestaurant() {
        FireStore.db.collection("restaurants").addSnapshotListener(eventListener);
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
                    String name = documentChange.getDocument().getData().get("restaurantName").toString();
                    String id = documentChange.getDocument().getData().get("id").toString();

//                    Log.d("Restaurants details: ", documentChange.getDocument().getId());
                    Restaurant restaurant = new Restaurant(imageUrl, description, name, id);
                    restaurantList.add(restaurant);
                }
            }
//            Collections.sort(chatMessages, (obj1, obj2) -> obj1.getMessageId().compareTo(obj2.getMessageId()));
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