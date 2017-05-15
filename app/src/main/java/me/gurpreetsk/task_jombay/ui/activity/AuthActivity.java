package me.gurpreetsk.task_jombay.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmQuery;
import me.gurpreetsk.task_jombay.R;
import me.gurpreetsk.task_jombay.model.Auth;
import me.gurpreetsk.task_jombay.model.user.User;
import me.gurpreetsk.task_jombay.model.user.UserDetails;
import me.gurpreetsk.task_jombay.model.userProfile.UserProfile;
import me.gurpreetsk.task_jombay.rest.ApiClient;
import me.gurpreetsk.task_jombay.rest.ApiInterface;
import me.gurpreetsk.task_jombay.service.TokenService;
import me.gurpreetsk.task_jombay.utils.Constants;
import me.gurpreetsk.task_jombay.utils.NetworkConnnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends AppCompatActivity {

    @BindView(R.id.edittext_user_email)
    MaterialEditText edittextUserEmail;
    @BindView(R.id.edittext_user_password)
    MaterialEditText edittextUserPassword;
    @BindView(R.id.button_login)
    Button buttonLogin;

    SharedPreferences preferences;
    ProgressDialog progressDialog = null;

    private static final String TAG = AuthActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(AuthActivity.this);
        if (!preferences.getBoolean(Constants.IS_LOGGED_IN, false)) {
            setContentView(R.layout.activity_auth);
            ButterKnife.bind(this);
        } else
            startActivity(new Intent(AuthActivity.this, MainActivity.class));
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (progressDialog != null)
            progressDialog.dismiss();
        finish();
    }

    @OnClick(R.id.button_login)
    public void loginUser() {
        if (NetworkConnnection.isNetworkConnected(AuthActivity.this)) {
            if (!TextUtils.isEmpty(edittextUserEmail.getText().toString())
                    && Patterns.EMAIL_ADDRESS.matcher(edittextUserEmail.getText().toString()).matches()
                    && !TextUtils.isEmpty(edittextUserPassword.getText().toString())) {
                progressDialog = new ProgressDialog(AuthActivity.this);
                progressDialog.setMessage("Logging in");
                progressDialog.show();
                ApiInterface apiService = ApiClient.getInstance().create(ApiInterface.class);
                Call<Auth> call = apiService.authenticateUser(edittextUserEmail.getText().toString(),
                        edittextUserPassword.getText().toString(),
                        "password",
                        "user");
                call.enqueue(new Callback<Auth>() {
                    @Override
                    public void onResponse(Call<Auth> call, Response<Auth> response) {
                        try {
                            Log.i(TAG, "onResponse: " + response.body().getAccessToken());
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putInt(Constants.CREATED_AT, response.body().getCreatedAt());
                            editor.putInt(Constants.EXPIRES_IN, response.body().getExpiresIn());
                            editor.putString(Constants.ACCESS_TOKEN, response.body().getAccessToken());
                            editor.putString(Constants.REFRESH_TOKEN, response.body().getRefreshToken());
                            editor.putString(Constants.SCOPE, response.body().getScope());
                            editor.putString(Constants.TOKEN_TYPE, response.body().getTokenType());
                            editor.putBoolean(Constants.IS_LOGGED_IN, true);
                            editor.commit();
                            getUserDetails();
                            setupJob();
                        } catch (NullPointerException e) {
                            Log.e(TAG, "onResponse: ", e);
                            Toast.makeText(AuthActivity.this, "Username or password is invalid",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<Auth> call, Throwable t) {
                        Log.e(TAG, "onFailure: " + t.toString());
                        progressDialog.dismiss();
                        Toast.makeText(AuthActivity.this, "Failed to login", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "Please fill all information", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Check internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    private void setupJob() {
        FirebaseJobDispatcher dispatcher =
                new FirebaseJobDispatcher(new GooglePlayDriver(AuthActivity.this));
        Job myJob = dispatcher.newJobBuilder()
                .setService(TokenService.class)
                .setTag("token-tag")
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(600, 900))
                .setLifetime(Lifetime.FOREVER)
                .setReplaceCurrent(false)
                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                .build();
        dispatcher.mustSchedule(myJob);
    }

    private void getUserDetails() {
        ApiInterface service = ApiClient.getInstance().create(ApiInterface.class);
        String auth = String.format("%s %s", preferences.getString(Constants.TOKEN_TYPE, ""),
                preferences.getString(Constants.ACCESS_TOKEN, ""));
        Log.i(TAG, "getUserDetails: " + auth);
        Call<User> call = service.getUser(auth);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    final UserDetails details = response.body().getUserDetails();
                    Log.i(TAG, "onResponse: " + details.getId());
                    Realm realm = null;
                    try {
                        realm = Realm.getDefaultInstance();
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                try {
                                    realm.copyToRealm(details);
                                } catch (Exception e) {
                                    // entry already exists
                                    e.printStackTrace();
                                }
                                getUserLessons(details.getCompanyIds().get(0).getString(),
                                        details.getId());
                            }
                        });
                    } finally {
                        if (realm != null)
                            realm.close();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                if (progressDialog != null)
                    progressDialog.dismiss();
                Toast.makeText(AuthActivity.this, "Couldn't fetch User details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserLessons(String companyId, String userId) {
        ApiInterface service = ApiClient.getInstance().create(ApiInterface.class);
        String auth = String.format("%s %s", preferences.getString(Constants.TOKEN_TYPE, ""),
                preferences.getString(Constants.ACCESS_TOKEN, ""));
        Log.i(TAG, "getUserDetails: " + auth);
        Call<UserProfile> call = service.getUserProfile(auth, companyId, userId);
        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                Log.i(TAG, "onResponse: " + response.code());
                if (response.code() == 200) {
                    final UserProfile profile = response.body().getUserProfile();
                    Realm realm = null;
                    try {
                        realm = Realm.getDefaultInstance();
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                try {
                                    realm.copyToRealm(profile);
                                } catch (Exception e) {
                                    // entry already exists
                                    e.printStackTrace();
                                }
                            }
                        });
                    } finally {
                        if (realm != null)
                            realm.close();
                    }
                    Log.i(TAG, "onResponse: " +
                            response.body().getUserProfile().getUserLessons());
                    startActivity(new Intent(AuthActivity.this, MainActivity.class));
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                if (progressDialog != null)
                    progressDialog.dismiss();
            }
        });
    }

}
