package me.gurpreetsk.task_jombay.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import io.realm.Realm;
import io.realm.RealmQuery;
import me.gurpreetsk.task_jombay.R;
import me.gurpreetsk.task_jombay.model.user.User;
import me.gurpreetsk.task_jombay.model.user.UserDetails;
import me.gurpreetsk.task_jombay.model.userProfile.UserProfile;
import me.gurpreetsk.task_jombay.rest.ApiClient;
import me.gurpreetsk.task_jombay.rest.ApiInterface;
import me.gurpreetsk.task_jombay.service.TokenService;
import me.gurpreetsk.task_jombay.utils.Constants;
import me.gurpreetsk.task_jombay.utils.RealmString;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    SharedPreferences preferences;

    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        FirebaseJobDispatcher dispatcher =
                new FirebaseJobDispatcher(new GooglePlayDriver(MainActivity.this));
        Job myJob = dispatcher.newJobBuilder()
                .setService(TokenService.class) // the JobService that will be called
                .setTag("token-tag")        // uniquely identifies the job
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(0, 6))//todo set
                .setLifetime(Lifetime.FOREVER)
                .setReplaceCurrent(false)
                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                .build();
        dispatcher.mustSchedule(myJob);


        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmQuery<UserDetails> query = realm.where(UserDetails.class);
                    getUserLessons(query.findAll().get(0).getCompanyIds().get(0).getString(),
                            query.findAll().get(0).getId());
                }
            });
        } finally {
            if (realm != null)
                realm.close();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}