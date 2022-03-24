package com.gy.sweetch.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gy.sweetch.R;
import com.gy.sweetch.model.ImageResult;
import com.gy.sweetch.viewModel.PhotoViewModel;

public class PhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView image = findViewById(R.id.image);
        ProgressBar loader = findViewById(R.id.loader);

        PhotoViewModel photoViewModel = new ViewModelProvider(this).get(PhotoViewModel.class);

        // Show loader
        loader.setVisibility(View.VISIBLE);

        // Attempt to get an image
        MutableLiveData<ImageResult> imageResult = photoViewModel.getImage(getFilesDir().getAbsolutePath());

        imageResult.observe(this, result -> {
            if (result != null) {
                // Checking if a message was not received successfully
                if (result.getImageFile() == null) {
                    // Show an error message
                    Toast.makeText(PhotoActivity.this,
                            getString(R.string.error_msg, result.getErrorMessage()),
                            Toast.LENGTH_LONG).show();
                }
                else {
                    // Load the image
                    image.setImageURI(Uri.fromFile(result.getImageFile()));
                }
                // Hide loader
                loader.setVisibility(View.INVISIBLE);
            }
        });
    }
}