package de.afrouper.server.sungrow.api.operations;

import java.util.List;

public class ApiOperationsFactory {

    public static PlantList getPlantList() {
        return new PlantList();
    }

    public static DeviceList getDeviceList(String plantId) {
        return new DeviceList(plantId);
    }

    public static BasicPlantInfo getBasicPlantInfo(String deviceSerialNumber) {
        return new BasicPlantInfo(deviceSerialNumber);
    }

    public static RealtimeData getRealtimeData(List<String> serials) {
        RealtimeData realTimeData = new RealtimeData();
        realTimeData.getRequest().setSerials(serials);
        return realTimeData;
    }
}
