package com.devexpert.forfoodiesbyfoodies.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.devexpert.forfoodiesbyfoodies.R;
import com.devexpert.forfoodiesbyfoodies.models.StreetFood;
import com.devexpert.forfoodiesbyfoodies.services.FireStore;
import com.devexpert.forfoodiesbyfoodies.utils.CommonFunctions;
import com.devexpert.forfoodiesbyfoodies.utils.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.util.UUID;

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
    FirebaseStorage storage;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_street_food);
        initView();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        userId = FireStore.getCurrentUserUUid();
        System.out.println("+++++++++++" + userId);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
            }
        });
        imageView.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Tack Image"), PICK_IMAGE);
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
            System.out.println("??????" + foodType);
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


        uploadImage(name, description, location);
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
//                File file = FileUtil.from(currentActivity, uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(String name, String description, String location) {

        if (imagePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
            ref.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            System.out.println("image Upload" + taskSnapshot.getStorage().getDownloadUrl().toString());
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                            StreetFood streetFood = new StreetFood(name, description, location, taskSnapshot.getStorage().getDownloadUrl().toString(), foodType, userId);
                            FireStore.addStreetFoodStall(streetFood, getApplicationContext());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(taskSnapshot -> {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                .getTotalByteCount());
                        progressDialog.setMessage("Uploaded " + (int) progress + "%");
                    });
        }
    }
}