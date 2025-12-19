package de.afrouper.server.sungrow.api.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public record PlantList(
        Integer rowCount,

        @SerializedName("pageList")
        List<Plant> plants
) {
}
