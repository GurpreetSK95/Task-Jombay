package me.gurpreetsk.task_jombay.utils;

import android.nfc.Tag;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.List;

import io.realm.RealmList;
import me.gurpreetsk.task_jombay.model.user.UserDetails;

/**
 * Created by Gurpreet on 15/05/17.
 */

public class RealmListConverter implements JsonSerializer<List<String>>,
        JsonDeserializer<RealmList<UserDetails>> {

    @Override
    public JsonElement serialize(List<String> src, Type typeOfSrc,
                                 JsonSerializationContext context) {
        JsonArray ja = new JsonArray();
        for (String tag : src) {
            ja.add(context.serialize(tag));
        }
        return ja;
    }

    @Override
    public RealmList<UserDetails> deserialize(JsonElement json, Type typeOfT,
                                              JsonDeserializationContext context)
            throws JsonParseException {
        RealmList<UserDetails> tags = new RealmList<>();
        JsonArray ja = json.getAsJsonArray();
        for (JsonElement je : ja) {
            tags.add((UserDetails) context.deserialize(je, UserDetails.class));
        }
        return tags;
    }

}