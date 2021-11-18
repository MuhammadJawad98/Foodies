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
import com.devexpert.forfoodiesbyfoodies.activities.RestaurantDetailActivity;
import com.devexpert.forfoodiesbyfoodies.adapters.RecyclerViewAdapter;
import com.devexpert.forfoodiesbyfoodies.adapters.ReviewRecyclerviewAdapter;
import com.devexpert.forfoodiesbyfoodies.adapters.StreetFoodRecyclerviewAdapter;
import com.devexpert.forfoodiesbyfoodies.interfaces.StreetFoodResult;
import com.devexpert.forfoodiesbyfoodies.models.Restaurant;
import com.devexpert.forfoodiesbyfoodies.models.StreetFood;
import com.devexpert.forfoodiesbyfoodies.services.FireStore;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;


public class StreetFoodFragment extends Fragment  {
    //    private List<StreetFood> streetFoodList = new ArrayList<>();
    private StreetFoodRecyclerviewAdapter adapter;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private List<StreetFood> list;
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    public StreetFoodFragment() {
        // Required empty public constructor

    }


    public static StreetFoodFragment newInstance(String param1, String param2) {
        StreetFoodFragment fragment = new StreetFoodFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_street_food, container, false);

        progressBar = view.findViewById(R.id.progressbar_id);
        recyclerView = view.findViewById(R.id.streetFoodRecyclerview_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        FireStore.getStreetFoodData(streetFoods -> {
            try {

                list = streetFoods;
                progressBar.setVisibility(View.GONE);
                System.out.println(">>>>>>>>>>>>>>> food: " + list.size());
                adapter = new StreetFoodRecyclerviewAdapter(getContext(), list);
//        adapter.setClickListener(this);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                System.out.println(">>>>>>>>> error  street" + e.getMessage());
            }
        });

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), AddStreetFoodActivity.class);
            startActivity(intent);
        });

        return view;
    }
}