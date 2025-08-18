package de.afrouper.server.sungrow.api.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public record DevicePointList(

        @SerializedName("device_point_list")
        List<DevicePoint> devicePointList,

        @SerializedName("fail_ps_key_list")
        List<String> failedKeys
) {
}
