package de.afrouper.server.sungrow.api.dto;

import com.google.gson.annotations.SerializedName;

public record DevicePointInfo(
        @SerializedName("point_id")
        String pointId,

        @SerializedName("open_point_remark")
        String openPointRemark,

        @SerializedName("storage_unit")
        String storageUnit,

        @SerializedName("show_unit")
        String showUnit,

        @SerializedName("device_type")
        DeviceType deviceType,

        @SerializedName("point_name")
        String pointName
) {
}
