package de.afrouper.server.sungrow.api;


import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import de.afrouper.server.sungrow.api.dto.*;
import de.afrouper.server.sungrow.api.dto.v1.DevicePointList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.stream.Collectors;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@WireMockTest
class SungrowClientTest {

    private SungrowClient sungrowClient;

    @BeforeEach
    void createClient(WireMockRuntimeInfo wireMockRuntimeInfo) throws Exception {
        stub("/openapi/login", "loginRequest.json", "loginResponse.json");
        sungrowClient = TestHelper.createTestClient(wireMockRuntimeInfo.getHttpPort());
    }

    @Test
    void login() {
        assertNotNull(sungrowClient);
    }

    @Test
    void failLogin(WireMockRuntimeInfo wireMockRuntimeInfo) {
        stub("/openapi/login", "loginRequest_Fail.json", "loginResponse_Fail.json");
        SungrowApiException ex = assertThrows(
                SungrowApiException.class,
                () -> TestHelper.createFailTestClient(wireMockRuntimeInfo.getHttpPort())
        );
        assertTrue(ex.getMessage().contains("error"));
        assertEquals("42", ex.getResultCode());
    }

    @Test
    void plantList() {
        stub("/openapi/getPowerStationList", "queryPlantListRequest.json", "queryPlantListResponse.json");

        PlantList plantList = sungrowClient.getPlants();
        Plant plant = plantList.plants().getFirst();
        assertEquals("JUnit Test Plant", plant.plantName());
        assertEquals("585", plant.currentPower().value());
        assertEquals(ConnectType.Full_Grid, plant.connectType());
    }

    @Test
    void basicPlantInfo() {
        stub("/openapi/getPowerStationDetail", "basicPlantInfoRequest.json", "basicPlantInfoResponse.json");

        BasicPlantInfo basicPlantInfo = sungrowClient.getBasicPlantInfo("B2313140126");
        assertEquals(11000, basicPlantInfo.designCapacity());
        assertEquals(ConnectType.Full_Grid, basicPlantInfo.connectType());
    }

    @Test
    void deviceList() {
        stub("/openapi/getDeviceList", "queryDeviceListRequest.json", "queryDeviceListResponse.json");

        DeviceList deviceList = sungrowClient.getDevices("689661");
        assertEquals(5, deviceList.rowCount());
    }

    @Test
    void pointInformationList() {
        stub("/openapi/getOpenPointInfo", "getOpenPointInfoRequest.json", "getOpenPointInfoResponse.json");

        DevicePointInfoList openPointInfo = sungrowClient.getOpenPointInfo(DeviceType.Inverter, "367701");
        assertEquals(4, openPointInfo.devicePointInfoList().size());
    }

    @Test
    void deviceRealTimeData() {
        stub("/openapi/getDeviceRealTimeData", "deviceRealTimeDataRequest.json", "deviceRealTimeDataResponse.json");

        DevicePointList deviceRealTimeData = sungrowClient.getDeviceRealTimeData(DeviceType.Plant,
                Arrays.asList("972018_11_0_0",
                        "971822_11_0_0",
                        "972131_11_0_0",
                        "972192_11_0_0",
                        "972245_11_0_0",
                        "971884_11_0_0",
                        "971868_11_0_0",
                        "972231_11_0_0",
                        "971556_11_0_0",
                        "971812_11_0_0",
                        "971841_11_0_0",
                        "972292_11_0_0",
                        "971671_11_0_0",
                        "972402_11_0_0",
                        "972079_11_0_0",
                        "972361_11_0_0",
                        "971779_11_0_0",
                        "971867_11_0_0",
                        "972086_11_0_0",
                        "972262_11_0_0",
                        "971891_11_0_0",
                        "972393_11_0_0",
                        "972359_11_0_0",
                        "972270_11_0_0",
                        "972068_11_0_0",
                        "972062_11_0_0",
                        "972213_11_0_0",
                        "971821_11_0_0",
                        "972028_11_0_0",
                        "972347_11_0_0",
                        "971989_11_0_0",
                        "971594_11_0_0",
                        "971886_11_0_0",
                        "972182_11_0_0",
                        "972175_11_0_0",
                        "971897_11_0_0",
                        "971830_11_0_0",
                        "971644_11_0_0",
                        "971988_11_0_0",
                        "972307_11_0_0",
                        "972188_11_0_0",
                        "971732_11_0_0",
                        "972083_11_0_0",
                        "972363_11_0_0",
                        "972368_11_0_0",
                        "972370_11_0_0",
                        "972400_11_0_0",
                        "972315_11_0_0",
                        "970645_11_0_0",
                        "970956_11_0_0"),
                Arrays.asList("83022", "83033"));

        assertEquals(50, deviceRealTimeData.devicePointList().size());
    }

    private void stub(String path, String requestFile, String responseFile) {
        stubFor(post(urlPathMatching(path))
                .withRequestBody(equalToJson(readResource("/" + requestFile), true, true))
                .withHeader("x-access-key", equalTo(TestHelper.SECRET_KEY))
                .withHeader("sys_code", equalTo("901"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(readResource("/" + responseFile))
                )
        );
    }

    private String readResource(String name) {
        InputStream inputStream = getClass().getResourceAsStream(name);
        if(inputStream == null) {
            throw new IllegalStateException("Resource not found: " + name);
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}