package de.afrouper.server.sungrow.api.dto;

import com.google.gson.annotations.SerializedName;

public record DevicePointWrapper(
        @SerializedName("device_point")
        DevicePoint devicePoint
) {
}
