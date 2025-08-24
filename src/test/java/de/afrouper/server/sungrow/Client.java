package de.afrouper.server.sungrow;

import de.afrouper.server.sungrow.api.SungrowClient;
import de.afrouper.server.sungrow.api.SungrowClientFactory;
import de.afrouper.server.sungrow.api.dto.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Client {

    private SungrowClient sungrowClient = null;

    public static void main(String[] args) {
        System.out.println("********** Start Sungrow Client **********");

        Client client = new Client();
        client.executeSample();

        System.out.println("*********** End Sungrow Client ***********");
    }

    private void executeSample() {
        sungrowClient = SungrowClientFactory.createSungrowClientWithEncryption(SungrowClientFactory.Region.EUROPE);
        sungrowClient.login();

        PlantList plants = sungrowClient.getPlants();
        System.out.println(plants);

        plants.plants().forEach(this::handlePlants);
    }

    private void handlePlants(Plant plant) {
        System.out.println("Reading Devices for Plant: " + plant.plantName());
        DeviceList deviceList = sungrowClient.getDevices(plant.plantId());

        System.out.println("Handling " + deviceList.rowCount() + " devices for " + plant.plantName());
        deviceList.devices().forEach(this::handleDevice);
    }

    private void handleDevice(Device device) {
        readRealTimeData(device);

        if(device.serial() != null) {
            BasicPlantInfo basicPlantInfo = sungrowClient.getBasicPlantInfo(device.serial());
            System.out.println(basicPlantInfo);
        }
    }

    private void readRealTimeData(Device device) {
        System.out.println("*********** Start Real Time Data " + device.deviceName() + " **********");
        DevicePointInfoList openPointInfo = sungrowClient.getOpenPointInfo(device.deviceType(), device.deviceModelId());
        List<String> ids = openPointInfo.devicePointInfoList().stream()
                .filter(Objects::nonNull)
                .map(DevicePointInfo::pointId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        DevicePointList devicePointList = sungrowClient.getDeviceRealTimeData(device.deviceType(), Collections.singletonList(device.plantDeviceId()), ids);

        devicePointList
                .devicePointList()
                .stream()
                .map(DevicePoint::pointIds)
                .filter(Objects::nonNull)
                .forEach(e -> handlePoints(e, openPointInfo));

        System.out.println("************ End Real Time Data " + device.deviceName() + " ***********");
    }

    private void handlePoints(Map<String, String> dataPoints, DevicePointInfoList openPointInfo) {
        dataPoints.forEach((key, value) -> {
            DevicePointInfo pointInfo = openPointInfo.getDevicePointInfo(key.substring(1));
            System.out.println(pointInfo.pointName() + ": " + value + " " + pointInfo.storageUnit());
        });
    }
}
