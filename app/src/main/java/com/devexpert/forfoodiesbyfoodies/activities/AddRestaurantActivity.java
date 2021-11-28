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

import com.devexpert.forfoodiesbyfoodies.R;
import com.devexpert.forfoodiesbyfoodies.interfaces.ImageUploadResult;
import com.devexpert.forfoodiesbyfoodies.services.FireStore;
import com.devexpert.forfoodiesbyfoodies.utils.CommonFunctions;

import java.io.InputStream;

public class AddRestaurantActivity extends AppCompatActivity {
    private ImageView imageView;
    private EditText edtRestaurantName, edtRestaurantDescp;
    private Button btnSubmit;
    private String imagePath = "";
    private Uri imageUri;
    private ActivityResultLauncher<Intent> someActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaurant);
        initView();

         // when click on image view it open the gallery to select an image
        imageView.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            someActivityResultLauncher.launch(intent);
        });

        //after selecting image from gallery this will return you the result (image path)
        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data == null) {
                            return;
                        }
                        try {
                            Uri uri = data.getData();
                            CommonFunctions.customLog(data.getData().toString());
                            CommonFunctions.customLog(uri.getPath());
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
        btnSubmit.setOnClickListener(view -> onSubmitData());

    }

    //initializing view
    void initView() {
        imageView = findViewById(R.id.userImageView_id);
        edtRestaurantName = findViewById(R.id.edtRestaurantName_id);
        edtRestaurantDescp = findViewById(R.id.edtRestaurantDesp_id);
        btnSubmit = findViewById(R.id.btnSubmit_id);
    }

    //First check data is filled and then upload data to firebase
    void onSubmitData() {
        String name = edtRestaurantName.getText().toString().trim();
        String description = edtRestaurantDescp.getText().toString().trim();
        if (name.isEmpty()) {
            CommonFunctions.showToast("Please add restaurant name", getApplicationContext());
            return;
        }
        if (description.isEmpty()) {
            CommonFunctions.showToast("Please add restaurant description", getApplicationContext());
            return;
        }
        if (imagePath.isEmpty()) {
            CommonFunctions.showToast("Please select any image", getApplicationContext());
            return;
        }
        CommonFunctions.uploadImage(imagePath, this, imageUri, new ImageUploadResult() {
            @Override
            public void onUploadSuccess(String imageUrl) {
                FireStore.addRestaurant(imageUrl, description, name);
                finish();
            }

            @Override
            public void onUploadFailure() {
                CommonFunctions.showToast("Something went wrong", getApplicationContext());
            }
        });
    }
}