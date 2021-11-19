package com.devexpert.forfoodiesbyfoodies.interfaces;

public interface ImageUploadResult {
    void onUploadSuccess(String imageUrl);
    void onUploadFailure();
}
