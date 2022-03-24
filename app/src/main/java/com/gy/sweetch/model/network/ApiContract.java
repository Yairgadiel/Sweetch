package com.gy.sweetch.model.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * This class represents the remote API required
 */
public interface ApiContract {

    @GET
    Call<ResponseBody> downloadImage(@Url String url);
}