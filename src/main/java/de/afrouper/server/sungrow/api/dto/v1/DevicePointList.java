package de.afrouper.server.sungrow.api.dto.v1;

import com.google.gson.annotations.SerializedName;
import de.afrouper.server.sungrow.api.dto.DevicePoint;

import java.util.List;

public record DevicePointList(

        @SerializedName("device_point_list")
        List<DevicePoint> devicePointList,

        @SerializedName("fail_ps_key_list")
        List<String> failedKeys
) {
}
