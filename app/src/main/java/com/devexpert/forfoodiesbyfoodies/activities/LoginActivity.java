package com.devexpert.forfoodiesbyfoodies.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.devexpert.forfoodiesbyfoodies.R;
import com.devexpert.forfoodiesbyfoodies.services.CustomSharedPreference;
import com.devexpert.forfoodiesbyfoodies.utils.CommonFunctions;
import com.devexpert.forfoodiesbyfoodies.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private EditText emailField, passwordField;
    private Button loginButton;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private TextView signUpTextView, forgetPasswordTextView;
    private CustomSharedPreference yourPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        yourPreference = CustomSharedPreference.getInstance(getApplicationContext());
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(view -> login());        //call login function when click ob login button
        //navigate to signUp activity
        signUpTextView.setOnClickListener(view -> startActivity(new Intent(this, SignUpActivity.class)));
        forgetPasswordTextView.setOnClickListener(view-> startActivity(new Intent(this, ForgetPasswordActivity.class)));
    }

    //initialize view
    void initView() {
        emailField = findViewById(R.id.emailField_id);
        passwordField = findViewById(R.id.passwordField_id);
        loginButton = findViewById(R.id.loginBtn_id);
        progressBar = findViewById(R.id.progressBar_id);
        signUpTextView = findViewById(R.id.signUpTextView_id);
        forgetPasswordTextView = findViewById(R.id.forgetPasswordTextView_id);
    }

    //Function for login
    void login() {
        CommonFunctions.hideKeyboard(this);
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        //check validity before login
        if (email.isEmpty() || password.isEmpty()) {
            CommonFunctions.showToast("Please fill the data properly.", getApplicationContext());
        } else {
            if (CommonFunctions.isEmailValid(email)) {

                if (password.length() >= 8) {
                    //login procedure
                    progressBar.setVisibility(View.VISIBLE);
                    //authenticate user
                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, task -> {
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    CommonFunctions.customLog("Login Error: " + task.getException().getMessage());
                                    CommonFunctions.showToast("Something went wrong.", getApplicationContext());
                                } else {
                                    yourPreference.saveData(Constants.userId, task.getResult().getUser().getUid());
                                    Intent intent = new Intent(LoginActivity.this, DashBoardActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });

                } else {
                    CommonFunctions.showToast("Password should contain at-least 8 characters.", getApplicationContext());
                }
            } else {
                CommonFunctions.showToast("Email is not valid", getApplicationContext());
            }
        }
    }

}