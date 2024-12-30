package de.afrouper.server.sungrow.api;

import de.afrouper.server.sungrow.api.operations.ApiOperationsFactory;
import de.afrouper.server.sungrow.api.operations.BasicPlantInfo;
import de.afrouper.server.sungrow.api.operations.DeviceList;
import de.afrouper.server.sungrow.api.operations.PlantList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
class SungrowClientTest {

    private static SungrowClient sungrowClient;

    @BeforeAll
    static void init() {
        sungrowClient = new SungrowClient(
                EnvironmentConfiguration.getURI(),
                EnvironmentConfiguration.getAppKey(),
                EnvironmentConfiguration.getSecretKey(),
                EnvironmentConfiguration.getConnectionTimeout(),
                EnvironmentConfiguration.getRequestTimeout()
        );
    }

    @Test
    void loginTest() throws Exception {
        sungrowClient.login(System.getenv("username"), System.getenv("password"));

        PlantList plantList = ApiOperationsFactory.getPlantList();
        sungrowClient.execute(plantList);
        assertNotNull(plantList.getResponse());

        DeviceList deviceList = ApiOperationsFactory.getDeviceList(plantList.getResponse().getPlants().get(0).getPlantId());
        sungrowClient.execute(deviceList);
        assertNotNull(deviceList.getResponse());
        assertEquals(3, deviceList.getResponse().getDevices().size());
        deviceList.getResponse().getDevices().forEach(device -> {
            System.out.println("--------------------");
            System.out.println(device.getDeviceType());
            System.out.println(device.getDeviceName());
            System.out.println(device.getDeviceTypeName());
            System.out.println(device.getModelCode());
            System.out.println(device.getSerial());
            System.out.println("*******************");
            BasicPlantInfo basicPlantInfo = ApiOperationsFactory.getBasicPlantInfo(device.getSerial());
            try {
                sungrowClient.execute(basicPlantInfo);
                System.out.println(basicPlantInfo.getResponse().getInstalledPower());
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("*******************");
            System.out.println("--------------------");
        });
        System.out.println(deviceList.getResponse().getDevices());
    }
}