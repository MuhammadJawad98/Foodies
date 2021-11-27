package com.devexpert.forfoodiesbyfoodies.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.devexpert.forfoodiesbyfoodies.R;
import com.devexpert.forfoodiesbyfoodies.models.User;
import com.devexpert.forfoodiesbyfoodies.services.FireStore;
import com.devexpert.forfoodiesbyfoodies.services.CustomSharedPreference;
import com.devexpert.forfoodiesbyfoodies.utils.CommonFunctions;
import com.devexpert.forfoodiesbyfoodies.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;

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
    private final CustomSharedPreference yourPreference = CustomSharedPreference.getInstance(getApplicationContext());

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

    //initializing view
    void initView() {
        firstNameField = findViewById(R.id.firstNameField_id);
        lastNameField = findViewById(R.id.lastNameField_id);
        emailField = findViewById(R.id.emailField_id);
        passwordField = findViewById(R.id.passwordField_id);
        signUpButton = findViewById(R.id.signUpBtn_id);
        loginTextView = findViewById(R.id.loginTextView_id);
        progressBar = findViewById(R.id.progressBar_id);

    }

    //Function for signup
    void signUp() {
        String firstName = firstNameField.getText().toString().trim();
        String lastName = lastNameField.getText().toString().trim();
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        //check validity
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            CommonFunctions.showToast("Please fill the data properly.", getApplicationContext());

        } else {
            if (CommonFunctions.isEmailValid(email)) {

                if (password.length() >= 8) {
                    progressBar.setVisibility(View.VISIBLE);

                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this, task -> {
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    CommonFunctions.showToast("Login failed." + task.getException(), getApplicationContext());

                                } else {
                                    User user = new User(firstName, lastName, email, task.getResult().getUser().getUid(), password, Constants.defaultImageUrl,
                                            true, false, false);
                                    FireStore.addUserToFireStore(user);
                                    yourPreference.saveData(Constants.userId, task.getResult().getUser().getUid());


                                    CommonFunctions.showToast("Login Successful.", getApplicationContext());
                                    startActivity(new Intent(getApplicationContext(), DashBoardActivity.class));
                                    finish();
                                }
                            });

                    CommonFunctions.showToast("Successfully SignUp", getApplicationContext());

                } else {
                    CommonFunctions.showToast("Password should contain at-least 8 characters.", getApplicationContext());

                }
            } else {
                CommonFunctions.showToast("Email is not valid", getApplicationContext());


            }
        }
    }
}