package de.afrouper.server.sungrow.api.dto;

import com.google.gson.annotations.SerializedName;

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
        String uuid

        // See more fields at https://developer-api.isolarcloud.com/#/document/api?id=10955&project_id=2&version=V1
        // Perhaps better provide data as map?
) {
}
