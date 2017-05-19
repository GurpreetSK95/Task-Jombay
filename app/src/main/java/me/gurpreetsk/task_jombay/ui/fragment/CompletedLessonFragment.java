package me.gurpreetsk.task_jombay.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import me.gurpreetsk.task_jombay.R;
import me.gurpreetsk.task_jombay.model.userProfile.Lesson;
import me.gurpreetsk.task_jombay.model.userProfile.UserProfile;
import me.gurpreetsk.task_jombay.ui.adapter.CompletedAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompletedLessonFragment extends Fragment {

    //    @BindView(R.id.listview_completed)
//    ListView listView;
    @BindView(R.id.text)
    TextView textView;

    Realm realm = null;

    private static final String TAG = CompletedLessonFragment.class.getSimpleName();


    public CompletedLessonFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_completed_lesson, container, false);
        ButterKnife.bind(this, view);

        realm = Realm.getDefaultInstance();

        String text = "";
        RealmResults<Lesson> result = getLessons();
        try {
            for (int i = 0; i < result.size(); i++)
//                if (result.get(i).getStatus().equals("completed"))
                text += result.get(i).getLesson().getTitle() + "\n\n";
        } catch (Exception e) {
            text = "No Completed lessons found";
        }
        textView.setText(text);
        return view;
    }

    private RealmResults<Lesson> getLessons() {
        final List<RealmResults<Lesson>> results = new ArrayList<>();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmQuery<Lesson> query = realm.where(Lesson.class);
                results.add(0, query.equalTo("status", "completed").findAll());
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
