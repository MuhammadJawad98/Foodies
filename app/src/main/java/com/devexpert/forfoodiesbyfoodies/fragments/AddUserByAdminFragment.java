package com.devexpert.forfoodiesbyfoodies.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.devexpert.forfoodiesbyfoodies.R;
import com.devexpert.forfoodiesbyfoodies.models.User;
import com.devexpert.forfoodiesbyfoodies.services.FireStore;
import com.devexpert.forfoodiesbyfoodies.utils.CommonFunctions;
import com.devexpert.forfoodiesbyfoodies.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;


public class AddUserByAdminFragment extends Fragment {

    private EditText edtFirstName, edtLastName, edtEmail, edtPassword;
    private boolean isCritic = true;
    private boolean isAdmin = false;
    private FirebaseAuth auth;

    public AddUserByAdminFragment() {
        //  empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_user_by_admin, container, false);
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        edtFirstName = view.findViewById(R.id.firstNameField_id);
        edtLastName = view.findViewById(R.id.lastNameField_id);
        edtEmail = view.findViewById(R.id.emailField_id);
        edtPassword = view.findViewById(R.id.passwordField_id);
        RadioGroup radioGroup = view.findViewById(R.id.radioGroupAddUser_id);
        Button btnSubmit = view.findViewById(R.id.signUpBtn_id);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radioCritic:
                    isCritic = true;
                    isAdmin = false;
                    break;
                case R.id.radioAdmin:
                    isAdmin = true;
                    isCritic = false;
                    break;
            }
            CommonFunctions.customLog("isCritics: " + isCritic + " isAdmin: " + isAdmin);
        });
        btnSubmit.setOnClickListener(view1 -> createAnAccount());       //create an account

        return view;
    }

    //Function for creating an account
    void createAnAccount() {
        String firstName = edtFirstName.getText().toString().trim();
        String lastName = edtLastName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            CommonFunctions.showToast("Please fill the data properly.", getContext());
        } else {
            if (CommonFunctions.isEmailValid(email)) {

                if (password.length() >= 8) {
                    //do signUp
                    final ProgressDialog progressDialog = new ProgressDialog(getContext());
                    progressDialog.setTitle("Creating an account please wait!");
                    progressDialog.show();

                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                        progressDialog.cancel();
                        if (!task.isSuccessful()) {
                            CommonFunctions.showToast("Error while creating account!", getContext());
                        } else {
                            User user = new User(firstName, lastName, email, task.getResult().getUser().getUid(),
                                    password, Constants.defaultImageUrl, false, isCritic, isAdmin);
                            FireStore.addUserToFireStore(user);
                            edtFirstName.setText("");
                            edtLastName.setText("");
                            edtEmail.setText("");
                            edtPassword.setText("");
                            CommonFunctions.showToast("Create account Successful.", getContext());

                        }
                    }).addOnFailureListener(e -> {
                        progressDialog.cancel();
                        CommonFunctions.showToast("Error while creating account!", getContext());

                    });
                } else {
                    CommonFunctions.showToast("Password should contain at-least 8 characters.", getContext());
                }
            } else {
                CommonFunctions.showToast("Email is not valid", getContext());
            }
        }
    }
}