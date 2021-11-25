package com.devexpert.forfoodiesbyfoodies.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.devexpert.forfoodiesbyfoodies.R;
import com.devexpert.forfoodiesbyfoodies.adapters.ChatAdapter;
import com.devexpert.forfoodiesbyfoodies.adapters.RecyclerViewAdapter;
import com.devexpert.forfoodiesbyfoodies.interfaces.OnResult;
import com.devexpert.forfoodiesbyfoodies.models.Chat;
import com.devexpert.forfoodiesbyfoodies.models.User;
import com.devexpert.forfoodiesbyfoodies.services.FireStore;
import com.devexpert.forfoodiesbyfoodies.services.YourPreference;
import com.devexpert.forfoodiesbyfoodies.utils.CommonFunctions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class ChatActivity extends AppCompatActivity {
    private String documentId;
    private User userData;
    private EditText editText;
    private Button btnSendMessage;
    private RecyclerView recyclerView;
    private ChatAdapter adapter;
    private List<Chat> chatMessages = new ArrayList<>();
    private String userId;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        YourPreference yourPreference = YourPreference.getInstance(getApplicationContext());
        userId = yourPreference.getData("userId");

        editText = findViewById(R.id.edt_message_id);
        btnSendMessage = findViewById(R.id.btnSend_id);
        recyclerView = findViewById(R.id.chat_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new ChatAdapter(getApplicationContext(), chatMessages, userId);
        recyclerView.setAdapter(adapter);


        FireStore.getData(userId, user -> userData = user);

        Intent intent = getIntent();
        documentId = intent.getStringExtra("docId");

        listenMessage();


        btnSendMessage.setOnClickListener(view -> sendMessage());
    }

    void sendMessage() {
        String msg = editText.getText().toString().trim();
        if (msg.isEmpty()) {
            CommonFunctions.showToast("Type something!", getApplicationContext());
            return;
        }
        Chat chat = new Chat(msg, CommonFunctions.CurrentDateTime(), userData.getUserId(), userData.getFirstName());
        editText.setText("");
        FireStore.sendMessage(documentId, chat);
    }

    private void listenMessage() {
        FireStore.db.collection("channels").document(documentId).collection("messages").orderBy("timestamp", Query.Direction.ASCENDING).addSnapshotListener(eventListener);

    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null) {
            return;
        }
        if (value != null) {
            int count = chatMessages.size();
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    System.out.println(">>>>>>>>>>>>>>>>>>" + documentChange.getDocument().get("text").toString());
                    Chat chat = new Chat();
                    chat.setText(documentChange.getDocument().get("text").toString());
                    Date creationDate = documentChange.getDocument().getDate("timestamp");
                    chat.setTimestamp(creationDate);
                    chat.setUserId(documentChange.getDocument().get("userId").toString());
                    chat.setUserName(documentChange.getDocument().get("userName").toString());
                    chat.setMessageId(documentChange.getDocument().getId());
                    chatMessages.add(chat);
                }
            }
//            Collections.sort(chatMessages, (obj1, obj2) -> obj1.getMessageId().compareTo(obj2.getMessageId()));
            if (count == 0) {
                if (chatMessages.size() > 0) {
                    recyclerView.smoothScrollToPosition(chatMessages.size() - 1);
                }
                adapter.notifyDataSetChanged();
            } else {
                adapter.notifyItemRangeInserted(chatMessages.size(), chatMessages.size());
                recyclerView.smoothScrollToPosition(chatMessages.size() - 1);
            }
            recyclerView.setVisibility(View.VISIBLE);
        }
    };

}