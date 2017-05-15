package me.gurpreetsk.task_jombay.ui.adapter;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.TextView;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;
import me.gurpreetsk.task_jombay.R;
import me.gurpreetsk.task_jombay.model.userProfile.UserProfile;

/**
 * Created by Gurpreet on 15/05/17.
 */

public class CompletedAdapter extends RealmBaseAdapter<UserProfile> implements ListAdapter {

    private RealmResults<UserProfile> data;

    public CompletedAdapter(RealmResults<UserProfile> data) {
        super(data);
        this.data = data;
    }

    private static class ViewHolder {
        TextView title;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_recyclerview_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.title.setText(data.get(0).getUserLessons().get(position).getLesson().getTitle());
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (adapterData != null) {
            viewHolder.title.setText(data.get(0).getUserLessons().get(position).getLesson().getTitle());
        }
        return convertView;
    }
}