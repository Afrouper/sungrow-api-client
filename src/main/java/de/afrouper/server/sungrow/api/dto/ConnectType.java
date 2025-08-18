package de.afrouper.server.sungrow.api.dto;

import com.google.gson.annotations.SerializedName;

public enum ConnectType {
    @SerializedName("1")
    Full_Grid,
    @SerializedName("2")
    Self_Used_Feedin,
    @SerializedName("3")
    Self_Used_No_Feedin,
    @SerializedName("4")
    Off_Grid
}
