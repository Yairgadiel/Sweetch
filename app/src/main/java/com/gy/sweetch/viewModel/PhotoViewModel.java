package com.gy.sweetch.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gy.sweetch.model.ImageResult;
import com.gy.sweetch.model.PhotoRepository;

public class PhotoViewModel extends ViewModel {
    private final MutableLiveData<ImageResult> image = new MutableLiveData<>(null);

    /**
     * Get the recent image result. If there isn't one - fetch
     * @param imageDir the directory in which the image should be saved
     * @return the image result
     */
    public MutableLiveData<ImageResult> getImage(String imageDir) {
       // Checking if an image was already fetched
        if (image.getValue() == null) {
            // No image was fetched on this run - fetch now
            PhotoRepository.getInstance().fetchNewImage(imageDir, image::postValue);
        }

        return image;
    }
}
