package de.afrouper.server.sungrow.api.dto;

import com.google.gson.annotations.SerializedName;

public enum GridConnectionType {

    @SerializedName("1")
    FULL_GRID,

    @SerializedName("2")
    SELF_WITH_GRID,

    @SerializedName("3")
    SELF_WITHOUT_GRID,

    @SerializedName("4")
    OFF_GRID
}
