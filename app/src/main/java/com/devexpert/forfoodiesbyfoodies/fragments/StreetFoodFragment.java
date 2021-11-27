package com.devexpert.forfoodiesbyfoodies.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.devexpert.forfoodiesbyfoodies.R;
import com.devexpert.forfoodiesbyfoodies.activities.AddStreetFoodActivity;
import com.devexpert.forfoodiesbyfoodies.adapters.StreetFoodRecyclerviewAdapter;
import com.devexpert.forfoodiesbyfoodies.models.StreetFood;
import com.devexpert.forfoodiesbyfoodies.models.User;
import com.devexpert.forfoodiesbyfoodies.services.FireStore;
import com.devexpert.forfoodiesbyfoodies.utils.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class StreetFoodFragment extends Fragment {
    private StreetFoodRecyclerviewAdapter adapter;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private final List<StreetFood> streetFoodList = new ArrayList<>();
    private final User userData;

    public StreetFoodFragment(User user) {
        this.userData = user;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_street_food, container, false);

        progressBar = view.findViewById(R.id.progressbar_id);
        recyclerView = view.findViewById(R.id.streetFoodRecyclerview_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new StreetFoodRecyclerviewAdapter(getContext(), streetFoodList, userData);
        recyclerView.setAdapter(adapter);
        listenNewStreetFoodRestaurant();       //listen all street food entries

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(view1 -> {
            //navigate to AddStreetFoodActivity
            Intent intent = new Intent(getContext(), AddStreetFoodActivity.class);
            startActivity(intent);
        });

        return view;
    }

    //Function for listening all street food restaurant including new one too
    private void listenNewStreetFoodRestaurant() {
        FireStore.db.collection(Constants.rootCollectionStreetFood).orderBy(Constants.name, Query.Direction.ASCENDING)
                .addSnapshotListener(eventListener);
        progressBar.setVisibility(View.GONE);

    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null) {
            return;
        }
        if (value != null) {
            int count = streetFoodList.size();
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    StreetFood streetFood = new StreetFood();
                    streetFood.setDescription(documentChange.getDocument().get("description").toString());
                    streetFood.setLocation(documentChange.getDocument().get("location").toString());
                    streetFood.setName(documentChange.getDocument().get(Constants.name).toString());
                    streetFood.setPicture(documentChange.getDocument().get("picture").toString());
                    streetFood.setType(documentChange.getDocument().get("type").toString());
                    streetFood.setUserId(documentChange.getDocument().get(Constants.userId).toString());
                    streetFood.setId(documentChange.getDocument().getId());
                    streetFoodList.add(streetFood);
                }
            }
            if (count == 0) {
                adapter.notifyDataSetChanged();
            } else {
                adapter.notifyItemRangeInserted(streetFoodList.size(), streetFoodList.size());
            }
            recyclerView.setVisibility(View.VISIBLE);
        }
        progressBar.setVisibility(View.GONE);

    };
}