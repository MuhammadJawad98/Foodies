package com.devexpert.forfoodiesbyfoodies.fragments;

import android.app.ProgressDialog;
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

import com.devexpert.forfoodiesbyfoodies.R;
import com.devexpert.forfoodiesbyfoodies.adapters.RecyclerViewAdapter;
import com.devexpert.forfoodiesbyfoodies.interfaces.FirebaseResultListener;
import com.devexpert.forfoodiesbyfoodies.models.Restaurant;
import com.devexpert.forfoodiesbyfoodies.services.FireStore;
import com.devexpert.forfoodiesbyfoodies.utils.CommonFunctions;
import com.devexpert.forfoodiesbyfoodies.utils.Constants;

import java.util.ArrayList;
import java.util.List;


public class RestaurantsFragment extends Fragment implements RecyclerViewAdapter.ItemClickListener {
    private static final String ARG_PARAM1 = "";
    private static final String ARG_PARAM2 = "";
    RecyclerViewAdapter adapter;
    List<Restaurant> restaurantList = new ArrayList<>();

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
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_resturants, container, false);
        FireStore.getRestaurantFromFirebase(getContext(), list -> {
            restaurantList.clear();
            restaurantList = list;
            RecyclerView recyclerView = view.findViewById(R.id.restaurantRecyclerview_id);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new RecyclerViewAdapter(getContext(), restaurantList);
            adapter.setClickListener(this);
            recyclerView.setAdapter(adapter);
        });
        return view;
    }

    @Override
    public void onItemClick(View view, int position) {
        //handling click on the item
//        CommonFunctions.showToast("You click on item " + position, getContext());
    }


}