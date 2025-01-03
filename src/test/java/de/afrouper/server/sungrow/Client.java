package de.afrouper.server.sungrow;

import de.afrouper.server.sungrow.api.SungrowClient;
import de.afrouper.server.sungrow.api.SungrowClientFactory;
import de.afrouper.server.sungrow.api.operations.ApiOperationsFactory;
import de.afrouper.server.sungrow.api.operations.BasicPlantInfo;
import de.afrouper.server.sungrow.api.operations.DeviceList;
import de.afrouper.server.sungrow.api.operations.PlantList;

import java.io.IOException;

public class Client {

    private SungrowClient sungrowClient = null;

    public static void main(String[] args) throws IOException {
        System.out.println("********** Start Sungrow Client **********");

        Client client = new Client();
        client.executeSample();

        System.out.println("*********** End Sungrow Client ***********");
    }

    private void executeSample() throws IOException {
        sungrowClient = SungrowClientFactory.createSungrowClientWithEncryption(SungrowClientFactory.Region.EUROPE);
        sungrowClient.login();

        PlantList plantList = ApiOperationsFactory.getPlantList();
        sungrowClient.execute(plantList);

        plantList.getResponse().getPlants().forEach(this::handlePlants);
    }

    private void handlePlants(PlantList.Plant plant) {
        try {
            System.out.println("Reading Devices for Plant: " + plant.getPlantName());
            DeviceList deviceList = ApiOperationsFactory.getDeviceList(plant.getPlantId());
            sungrowClient.execute(deviceList);

            DeviceList.Response deviceListResponse = deviceList.getResponse();
            System.out.println("Handling " + deviceListResponse.getRowCount() + " devices for " + plant.getPlantName());
            deviceListResponse.getDevices().forEach(this::handleDevice);
        }
        catch (IOException e) {
            System.err.println("Error handling plant " + plant.getPlantId() + ": " + e.getMessage());
        }
    }

    private void handleDevice(DeviceList.Device device) {
        System.out.println("****************************************************************");
        try {
            //if(DeviceList.DeviceType.device.getDeviceType())
            System.out.println("Handling device: " + device.getDeviceName() + " with serial " + device.getSerial());
            System.out.println(device.getDeviceType());
            System.out.println(device.getDeviceTypeName());
            BasicPlantInfo basicPlantInfo = ApiOperationsFactory.getBasicPlantInfo(device.getSerial());
            sungrowClient.execute(basicPlantInfo);

            BasicPlantInfo.Response response = basicPlantInfo.getResponse();
            System.out.println("Installed Power for " + device.getDeviceName() + ": " + response.getInstalledPower());
        }
        catch (IOException e) {
            System.err.println("Error handling device " + device.getDeviceName() + ": " + e.getMessage());
        }
        System.out.println("****************************************************************");
    }
}
