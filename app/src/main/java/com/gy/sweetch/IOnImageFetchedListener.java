package com.gy.sweetch;

import androidx.annotation.NonNull;

import com.gy.sweetch.model.ImageResult;

/**
 * An event representing an image fetch
 */
public interface IOnImageFetchedListener {
    void onImageFetched(@NonNull ImageResult imageResult);
}
