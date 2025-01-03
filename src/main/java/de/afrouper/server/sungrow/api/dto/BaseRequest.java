package de.afrouper.server.sungrow.api.dto;

import com.google.gson.annotations.SerializedName;

public class BaseRequest {

    @SerializedName("appkey")
    private String appKey;

    @SerializedName("token")
    private String token;

    @SerializedName("lang")
    private Language language;

    @SerializedName("api_key_param")
    private ApiKeyParameter apiKey;

    public BaseRequest() {
        setLanguage(Language.ENGLISH);
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public ApiKeyParameter getApiKey() {
        return apiKey;
    }

    public void setApiKey(ApiKeyParameter apiKey) {
        this.apiKey = apiKey;
    }
}
