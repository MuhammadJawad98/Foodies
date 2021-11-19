package com.devexpert.forfoodiesbyfoodies.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.devexpert.forfoodiesbyfoodies.R;
import com.devexpert.forfoodiesbyfoodies.interfaces.FirebaseUserDataResult;
import com.devexpert.forfoodiesbyfoodies.interfaces.ImageUploadResult;
import com.devexpert.forfoodiesbyfoodies.interfaces.OnResult;
import com.devexpert.forfoodiesbyfoodies.models.User;
import com.devexpert.forfoodiesbyfoodies.services.FireStore;
import com.devexpert.forfoodiesbyfoodies.utils.CommonFunctions;
import com.google.android.gms.common.internal.service.Common;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.InputStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    private ImageView imageView;
    private EditText edtName, edtLastName, edtEmail, edtPassword;
    private Button btnSubmit;
    private TextView userTypeText, tvRatingValue;
    private RatingBar ratingBar;
    private User userData;
    private String imagePath = "";
    private Uri imageUri;
    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;
    private ActivityResultLauncher<Intent> someActivityResultLauncher;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        imageView = view.findViewById(R.id.userImageView_id);
        edtName = view.findViewById(R.id.nameEditText_id);
        edtLastName = view.findViewById(R.id.lastNameEditText_id);
        edtEmail = view.findViewById(R.id.emailEditText_id);
        edtPassword = view.findViewById(R.id.passwordEditText_id);

        btnSubmit = view.findViewById(R.id.btnSubmit_id);
        userTypeText = view.findViewById(R.id.userType_id);
        tvRatingValue = view.findViewById(R.id.ratingValueText_id);
        ratingBar = view.findViewById(R.id.review_id);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        FireStore.getData(user -> {
            userData = user;
            System.out.println("document id::: " + userData.getDocumentId());
            edtEmail.setText(user.getEmail());
            edtName.setText(user.getFirstName());
            edtLastName.setText(user.getLastName());
            edtPassword.setText(user.getPassword());
            Picasso.get().load(user.getImageUrl()).fit().centerCrop().
                    placeholder(R.drawable.placeholder_image).error(R.drawable.error_image).into(imageView);
        });
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
                CommonFunctions.uploadImage(imagePath, getContext(), imageUri, new ImageUploadResult() {
                    @Override
                    public void onUploadSuccess(String imageUrl) {
                        User user = new User(firstName, lastName, email, userData.getUserId(), password, imageUrl, userData.isUser(), userData.isCritic(), userData.isAdmin());

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
                User user = new User(firstName, lastName, email, userData.getUserId(), password, userData.getImageUrl(), userData.isUser(), userData.isCritic(), userData.isAdmin());
                FireStore.updateUserData(userData.getDocumentId(), user, new OnResult() {
                    @Override
                    public void onComplete() {
                        System.out.println(">>>>>>>>>>>> success");
                        CommonFunctions.showToast("dat save successfully", getContext());
                    }

                    @Override
                    public void onFailure() {
                        System.out.println(">>>>>>>>>>>> failure");
                        CommonFunctions.showToast("Something went wrong", getContext());
                    }
                });
            }
        });
        return view;
    }
}