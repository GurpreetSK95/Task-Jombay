package me.gurpreetsk.task_jombay.rest;

import me.gurpreetsk.task_jombay.model.Token;
import me.gurpreetsk.task_jombay.model.user.User;
import me.gurpreetsk.task_jombay.model.userProfile.UserProfile;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Gurpreet on 13/05/17.
 */

public interface ApiInterface {

    @POST("/oauth/token.json")
    @FormUrlEncoded
    Call<Token> authenticateUser(
            @Field("username") String username,
            @Field("password") String password,
            @Field("grant_type") String grantType,
            @Field("scope") String scope
    );

    @GET("/users/current.json")
    Call<User> getUser(@Header("Authorization") String authorization);

    @GET("/companies/{company_id}/sq/users/{user_id}/user_profile?" +
            "include[user_lessons][only][]=status" +
            "&include[user_lessons][include][lesson][only]=title" +
            "&include[user_lessons][only][]=lesson_id&select=_id")
    Call<UserProfile> getUserProfile(@Header("Authorization") String authorization,
                                     @Path("company_id") String companyId,
                                     @Path("user_id") String userId);


}
