package me.gurpreetsk.task_jombay.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import io.realm.RealmList;
import me.gurpreetsk.task_jombay.utils.CustomTypeAdapter;
import me.gurpreetsk.task_jombay.utils.RealmString;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Gurpreet on 13/05/17.
 */

public class ApiClient {

    private static Retrofit retrofitInstance = null;
    private static final String BASE_URL = "https://api.es-q.co/";

    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(new TypeToken<RealmList<RealmString>>() {
                    }.getType(),
                    CustomTypeAdapter.INSTANCE)
            .create();

    public static Retrofit getInstance() {
        if (retrofitInstance == null) {
            retrofitInstance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofitInstance;
    }

}
