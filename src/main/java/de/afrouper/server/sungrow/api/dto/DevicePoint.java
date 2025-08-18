package de.afrouper.server.sungrow.api.dto;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public record DevicePoint(

        @SerializedName("dev_fault_status")
        DeviceFaultStatus deviceFaultStatus,

        @SerializedName("dev_status")
        DeviceStatus deviceStatus,

        @SerializedName("device_name")
        String name,

        @SerializedName("device_sn")
        String serial,

        @SerializedName("device_time")
        Long deviceUpdateTime,

        @SerializedName("uuid")
        String uuid,

        Map<String, String> pointIds
) {
}
