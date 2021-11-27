package com.devexpert.forfoodiesbyfoodies.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.devexpert.forfoodiesbyfoodies.interfaces.ImageUploadResult;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class CommonFunctions {

    //Function for checking email validity
    public static boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    //Function for showing toast
    public static void showToast(String message, Context context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    //Function for hiding keyboard
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    //Function for uploading image to firebase storage
    public static void uploadImage(String imagePath, Context context, Uri imageUri, ImageUploadResult imageUploadResult) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        try {
            if (imagePath != null) {
                final ProgressDialog progressDialog = new ProgressDialog(context);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();

                StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
                ref.putFile(imageUri)
                        .addOnSuccessListener(taskSnapshot -> {
                            if (taskSnapshot.getMetadata() != null) {
                                if (taskSnapshot.getMetadata().getReference() != null) {
                                    Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                    result.addOnSuccessListener(uri -> {
                                        String imageUrl = uri.toString();
                                        CommonFunctions.customLog("Image Upload" + imageUrl);
                                        progressDialog.dismiss();
                                        imageUploadResult.onUploadSuccess(imageUrl);
                                    });
                                }
                            }

                        })
                        .addOnFailureListener(e -> {
                            progressDialog.dismiss();
                            imageUploadResult.onUploadFailure();

                            CommonFunctions.showToast("Failed " + e.getMessage(), context);
                        })
                        .addOnProgressListener(taskSnapshot -> {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        });
            }
        } catch (Exception e) {
            CommonFunctions.customLog("Error while uploading image" + e.getMessage());
        }

    }

    //Function for getting current date time for messages
    public static Date CurrentDateTime() {
        return new Date();
    }

    //Function for converting date object into specific time format (messages)
    public static String convertTime(Date date) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        return dateFormat.format(date);
    }

    //Function for logs
    public static void customLog(String message) {
        Log.d("FFBF Log: ", message);
    }
}
