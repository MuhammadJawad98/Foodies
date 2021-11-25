package com.devexpert.forfoodiesbyfoodies.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.EditText;

import com.devexpert.forfoodiesbyfoodies.R;
import com.devexpert.forfoodiesbyfoodies.adapters.ChannelsAdapter;
import com.devexpert.forfoodiesbyfoodies.models.Channels;
import com.devexpert.forfoodiesbyfoodies.services.FireStore;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class ChatForumFragment extends Fragment {
    private RecyclerView recyclerView;
    private ChannelsAdapter adapter;
    private List channelsList = new ArrayList<>();
    private FloatingActionButton actionButton;


    public ChatForumFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_forum, container, false);
        recyclerView = view.findViewById(R.id.channel_recyclerview_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        adapter = new ChannelsAdapter(getContext(), channelsList);
        recyclerView.setAdapter(adapter);
        actionButton = view.findViewById(R.id.fab_addChannels);
        listenNewChannels();


        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogue();
            }
        });
        return view;
    }

    private void listenNewChannels() {
        FireStore.db.collection("channels").addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null) {
            return;
        }
        if (value != null) {
            int count = channelsList.size();
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    Channels channel = new Channels();
                    channel.setTopic(documentChange.getDocument().get("topic").toString());
                    channel.setId(documentChange.getDocument().getId());
                    channelsList.add(channel);
                }
            }
            if (count == 0) {
                adapter.notifyDataSetChanged();
            } else {
                adapter.notifyItemRangeInserted(channelsList.size(), channelsList.size());
                recyclerView.smoothScrollToPosition(channelsList.size() - 1);
            }
            recyclerView.setVisibility(View.VISIBLE);
        }
    };

    private void openDialogue() {
        final EditText taskEditText = new EditText(getContext());
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Add new topic!")
//                .setMessage("Anything you want to discuss with other people?")
                .setView(taskEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String topic = taskEditText.getText().toString().trim();

                        if(topic.isEmpty()){
                            return;
                        }
                        FireStore.createNewTopic(topic);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

}