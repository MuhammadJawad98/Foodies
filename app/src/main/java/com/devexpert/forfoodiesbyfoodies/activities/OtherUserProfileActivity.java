package com.devexpert.forfoodiesbyfoodies.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import com.devexpert.forfoodiesbyfoodies.R;
import com.devexpert.forfoodiesbyfoodies.models.Review;
import com.devexpert.forfoodiesbyfoodies.models.User;
import com.devexpert.forfoodiesbyfoodies.services.FireStore;
import com.devexpert.forfoodiesbyfoodies.utils.CommonFunctions;
import com.devexpert.forfoodiesbyfoodies.utils.Constants;
import com.google.firebase.firestore.QueryDocumentSnapshot;
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
        Review review = (Review) intent.getSerializableExtra(Constants.details);
        FireStore.db.collection(Constants.rootCollectionUsers).whereEqualTo(Constants.userId,
                review.getUserId()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    user = new User(document.getData().get(Constants.firstName).toString(),
                            document.getData().get(Constants.lastName).toString(),
                            document.getData().get(Constants.email).toString(),
                            document.getData().get(Constants.userId).toString(),
                            document.getData().get(Constants.imageUrl).toString(),
                            Boolean.parseBoolean(document.getData().get(Constants.user).toString()),
                            Boolean.parseBoolean(document.getData().get(Constants.critic).toString()),
                            Boolean.parseBoolean(document.getData().get(Constants.admin).toString()));
                }
                Picasso.get().load(user.getImageUrl()).fit().centerCrop().
                        placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.error_image).into(userImageView);

                edtFirstName.setText(user.getFirstName());
                edtLastName.setText(user.getLastName());
                edtEmail.setText(user.getEmail());
            } else {
                CommonFunctions.customLog("Error getting documents: "+ task.getException());
            }
        });
    }

    //initializing view
    void initView() {
        userImageView = findViewById(R.id.userImageView_id);
        edtFirstName = findViewById(R.id.nameEditText_id);
        edtLastName = findViewById(R.id.lastNameEditText_id);
        edtEmail = findViewById(R.id.emailEditText_id);
    }
}