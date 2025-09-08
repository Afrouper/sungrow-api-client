package de.afrouper.server.sungrow;

import de.afrouper.server.sungrow.api.EnvironmentConfiguration;
import de.afrouper.server.sungrow.api.SungrowClientBuilder;
import de.afrouper.server.sungrow.api.SungrowClientOAuth;
import de.afrouper.server.sungrow.api.dto.*;
import de.afrouper.server.sungrow.api.dto.v2.DevicePointList;

import java.util.*;

public class OAuth2Client {

    private SungrowClientOAuth sungrowClient = null;

    public static void main(String[] args) {
        System.out.println("********** Start Sungrow Client **********");

        OAuth2Client client = new OAuth2Client();
        client.executeSample();

        System.out.println("*********** End Sungrow Client ***********");
    }

    private void executeSample() {
        sungrowClient = new SungrowClientBuilder()
                .builder(SungrowClientBuilder.Region.EUROPE)
                .withCredentials(EnvironmentConfiguration.getAppKey(), EnvironmentConfiguration.getSecretKey())
                .withOAuth2(EnvironmentConfiguration.getAuthorizeUrl(), EnvironmentConfiguration.getRedirectUrl())
                .build();

        try{
            String authCode = "xxxxx";
            //For Test - set breakpoint and change value with authcode
            System.out.println(authCode);
            sungrowClient.authCodeFlow(authCode);

            PlantList plants = sungrowClient.getPlants();
            System.out.println(plants);

            BasicPlantInfo basicPlantInfo = sungrowClient.getBasicPlantInfo(sungrowClient.getPlantIds().getFirst());
            System.out.println(basicPlantInfo);

            plants.plants().forEach(this::handlePlant);
        }
        catch (Exception e){
            System.out.println(e);
            if(sungrowClient != null) {
                sungrowClient.close();
            }
        }
    }

    private void handlePlant(Plant plant) {
        System.out.println("Reading Devices for Plant: " + plant.plantName());
        DeviceList deviceList = sungrowClient.getDevices(plant.plantId());

        System.out.println("Handling " + deviceList.rowCount() + " devices for " + plant.plantName());

        deviceList.devices().stream().filter(d -> DeviceType.Energy_Storage_System.equals(d.deviceType())).forEach(this::readRealTimeData);
    }

    private void readRealTimeData(Device device) {
        System.out.println("*********** Start Real Time Data **********");

        List<String> psIds = Collections.singletonList(device.plantDeviceId());

        List<String> pointIds = Arrays.asList("13161",
                "13162",
                "13160",
                "13121",
                "13001",
                "13122",
                "13163",
                "18094",
                "18095",
                "18090",
                "18091",
                "18092",
                "18093",
                "13125",
                "13169",
                "13126",
                "13002",
                "13003",
                "13008",
                "13009",
                "13007",
                "18103",
                "13170",
                "18104",
                "18105",
                "13011",
                "18065",
                "13012",
                "18066",
                "13130",
                "18067",
                "13010",
                "18068",
                "18061",
                "18062",
                "18063",
                "18064",
                "13137",
                "13013",
                "13134",
                "13019",
                "18106",
                "13138",
                "18108",
                "13139",
                "18109",
                "13140",
                "18038",
                "18039",
                "13143",
                "18076",
                "18077",
                "18110",
                "13141",
                "18078",
                "13142",
                "18079",
                "18075",
                "13147",
                "13148",
                "13146",
                "13107",
                "13108",
                "13028",
                "13105",
                "13149",
                "13029",
                "13106",
                "13150",
                "18087",
                "13034",
                "13155",
                "18088",
                "18089",
                "18083",
                "18040",
                "18084",
                "18041",
                "18085",
                "18086",
                "18080",
                "18081",
                "18082",
                "13158",
                "13159",
                "13035",
                "13112",
                "13157",
                "13119",
                "13116");

        DevicePointList devicePointList = sungrowClient.getDeviceRealTimeData(psIds, pointIds, DeviceType.Energy_Storage_System);
        System.out.println(devicePointList);

        System.out.println("************ End Real Time Data ***********");
    }

    private void handlePoints(Map<String, String> dataPoints, DevicePointInfoList openPointInfo) {
        dataPoints.forEach((key, value) -> {
            DevicePointInfo pointInfo = openPointInfo.getDevicePointInfo(key.substring(1));
            System.out.println(pointInfo.pointName() + ": " + value + " " + pointInfo.storageUnit());
        });
    }
}
