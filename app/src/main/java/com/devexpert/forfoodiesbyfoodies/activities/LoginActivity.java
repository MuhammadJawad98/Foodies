package com.devexpert.forfoodiesbyfoodies.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.devexpert.forfoodiesbyfoodies.R;
import com.devexpert.forfoodiesbyfoodies.utils.CommonFunctions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import android.util.Log;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(view -> login());
        signUpTextView.setOnClickListener(view -> startActivity(new Intent(this, SignUpActivity.class)));

    }

    void initView() {
        emailField = findViewById(R.id.emailField_id);
        passwordField = findViewById(R.id.passwordField_id);
        loginButton = findViewById(R.id.loginBtn_id);
        progressBar = findViewById(R.id.progressBar_id);
        signUpTextView = findViewById(R.id.signUpTextView_id);
        forgetPasswordTextView = findViewById(R.id.forgetPasswordTextView_id);
    }

    void login() {
        CommonFunctions.hideKeyboard(this);
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();


        if (email.isEmpty() || password.isEmpty()) {
            CommonFunctions.showToast("Please fill the data properly.", getApplicationContext());
        } else {
            if (CommonFunctions.isEmailValid(email)) {

                if (password.length() >= 8) {
                    //login procedure
                    progressBar.setVisibility(View.VISIBLE);
                    //authenticate user
                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    progressBar.setVisibility(View.GONE);
                                    if (!task.isSuccessful()) {
                                        // there was an error
                                        Log.d("Login Error:", task.getException().getMessage());
                                        CommonFunctions.showToast("Something went wrong.", getApplicationContext());
                                    } else {
                                        Intent intent = new Intent(LoginActivity.this, DashBoardActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
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