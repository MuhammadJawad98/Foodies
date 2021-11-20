package com.devexpert.forfoodiesbyfoodies.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.devexpert.forfoodiesbyfoodies.R;
import com.devexpert.forfoodiesbyfoodies.interfaces.ImageUploadResult;
import com.devexpert.forfoodiesbyfoodies.interfaces.OnResult;
import com.devexpert.forfoodiesbyfoodies.models.StreetFood;
import com.devexpert.forfoodiesbyfoodies.services.FireStore;
import com.devexpert.forfoodiesbyfoodies.utils.CommonFunctions;
import com.devexpert.forfoodiesbyfoodies.utils.Constants;

import java.io.InputStream;

public class AddStreetFoodActivity extends AppCompatActivity {
    private ImageView imageView;
    private EditText edtName, edtDescription, edtLocation;
    private RadioGroup radioGroup;
    private String foodType = Constants.vegetarian;
    private String imagePath = "";
    private static final int PICK_IMAGE = 1;
    private Button btnSubmit;
    private String userId = "";
    private Uri imageUri;
    //Firebase
    private ActivityResultLauncher<Intent> someActivityResultLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_street_food);
        initView();
        userId = FireStore.getCurrentUserUUid();
        System.out.println("userId::: " + userId);
        btnSubmit.setOnClickListener(view -> submitData());
        imageView.setOnClickListener(view -> {
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
                            final InputStream imageStream = getContentResolver().openInputStream(uri);
                            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                            imageView.setImageBitmap(selectedImage);
                            imagePath = uri.getPath();
                            imageUri = uri;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radioVege:
                    foodType = Constants.vegetarian;
                    break;
                case R.id.radioNonVege:
                    foodType = Constants.no_vegetarian;
                    break;
            }
            System.out.println("foodType: " + foodType);
        });

    }

    void initView() {
        imageView = findViewById(R.id.image_id);
        edtName = findViewById(R.id.edtName_id);
        edtDescription = findViewById(R.id.edtDescription_id);
        edtLocation = findViewById(R.id.edtLocation_id);
        radioGroup = findViewById(R.id.radioGroup);
        btnSubmit = findViewById(R.id.submitStreetFooBtn_id);
    }

    void submitData() {
        String name = edtName.getText().toString();
        String description = edtDescription.getText().toString();
        String location = edtLocation.getText().toString();

        if (name.isEmpty()) {
            CommonFunctions.showToast("Please fill the name", getApplicationContext());
            return;
        }

        if (description.isEmpty()) {
            CommonFunctions.showToast("Please fill the description", getApplicationContext());
            return;
        }
        if (location.isEmpty()) {
            CommonFunctions.showToast("Please fill the location", getApplicationContext());
            return;
        }
        if (imagePath.isEmpty()) {
            CommonFunctions.showToast("Please select some picture", getApplicationContext());
            return;
        }

        FireStore.db.collection("street_food").whereEqualTo("name", name).addSnapshotListener((value, error) -> {
            if (value.getDocuments().size() > 0) {
                Toast.makeText(getApplicationContext(), "Already exist", Toast.LENGTH_SHORT).show();

            } else {
                uploadImage(name, description, location);

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //error
                return;
            }
            try {
                Uri uri = data.getData();
                System.out.println(data.getData() + "::::::::::::" + uri.getPath());
                final InputStream imageStream = getContentResolver().openInputStream(uri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imageView.setImageBitmap(selectedImage);
                imagePath = uri.getPath();
                imageUri = uri;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(String name, String description, String location) {

        CommonFunctions.uploadImage(imagePath, getApplicationContext(), imageUri, new ImageUploadResult() {
            @Override
            public void onUploadSuccess(String imageUrl) {
                StreetFood streetFood = new StreetFood(name, description, location, imageUrl, foodType, userId);
                FireStore.addStreetFoodStall(streetFood, new OnResult() {
                    @Override
                    public void onComplete() {
                        finish();
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(getApplicationContext(), "Error while uploading data", Toast.LENGTH_SHORT).show();

                    }
                });
            }

            @Override
            public void onUploadFailure() {
                CommonFunctions.showToast("Error while uploading image", getApplicationContext());
            }
        });
    }
}