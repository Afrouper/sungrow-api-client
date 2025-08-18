package de.afrouper.server.sungrow.api.dto;

import com.google.gson.annotations.SerializedName;

public record BasicPlantInfo(

        @SerializedName("ps_id")
        Integer plantId,

        @SerializedName("ps_key")
        String plantPs_key,

        @SerializedName("connect_type")
        GridConnectionType connectType,

        String description,

        @SerializedName("design_capacity")
        Integer designCapacity,

        @SerializedName("ps_location")
        String plantLocation,

        @SerializedName("ps_name")
        String plantName,

        @SerializedName("ps_status")
        PlantStatus plantStatus,

        @SerializedName("ps_type")
        PlantType plantType,

        @SerializedName("ps_type_name")
        String plantTypeName
) {
}
