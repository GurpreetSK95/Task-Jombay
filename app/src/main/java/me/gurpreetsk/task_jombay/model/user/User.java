package me.gurpreetsk.task_jombay.model.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by Gurpreet on 14/05/17.
 */

public class User extends RealmObject {

    @SerializedName("user")
    @Expose
    private UserDetails user;

    public User() {
    }

    public UserDetails getUserDetails() {
        return user;
    }

    public void setUser(UserDetails user) {
        this.user = user;
    }
}
