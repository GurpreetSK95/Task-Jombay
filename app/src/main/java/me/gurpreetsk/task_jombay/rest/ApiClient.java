package me.gurpreetsk.task_jombay.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Gurpreet on 13/05/17.
 */

public class ApiClient {

    private static Retrofit retrofitInstance = null;
    private static final String BASE_URL = "";

    public static Retrofit getInstance() {
        if (retrofitInstance == null) {
            retrofitInstance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitInstance;
    }

}
