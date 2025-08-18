package de.afrouper.server.sungrow.api.dto;

import com.google.gson.annotations.SerializedName;

public enum PlantStatus {

    @SerializedName("1")
    ONLINE,

    @SerializedName("0")
    OFFLINE,
}
