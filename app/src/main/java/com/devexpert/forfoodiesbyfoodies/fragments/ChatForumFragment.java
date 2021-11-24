package com.devexpert.forfoodiesbyfoodies.fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devexpert.forfoodiesbyfoodies.R;
import com.devexpert.forfoodiesbyfoodies.adapters.ChannelsAdapter;
import com.devexpert.forfoodiesbyfoodies.interfaces.FirebaseResultListener;
import com.devexpert.forfoodiesbyfoodies.interfaces.FirebaseUserDataResult;
import com.devexpert.forfoodiesbyfoodies.models.User;
import com.devexpert.forfoodiesbyfoodies.services.FireStore;
import com.devexpert.forfoodiesbyfoodies.services.YourPreference;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class ChatForumFragment extends Fragment {
    private RecyclerView recyclerView;
    private ChannelsAdapter adapter;
    private List list = new ArrayList<>();
    private FloatingActionButton actionButton;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ChatForumFragment() {

        FireStore.getChannels(channelList -> {
            list.clear();
            list = channelList;
            adapter = new ChannelsAdapter(getContext(), list);
            recyclerView.setAdapter(adapter);
        });


    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_forum, container, false);
        recyclerView = view.findViewById(R.id.channel_recyclerview_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        actionButton = view.findViewById(R.id.fab_addChannels);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return view;
    }
}