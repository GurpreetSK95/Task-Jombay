package me.gurpreetsk.task_jombay.model.userProfile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Gurpreet on 15/05/17.
 */

public class UserProfile extends RealmObject {

    @SerializedName("user_profile")
    @Expose
    private UserProfile userProfile;
    @PrimaryKey
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("user_lessons")
    @Expose
    private RealmList<Lesson> userLessons = null;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserProfile() {
    }

    public RealmList<Lesson> getUserLessons() {
        return userLessons;
    }

    public void setUserLessons(RealmList<Lesson> userLessons) {
        this.userLessons = userLessons;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }
}
