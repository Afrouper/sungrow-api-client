package de.afrouper.server.sungrow.api.dto;

import com.google.gson.annotations.SerializedName;

public enum LoginState {

    @SerializedName("-1")
    ACCOUNT_NOT_EXIST,
    @SerializedName("0")
    INCORRECT_PASSWORD,
    @SerializedName("1")
    SUCCESS,
    @SerializedName("2")
    ACCOUNT_LOCKED,
    @SerializedName("5")
    ACCOUNT_LOCKED_BY_ADMIN
}
