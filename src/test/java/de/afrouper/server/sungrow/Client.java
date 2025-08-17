package de.afrouper.server.sungrow;

import de.afrouper.server.sungrow.api.SungrowClient;
import de.afrouper.server.sungrow.api.SungrowClientFactory;
import de.afrouper.server.sungrow.api.dto.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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

        PlantList plants = sungrowClient.getPlants();
        System.out.println(plants);

        plants.plants().forEach(this::handlePlants);
    }

    private void handlePlants(Plant plant) {
        try {
            System.out.println("Reading Devices for Plant: " + plant.plantName());
            DeviceList deviceList = sungrowClient.getDevices(plant.plantId());

            System.out.println("Handling " + deviceList.rowCount() + " devices for " + plant.plantName());
            System.out.println(deviceList);

            queryRealtimeData(deviceList);
        }
        catch (IOException e) {
            System.err.println("Error handling plant " + plant.plantId() + ": " + e.getMessage());
        }
    }

    private void queryRealtimeData(DeviceList deviceList) throws IOException {
        List<String> serials = deviceList.devices().stream().map(Device::serial).collect(Collectors.toList());
        RealTimeData realTimeData = sungrowClient.getRealTimeData(serials);
        System.out.println(realTimeData);
    }

}
