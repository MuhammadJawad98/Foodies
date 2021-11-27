package com.devexpert.forfoodiesbyfoodies.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.devexpert.forfoodiesbyfoodies.R;
import com.devexpert.forfoodiesbyfoodies.interfaces.ImageUploadResult;
import com.devexpert.forfoodiesbyfoodies.interfaces.OnResult;
import com.devexpert.forfoodiesbyfoodies.models.User;
import com.devexpert.forfoodiesbyfoodies.services.FireStore;
import com.devexpert.forfoodiesbyfoodies.services.CustomSharedPreference;
import com.devexpert.forfoodiesbyfoodies.utils.CommonFunctions;
import com.devexpert.forfoodiesbyfoodies.utils.Constants;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.InputStream;

public class ProfileFragment extends Fragment {
    private ImageView imageView;
    private EditText edtName, edtLastName, edtEmail, edtPassword;
    private User userData;
    private String imagePath = "";
    private Uri imageUri;
    FirebaseStorage storage;
    StorageReference storageReference;
    private ActivityResultLauncher<Intent> someActivityResultLauncher;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        imageView = view.findViewById(R.id.userImageView_id);
        edtName = view.findViewById(R.id.nameEditText_id);
        edtLastName = view.findViewById(R.id.lastNameEditText_id);
        edtEmail = view.findViewById(R.id.emailEditText_id);
        edtPassword = view.findViewById(R.id.passwordEditText_id);

        Button btnSubmit = view.findViewById(R.id.btnSubmit_id);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //fetching user id from local storage
        CustomSharedPreference sharedPreference = CustomSharedPreference.getInstance(getContext());
        String userId = sharedPreference.getData(Constants.userId);

        //gets data of user from firebase and sets into the fields
        FireStore.getData(userId, user -> {
            userData = user;
            CommonFunctions.customLog(userData.getDocumentId());
            edtEmail.setText(user.getEmail());
            edtName.setText(user.getFirstName());
            edtLastName.setText(user.getLastName());
            edtPassword.setText(user.getPassword());
            Picasso.get().load(user.getImageUrl()).fit().centerCrop().
                    placeholder(R.drawable.placeholder_image).error(R.drawable.error_image).into(imageView);
        });

        //when click on image, open the gallery to pick one image
        imageView.setOnClickListener(view12 -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            someActivityResultLauncher.launch(intent);
        });

        // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if (data == null) {
                            //error
                            return;
                        }
                        try {
                            Uri uri = data.getData();
                            System.out.println(data.getData() + "::::::::::::" + uri.getPath());
                            final InputStream imageStream = getActivity().getContentResolver().openInputStream(uri);
                            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                            imageView.setImageBitmap(selectedImage);
                            imagePath = uri.getPath();
                            imageUri = uri;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

        btnSubmit.setOnClickListener(view1 -> {
            String firstName = edtName.getText().toString().trim();
            String lastName = edtLastName.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            if (firstName.isEmpty()) {
                CommonFunctions.showToast("Please fill first name!", getContext());
                return;
            }
            if (lastName.isEmpty()) {
                CommonFunctions.showToast("Please fill last name!", getContext());
                return;
            }
            if (email.isEmpty() || !CommonFunctions.isEmailValid(email)) {
                CommonFunctions.showToast("Please fill email correctly!", getContext());
                return;
            }
            if (password.isEmpty() || password.length() < 8) {
                CommonFunctions.showToast("Please fill password correctly!", getContext());
                return;
            }
            if (imagePath != null && imageUri != null) {
                //upload image and then update user data
                CommonFunctions.uploadImage(imagePath, getContext(), imageUri, new ImageUploadResult() {
                    @Override
                    public void onUploadSuccess(String imageUrl) {
                        User user = new User(firstName, lastName, email, userData.getUserId(), password, imageUrl, userData.isUser(), userData.isCritic(), userData.isAdmin());

                        //update user data
                        FireStore.updateUserData(userData.getDocumentId(), user, new OnResult() {
                            @Override
                            public void onComplete() {
                                CommonFunctions.showToast("Data Update successfully", getContext());
                            }

                            @Override
                            public void onFailure() {
                                CommonFunctions.showToast("Error while updating user data", getContext());
                            }
                        });
                    }

                    @Override
                    public void onUploadFailure() {
                        CommonFunctions.showToast("Error while uploading image", getContext());
                    }
                });

            } else {
                //without image upload update data in firebase
                User user = new User(firstName, lastName, email, userData.getUserId(), password, userData.getImageUrl(), userData.isUser(), userData.isCritic(), userData.isAdmin());

                FireStore.updateUserData(userData.getDocumentId(), user, new OnResult() {
                    @Override
                    public void onComplete() {
                        CommonFunctions.showToast("Data save successfully", getContext());
                    }

                    @Override
                    public void onFailure() {
                        CommonFunctions.showToast("Something went wrong", getContext());
                    }
                });
            }
        });
        return view;
    }
}