package de.afrouper.server.sungrow.api.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    @Override
    public String toString() {
        return "DevicePointInfoList{" +
                "devicePointInfoList=" + devicePointInfoList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DevicePointInfoList that = (DevicePointInfoList) o;
        return Objects.equals(devicePointInfoList, that.devicePointInfoList);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(devicePointInfoList);
    }
}
