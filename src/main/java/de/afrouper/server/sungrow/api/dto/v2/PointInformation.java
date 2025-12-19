package de.afrouper.server.sungrow.api.dto.v2;

import com.google.gson.annotations.SerializedName;

public record PointInformation(
        @SerializedName("point_id")
        String id,
        @SerializedName("point_unit")
        String unit,
        @SerializedName("point_name")
        String name
) {
}
