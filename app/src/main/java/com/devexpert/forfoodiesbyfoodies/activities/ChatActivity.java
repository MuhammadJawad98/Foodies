package com.devexpert.forfoodiesbyfoodies.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.devexpert.forfoodiesbyfoodies.R;
import com.devexpert.forfoodiesbyfoodies.adapters.ChatAdapter;
import com.devexpert.forfoodiesbyfoodies.models.Chat;
import com.devexpert.forfoodiesbyfoodies.models.User;
import com.devexpert.forfoodiesbyfoodies.services.FireStore;
import com.devexpert.forfoodiesbyfoodies.services.CustomSharedPreference;
import com.devexpert.forfoodiesbyfoodies.utils.CommonFunctions;
import com.devexpert.forfoodiesbyfoodies.utils.Constants;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ChatActivity extends AppCompatActivity {
    private String documentId;
    private User userData;
    private EditText editText;
    private RecyclerView recyclerView;
    private ChatAdapter adapter;
    private final List<Chat> chatMessages = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        CustomSharedPreference yourPreference = CustomSharedPreference.getInstance(getApplicationContext());
        String userId = yourPreference.getData(Constants.userId);

        //initializing view
        editText = findViewById(R.id.edt_message_id);
        Button btnSendMessage = findViewById(R.id.btnSend_id);
        recyclerView = findViewById(R.id.chat_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new ChatAdapter(chatMessages, userId);
        recyclerView.setAdapter(adapter);

        //get user data from firestore
        FireStore.getData(userId, user -> userData = user);

        //Gets data that sends fro previous activity
        Intent intent = getIntent();
        documentId = intent.getStringExtra(Constants.docId);

        listenMessage();        //it will listen all the messages including new one too.


        btnSendMessage.setOnClickListener(view -> sendMessage());       //click to send message
    }

    void sendMessage() {
        String msg = editText.getText().toString().trim();
        if (msg.isEmpty()) {        //check is message is empty or not
            CommonFunctions.showToast("Type something!", getApplicationContext());
            return;
        }
        //if message is not empty then send it to firestore
        Chat chat = new Chat(msg, CommonFunctions.CurrentDateTime(), userData.getUserId(), userData.getFirstName());
        FireStore.sendMessage(documentId, chat);
        editText.setText("");       //after sending message clear edit text
    }

    //method for listening all messages also new one too
    private void listenMessage() {
        FireStore.db.collection(Constants.rootCollectionChannels).document(documentId).collection(Constants.messages).orderBy("timestamp", Query.Direction.ASCENDING).addSnapshotListener(eventListener);
    }

    //return all messages
    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null) {
            return;
        }
        if (value != null) {
            int count = chatMessages.size();
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    Chat chat = new Chat();
                    chat.setText(documentChange.getDocument().get("text").toString());
                    Date creationDate = documentChange.getDocument().getDate("timestamp");
                    chat.setTimestamp(creationDate);
                    chat.setUserId(documentChange.getDocument().get(Constants.userId).toString());
                    chat.setUserName(documentChange.getDocument().get("userName").toString());
                    chat.setMessageId(documentChange.getDocument().getId());
                    chatMessages.add(chat);
                }
            }
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