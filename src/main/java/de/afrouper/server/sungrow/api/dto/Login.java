package de.afrouper.server.sungrow.api.dto;

import com.google.gson.annotations.SerializedName;

public class Login {

    @SerializedName("user_account")
    private String username;
    @SerializedName("user_password")
    private String password;
    @SerializedName("appkey")
    private String appKey;

    public Login(String username, String password, String appKey) {
        this.username = username;
        this.password = password;
        this.appKey = appKey;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
}