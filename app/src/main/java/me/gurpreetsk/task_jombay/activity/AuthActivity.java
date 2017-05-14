package me.gurpreetsk.task_jombay.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmList;
import me.gurpreetsk.task_jombay.R;
import me.gurpreetsk.task_jombay.model.Token;
import me.gurpreetsk.task_jombay.model.user.User;
import me.gurpreetsk.task_jombay.model.user.UserDetails;
import me.gurpreetsk.task_jombay.rest.ApiClient;
import me.gurpreetsk.task_jombay.rest.ApiInterface;
import me.gurpreetsk.task_jombay.utils.Constants;
import me.gurpreetsk.task_jombay.utils.CustomTypeAdapter;
import me.gurpreetsk.task_jombay.utils.RealmListConverter;
import me.gurpreetsk.task_jombay.utils.RealmString;
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

    private static final String TAG = AuthActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(AuthActivity.this);
    }

    @OnClick(R.id.button_login)
    public void loginUser() {
        //TODO: check internet connectivity
        if (!TextUtils.isEmpty(edittextUserEmail.getText().toString())
                && Patterns.EMAIL_ADDRESS.matcher(edittextUserEmail.getText().toString()).matches()
                && !TextUtils.isEmpty(edittextUserPassword.getText().toString())) {
            ApiInterface apiService = ApiClient.getInstance().create(ApiInterface.class);
            Call<Token> call = apiService.authenticateUser(edittextUserEmail.getText().toString(),
                    edittextUserPassword.getText().toString(),
                    "password",
                    "user");
            call.enqueue(new Callback<Token>() {
                @Override
                public void onResponse(Call<Token> call, Response<Token> response) {
                    try {
                        Log.i(TAG, "onResponse: " + response.body().getAccessToken());
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt(Constants.CREATED_AT, response.body().getCreatedAt());
                        editor.putInt(Constants.EXPIRES_IN, response.body().getExpiresIn());
                        editor.putString(Constants.ACCESS_TOKEN, response.body().getAccessToken());
                        editor.putString(Constants.REFRESH_TOKEN, response.body().getRefreshToken());
                        editor.putString(Constants.SCOPE, response.body().getScope());
                        editor.putString(Constants.TOKEN_TYPE, response.body().getTokenType());
                        editor.commit();
                        getUserDetails();
                    } catch (NullPointerException e) {
                        Log.e(TAG, "onResponse: ", e);
                        Toast.makeText(AuthActivity.this, "Username or password is invalid",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Token> call, Throwable t) {
                    Log.e(TAG, "onFailure: " + t.toString());
                    Toast.makeText(AuthActivity.this, "Failed to login", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Please fill all information", Toast.LENGTH_SHORT).show();
        }
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
                            }
                        });
                    } finally {
                        if (realm != null)
                            realm.close();
                    }
                    startActivity(new Intent(AuthActivity.this, MainActivity.class));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                Toast.makeText(AuthActivity.this, "Couldn't fetch User details", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
