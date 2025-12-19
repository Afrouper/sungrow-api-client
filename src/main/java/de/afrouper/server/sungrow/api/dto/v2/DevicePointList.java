package de.afrouper.server.sungrow.api.dto.v2;

import com.google.gson.annotations.SerializedName;
import de.afrouper.server.sungrow.api.dto.DevicePoint;

import java.util.List;

public record DevicePointList(

        @SerializedName("point_dict")
        List<PointInformation> pointInformation,

        @SerializedName("device_point_list")
        List<DevicePoint> devicePointList,

        @SerializedName("illegal_ps_key_list")
        List<String> illegalPlantIds
) {
}
