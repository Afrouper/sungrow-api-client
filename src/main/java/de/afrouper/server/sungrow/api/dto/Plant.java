package de.afrouper.server.sungrow.api.dto;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;

public record Plant(
        @SerializedName("ps_name")
        String plantName,

        @SerializedName("ps_type")
        PlantType plantType,

        @SerializedName("ps_id")
        String plantId,

        @SerializedName("connect_type")
        ConnectType connectType,

        @SerializedName("total_energy")
        UnitValuePair totalEnergy,

        @SerializedName("curr_power_update_time")
        LocalDateTime currentPowerUpdateTime,

        @SerializedName("curr_power")
        UnitValuePair currentPower,

        @SerializedName("co2_reduce_total")
        UnitValuePair co2ReduceTotal,

        @SerializedName("today_income")
        UnitValuePair todayIncome,

        @SerializedName("equivalent_hour")
        UnitValuePair equivalentHour,

        @SerializedName("total_income")
        UnitValuePair totalIncome,

        @SerializedName("total_capcity")
        UnitValuePair totalCapcity,

        @SerializedName("today_energy")
        UnitValuePair todayEnergy,

        @SerializedName("co2_reduce")
        UnitValuePair co2Reduce,

        @SerializedName("alarm_count")
        int alarmCount,

        @SerializedName("latitude")
        Double latitude,

        @SerializedName("longitude")
        Double longitude
) {
}
