package me.gurpreetsk.task_jombay.utils;

import io.realm.RealmObject;

/**
 * Created by Gurpreet on 15/05/17.
 */

public class RealmString extends RealmObject {

    private String string;

    public RealmString() {
    }

    public RealmString(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
