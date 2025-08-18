package de.afrouper.server.sungrow.api.dto;

import com.google.gson.annotations.SerializedName;

public enum DeviceFaultStatus {
    @SerializedName("1")
    Faulty,
    @SerializedName("2")
    Alarm,
    @SerializedName("4")
    Normal
}
