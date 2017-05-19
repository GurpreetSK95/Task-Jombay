package me.gurpreetsk.task_jombay.rest;

import me.gurpreetsk.task_jombay.model.Auth;
import me.gurpreetsk.task_jombay.model.user.User;
import me.gurpreetsk.task_jombay.model.userProfile.UserProfile;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Gurpreet on 13/05/17.
 */

public interface ApiInterface {

    @POST("/oauth/token.json")
    @FormUrlEncoded
    Call<Auth> authenticateUser(
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
//    @GET("https://api.es-q.co/companies/585772cfd3a07bffe3000077/sq/users/58577374d3a07bffe30000cc/user_profile?include[user_lessons][only][]=status&include[user_lessons][include][lesson][only]=title&include[user_lessons][only][]=lesson_id&select=_id")
    Call<UserProfile> getUserProfile(@Header("Authorization") String authorization,
                                     @Path("company_id") String companyId,
                                     @Path("user_id") String userId);

    @POST("/oauth/token.json")
    @FormUrlEncoded
    Call<Auth> getNewToken(
            @Field("refresh_token") String token,
            @Field("grant_type") String grantType,
            @Field("scope") String scope
    );

}
