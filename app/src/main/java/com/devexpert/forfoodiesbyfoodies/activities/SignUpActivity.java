package com.devexpert.forfoodiesbyfoodies.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.devexpert.forfoodiesbyfoodies.R;
import com.devexpert.forfoodiesbyfoodies.models.User;
import com.devexpert.forfoodiesbyfoodies.services.FireStore;
import com.devexpert.forfoodiesbyfoodies.utils.CommonFunctions;
import com.devexpert.forfoodiesbyfoodies.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SignUpActivity extends AppCompatActivity {

    private EditText firstNameField, lastNameField, emailField, passwordField;
    private Button signUpButton;
    private TextView loginTextView;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initView();
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        signUpButton.setOnClickListener(view -> signUp());
        loginTextView.setOnClickListener(view -> finish());
    }

    void initView() {
        firstNameField = findViewById(R.id.firstNameField_id);
        lastNameField = findViewById(R.id.lastNameField_id);
        emailField = findViewById(R.id.emailField_id);
        passwordField = findViewById(R.id.passwordField_id);
        signUpButton = findViewById(R.id.signUpBtn_id);
        loginTextView = findViewById(R.id.loginTextView_id);
        progressBar = findViewById(R.id.progressBar_id);

    }

    void signUp() {
        String firstName = firstNameField.getText().toString().trim();
        String lastName = lastNameField.getText().toString().trim();
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            CommonFunctions.showToast("Please fill the data properly.", getApplicationContext());

        } else {
            if (CommonFunctions.isEmailValid(email)) {

                if (password.length() >= 8) {
                    //do signUp
                    progressBar.setVisibility(View.VISIBLE);

                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this, task -> {
                                progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    CommonFunctions.showToast("Login failed." + task.getException(), getApplicationContext());

                                } else {
                                    //String firstName, String lastName, String email, String userId, String password, String imgaeUrl, boolean isUser, boolean isCritic, boolean isAdmin
                                    User user = new User(firstName, lastName, email, FireStore.getCurrentUserUUid(),password, Constants.defaultImageUrl,
                                    true, false, false);
                                    FireStore.addUserToFireStore(user);
                                    CommonFunctions.showToast("Login Successful.", getApplicationContext());
                                    startActivity(new Intent(getApplicationContext(), DashBoardActivity.class));
                                    finish();
                                }
                            });

                    CommonFunctions.showToast("Success Signup", getApplicationContext());

                } else {
                    CommonFunctions.showToast("Password should contain at-least 8 characters.", getApplicationContext());

                }
            } else {
                CommonFunctions.showToast("Email is not valid", getApplicationContext());


            }
        }
    }
}