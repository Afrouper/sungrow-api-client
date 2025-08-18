package de.afrouper.server.sungrow.api.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class DevicePointInfoList {
    @SerializedName("pageList")
    private Map<String, DevicePointInfo> devicePointInfoList;

    public DevicePointInfoList(Map<String, DevicePointInfo> devicePointInfoList) {
        this.devicePointInfoList = devicePointInfoList;
    }

    public List<DevicePointInfo> devicePointInfoList() {
        return devicePointInfoList.values().stream().toList();
    }

    public DevicePointInfo getDevicePointInfo(String pointId) {
        return devicePointInfoList.get(pointId);
    }
}
