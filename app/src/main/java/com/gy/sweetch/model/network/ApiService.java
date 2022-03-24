package com.gy.sweetch.model.network;

import retrofit2.Retrofit;

/**
 * This class is in charge of handling all remote API calls
 */
public class ApiService {
    public static final String BASE_URL =
            "https://test-assets-mobile.s3-us-west-2.amazonaws.com/";

    public static final String IMAGE_URL_FORMAT =
            "https://test-assets-mobile.s3-us-west-2.amazonaws.com/%s.zip";

    private static ApiContract service = null;

    /**
     * This method instantiates and returns the single instance of the API contract implementation
     */
    public static ApiContract getService() {
        if (service == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .build();

            service = retrofit.create(ApiContract.class);
        }

        return service;
    }
}
