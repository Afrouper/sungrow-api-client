package de.afrouper.server.sungrow.api.dto;

import com.google.gson.annotations.SerializedName;

public enum DeviceStatus {
    @SerializedName("0")
    Undeployed,
    @SerializedName("1")
    Deployed

}
