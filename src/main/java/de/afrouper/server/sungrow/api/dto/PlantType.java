package de.afrouper.server.sungrow.api.dto;

import com.google.gson.annotations.SerializedName;

public enum PlantType {

    @SerializedName("1")
    Utility_Plant,

    @SerializedName("3")
    Distributed_PV_Plant,

    @SerializedName("4")
    Residential_PV_Plant,

    @SerializedName("5")
    Residential_Energy_Storage_Plant,

    @SerializedName("6")
    Village_Plant,

    @SerializedName("7")
    Distributed_Energy_Storage_Plant,

    @SerializedName("8")
    Poverty_Alleviation_Plant,

    @SerializedName("9")
    Wind_Power_Plant,

    @SerializedName("10")
    Utility_Energy_Storage_Plant,

    @SerializedName("12")
    C_and_I_Energy_Storage_Plant
}
