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

import java.util.Calendar;


public class ChatActivity extends AppCompatActivity {
    private String documentId;
    private User userData;
    private EditText editText;
    private Button btnSendMessage;
    private RecyclerView recyclerView;
    private ChatAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        editText = findViewById(R.id.edt_message_id);
        btnSendMessage = findViewById(R.id.btnSend_id);
        recyclerView = findViewById(R.id.chat_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        YourPreference yourPreference = YourPreference.getInstance(getApplicationContext());

        String userId = yourPreference.getData("userId");
        System.out.println("userid::::::" + userId);
        FireStore.getData(userId, user -> userData = user);


        Intent intent = getIntent();
        documentId = intent.getStringExtra("docId");
        Log.d(">>>>>>>>> Time ", java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()));
        FireStore.getChannelChat(documentId, list -> {
            Log.d("Chat Activity: ", list.size() + "");
            adapter = new ChatAdapter(getApplicationContext(), list,userId);
//        adapter.setClickListener(this);
            recyclerView.setAdapter(adapter);
        });
        btnSendMessage.setOnClickListener(view -> sendMessage());
    }

    void sendMessage() {
        String msg = editText.getText().toString().trim();
        if (msg.isEmpty()) {
            CommonFunctions.showToast("Type something!", getApplicationContext());
            return;
        }
        Chat chat = new Chat(msg, CommonFunctions.CurrentDateTime(), userData.getUserId(), userData.getFirstName());
        FireStore.sendMessage(documentId, chat, new OnResult() {
            @Override
            public void onComplete() {
                //add to list and notify
                editText.setText("");
            }

            @Override
            public void onFailure() {
                //show toast
            }
        });
    }

}