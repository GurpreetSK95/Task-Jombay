package me.gurpreetsk.task_jombay.model.userProfile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import io.realm.RealmObject;

/**
 * Created by Gurpreet on 15/05/17.
 */

public class Lesson extends RealmObject {

    @SerializedName("lesson_id")
    @Expose
    private String lessonId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("lesson")
    @Expose
    private LessonTitle lessonTitle;

    public Lesson() {
    }

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LessonTitle getLesson() {
        return lessonTitle;
    }

    public void setLesson(LessonTitle lessonTitle) {
        this.lessonTitle = lessonTitle;
    }
}
