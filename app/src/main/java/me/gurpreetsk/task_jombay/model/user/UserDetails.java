package me.gurpreetsk.task_jombay.model.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import me.gurpreetsk.task_jombay.utils.RealmString;

/**
 * Created by Gurpreet on 14/05/17.
 */

public class UserDetails extends RealmObject {

    @PrimaryKey
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("company_ids")
    @Expose
    private RealmList<RealmString> companyIds = null;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("email")
    @Expose
    private String email;
//    @SerializedName("is_suspended")
//    @Expose
//    private Object isSuspended;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("role_ids")
    @Expose
    private RealmList<RealmString> roleIds = null;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("username")
    @Expose
    private String username;

    public UserDetails() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RealmList<RealmString> getCompanyIds() {
        return companyIds;
    }

    public void setCompanyIds(RealmList<RealmString> companyIds) {
        this.companyIds = companyIds;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<RealmString> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(RealmList<RealmString> roleIds) {
        this.roleIds = roleIds;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
