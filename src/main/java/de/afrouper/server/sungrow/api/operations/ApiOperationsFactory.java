package de.afrouper.server.sungrow.api.operations;

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
}
