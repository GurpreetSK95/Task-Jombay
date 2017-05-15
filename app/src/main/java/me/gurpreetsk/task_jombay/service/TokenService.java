package me.gurpreetsk.task_jombay.service;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import me.gurpreetsk.task_jombay.model.Auth;
import me.gurpreetsk.task_jombay.rest.ApiClient;
import me.gurpreetsk.task_jombay.rest.ApiInterface;
import me.gurpreetsk.task_jombay.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gurpreet on 15/05/17.
 */

public class TokenService extends JobService {

    SharedPreferences preferences;

    private static final String TAG = TokenService.class.getSimpleName();


    @Override
    public boolean onStartJob(JobParameters job) {
//        ApiInterface service = ApiClient.getInstance().create(ApiInterface.class);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
//        Call<Auth> call = service.getNewToken(preferences.getString(Constants.ACCESS_TOKEN, ""),
//                "refresh_token",
//                "user");
//        call.enqueue(new Callback<Auth>() {
//            @Override
//            public void onResponse(Call<Auth> call, Response<Auth> response) {
//                Log.i(TAG, "onResponse: " + response.code());
        long nowTimestamp = System.currentTimeMillis() / 1000;
        long originalTS = preferences.getInt(Constants.CREATED_AT, 0);
        Log.i(TAG, "onResponse: " + (nowTimestamp - originalTS));
        if (nowTimestamp - originalTS > 3600) {
            preferences.edit().clear().apply();
        }
//            }

//            @Override
//            public void onFailure(Call<Auth> call, Throwable t) {
//                Log.i(TAG, "onFailure: " + t.toString());
//            }
//        });

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return true;
    }
}
