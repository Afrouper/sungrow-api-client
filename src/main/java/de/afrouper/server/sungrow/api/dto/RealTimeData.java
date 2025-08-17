package de.afrouper.server.sungrow.api.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public record RealTimeData(
        @SerializedName("device_point_list")
        List<DevicePointWrapper> devicePoints,

        @SerializedName("fail_sn_list")
        List<String> failedSerials,

        @SerializedName("fail_ps_key_list")
        List<String> failedPlantKeys
) {
}
