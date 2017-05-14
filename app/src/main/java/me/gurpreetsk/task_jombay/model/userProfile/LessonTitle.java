package me.gurpreetsk.task_jombay.model.userProfile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by Gurpreet on 15/05/17.
 */

public class LessonTitle extends RealmObject {

    @SerializedName("title")
    @Expose
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
