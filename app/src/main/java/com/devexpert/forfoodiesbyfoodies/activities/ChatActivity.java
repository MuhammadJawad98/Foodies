package com.devexpert.forfoodiesbyfoodies.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.devexpert.forfoodiesbyfoodies.R;
import com.devexpert.forfoodiesbyfoodies.interfaces.OnResult;
import com.devexpert.forfoodiesbyfoodies.models.Chat;
import com.devexpert.forfoodiesbyfoodies.models.User;
import com.devexpert.forfoodiesbyfoodies.services.FireStore;
import com.devexpert.forfoodiesbyfoodies.services.YourPreference;
import com.devexpert.forfoodiesbyfoodies.utils.CommonFunctions;

import java.util.Calendar;


public class ChatActivity extends AppCompatActivity {
    private String documentId;
    private User userData;
    private EditText editText;
    private Button btnSendMessage;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        editText = findViewById(R.id.edt_message_id);
        btnSendMessage = findViewById(R.id.btnSend_id);
        YourPreference yourPreference = YourPreference.getInstance(getApplicationContext());

        String userId = yourPreference.getData("userId");
        System.out.println("userid::::::"+userId);
        FireStore.getData(userId, user -> userData=user);


        Intent intent = getIntent();
        documentId = intent.getStringExtra("docId");
        Log.d(">>>>>>>>> Time ", java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()));
        FireStore.getChannelChat(documentId, list -> Log.d("Chat Activity: ", list.size() + ""));
        btnSendMessage.setOnClickListener(view -> sendMessage());
    }

    void sendMessage() {
        String msg = editText.getText().toString().trim();
        if (msg.isEmpty()) {
            CommonFunctions.showToast("Type something!", getApplicationContext());
            return;
        }
        Chat chat = new Chat(msg, CommonFunctions.CurrentTime(), userData.getUserId(), userData.getFirstName());
        FireStore.sendMessage(documentId, chat, new OnResult() {
            @Override
            public void onComplete() {
                //add to list and notify
            }

            @Override
            public void onFailure() {
                //show toast
            }
        });
    }

}