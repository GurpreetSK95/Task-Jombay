package me.gurpreetsk.task_jombay.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import me.gurpreetsk.task_jombay.R;
import me.gurpreetsk.task_jombay.model.userProfile.UserProfile;

/**
 * A simple {@link Fragment} subclass.
 */
public class PendingLessonFragment extends Fragment {

    @BindView(R.id.textview_incomplete)
    TextView textView;

    Realm realm = null;

    private static final String TAG = PendingLessonFragment.class.getSimpleName();


    public PendingLessonFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pending_lesson, container, false);

        ButterKnife.bind(this, view);

        realm = Realm.getDefaultInstance();

        String text = "";
        RealmResults<UserProfile> result = getLessons();
        try {
            for (int i = 0; i < result.size(); i++)
                for (int j = 0; j < result.get(i).getUserLessons().size(); j++)
                    if (!result.get(i).getUserLessons().get(j).getStatus().equals("completed"))
                        text += result.get(i).getUserLessons().get(j).getLesson().getTitle() + "\n\n";
        } catch (Exception e) {
            text = "No Incomplete lessons found";
        }
        textView.setText(text);

        return view;
    }

    private RealmResults<UserProfile> getLessons() {
        final List<RealmResults<UserProfile>> results = new ArrayList<>();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmQuery<UserProfile> query = realm.where(UserProfile.class);
                results.add(0, query.findAll());
                Log.i(TAG, "execute: " + query.findAll());
            }
        });
        return results.get(0);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (realm != null)
            realm.close();
    }

}
