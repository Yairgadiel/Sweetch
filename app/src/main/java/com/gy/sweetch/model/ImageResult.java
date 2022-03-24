package com.gy.sweetch.model;

import androidx.annotation.Nullable;

import java.io.File;

/**
 * This class represents result of an image received from the remote
 */
public class ImageResult {
    private File imageFile = null;
    private String errorMessage = null;

    public ImageResult(@Nullable File imageFile, @Nullable String errorMessage) {
        this.imageFile = imageFile;
        this.errorMessage = errorMessage;
    }

    public File getImageFile() {
        return imageFile;
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
