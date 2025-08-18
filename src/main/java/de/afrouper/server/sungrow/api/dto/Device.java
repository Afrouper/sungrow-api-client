package de.afrouper.server.sungrow.api.dto;

import com.google.gson.annotations.SerializedName;

public record Device(
        @SerializedName("chnnl_id")
        Integer channelId,

        @SerializedName("device_sn")
        String serial,

        @SerializedName("ps_key")
        String plantDeviceId,

        @SerializedName("device_type")
        DeviceType deviceType,

        @SerializedName("device_name")
        String deviceName,

        @SerializedName("type_name")
        String deviceTypeName,

        @SerializedName("device_model_code")
        String modelCode,

        @SerializedName("device_model_id")
        String deviceModelId,

        @SerializedName("firmware_version_info")
        String firmwareVersion
) {
}
