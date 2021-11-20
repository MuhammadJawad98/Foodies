package com.devexpert.forfoodiesbyfoodies.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import com.devexpert.forfoodiesbyfoodies.R;
import com.devexpert.forfoodiesbyfoodies.models.Restaurant;
import com.devexpert.forfoodiesbyfoodies.models.Review;
import com.devexpert.forfoodiesbyfoodies.models.User;
import com.devexpert.forfoodiesbyfoodies.services.FireStore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class OtherUserProfileActivity extends AppCompatActivity {
    private ImageView userImageView;
    private EditText edtFirstName, edtLastName, edtEmail;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_profile);
        initView();
        Intent intent = getIntent();
        Review review = (Review) intent.getSerializableExtra("details");
        FireStore.db.collection("users").whereEqualTo("userId",
                review.getUserId()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Log.d("TAG", document.getId() + " => " + document.getData());
                    user = new User(document.getData().get("firstName").toString(),
                            document.getData().get("lastName").toString(),
                            document.getData().get("email").toString(),
                            document.getData().get("userId").toString(),
                            document.getData().get("imageUrl").toString(),
                            Boolean.parseBoolean(document.getData().get("user").toString()),
                            Boolean.parseBoolean(document.getData().get("critic").toString()),
                            Boolean.parseBoolean(document.getData().get("admin").toString()));
                }
                Picasso.get().load(user.getImageUrl()).fit().centerCrop().
                        placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.error_image).into(userImageView);

                edtFirstName.setText(user.getFirstName());
                edtLastName.setText(user.getLastName());
                edtEmail.setText(user.getEmail());
            } else {
                Log.d("TAG", "Error getting documents: ", task.getException());
            }
        });


    }

    void initView() {
        userImageView = findViewById(R.id.userImageView_id);
        edtFirstName = findViewById(R.id.nameEditText_id);
        edtLastName = findViewById(R.id.lastNameEditText_id);
        edtEmail = findViewById(R.id.emailEditText_id);
    }
}