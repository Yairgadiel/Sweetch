package com.gy.sweetch.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.gy.sweetch.IOnImageFetchedListener;
import com.gy.sweetch.model.network.ApiService;
import com.gy.sweetch.utils.FileUtils;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This class handles on the functionality relevant for photos retrieval
 */
public class PhotoRepository {
    private static final String TAG = "PhotoRepo";

    private static final String FIRST_EXT = "125%402";
    private static final String SECOND_EXT = "127%402";

    /**
     * Executor for background work
     */
    private final ExecutorService executorService;

    /**
     * The path of the local directory in which the photos are saved
     */
    private String filesDirPath;

    /**
     * Single instance of the repository
     */
    private static PhotoRepository instance = null;

    private PhotoRepository() {
        executorService = Executors.newSingleThreadExecutor();
    }

    public static PhotoRepository getInstance() {
        if (instance == null) {
            instance = new PhotoRepository();
        }

        return instance;
    }

    /**
     * This method fetches a new image from the remote.
     * The method ensures that the same image can not appear twice in a row.
     * @param filesDir the path to the directory in which the images are saved.
     * @param imageFetchedListener a callback for retrieving the image.
     */
    public void fetchNewImage(@NonNull String filesDir, @NonNull IOnImageFetchedListener imageFetchedListener) {
        this.filesDirPath = filesDir;

        // Run on the background
        executorService.execute(() -> {
            // Determine what should the next extension be
            String ext = determineExtension();

            // Clear the files directory
            FileUtils.clearDir(filesDirPath);

            // Attempt to fetch the image
            Call<ResponseBody> call =
                    ApiService.getService().downloadImage(String.format(ApiService.IMAGE_URL_FORMAT, ext));

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (!response.isSuccessful() || response.body() == null) {
                        imageFetchedListener.onImageFetched(new ImageResult(null, "Unknown error!"));
                    }
                    else {
                        String imagePath = filesDirPath + File.separator + ext + ".zip";

                        if (!FileUtils.writeResponseBodyToDisk(response.body(), imagePath)) {
                            imageFetchedListener.onImageFetched(new ImageResult(null, "Unable to create image"));
                        }
                        else {
                            // Unzip the downloaded file
                            if (!FileUtils.unzip(filesDirPath + "/", ext + ".zip")) {
                                imageFetchedListener.onImageFetched(new ImageResult(null, "Unable to unzip"));
                            }
                            else {
                                File image = FileUtils.findImage(filesDirPath);

                                if (image == null) {
                                    imageFetchedListener.onImageFetched(new ImageResult(null, "No image in zip"));
                                }
                                else {
                                    imageFetchedListener.onImageFetched(new ImageResult(image, null));
                                }
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    Log.e(TAG, "onFailure: ", t);

                    imageFetchedListener.onImageFetched(new ImageResult(null, "Response failed"));
                }
            });
        });
    }

    /**
     * This method determines the "extension" of the file to fetch
     * @return String, an extension DIFFERENT from the current one
     */
    private String determineExtension() {
        String extension = FIRST_EXT;

        File filesDir = new File(filesDirPath);

        // Checking if file directory is valid
        if (!filesDir.exists() || !filesDir.isDirectory()) {
            Log.d(TAG, "findFile: files directory invalid");
        }
        else {
            File[] files = filesDir.listFiles();

            // If there are no files in the folder it means that this is the first run
            if (files == null || files.length == 0) {
                extension = FIRST_EXT;
            }
            else {
                // Looking for the zip file
                for (File file : files) {
                    if (file.getName().endsWith(".zip")) {
                        // Determine the extension to be the opposite of the current one
                        extension = file.getName().contains(FIRST_EXT) ? SECOND_EXT : FIRST_EXT;

                        break;
                    }
                }
            }
        }

        return extension;
    }
}
