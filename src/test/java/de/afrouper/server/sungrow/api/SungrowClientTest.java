package de.afrouper.server.sungrow.api;


import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import de.afrouper.server.sungrow.api.operations.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@WireMockTest
class SungrowClientTest {

    private SungrowClient sungrowClient;

    @BeforeEach
    void createClient(WireMockRuntimeInfo wireMockRuntimeInfo) throws Exception {
        stub("/openapi/login", "loginRequest.json", "loginResponse.json");
        sungrowClient = Constants.createTestClient(wireMockRuntimeInfo.getHttpPort());
    }

    @Test
    void login() {
        assertNotNull(sungrowClient);
    }

    @Test
    void plantList() throws Exception {
        stub("/openapi/getPowerStationList", "queryPlantListRequest.json", "queryPlantListResponse.json");

        PlantList plantList = ApiOperationsFactory.getPlantList();
        sungrowClient.execute(plantList);
        assertEquals("JUnit Test Plant", plantList.getResponse().getPlants().getFirst().getPlantName());
    }

    @Test
    void deviceList() throws Exception {
        stub("/openapi/getDeviceList", "queryDeviceListRequest.json", "queryDeviceListResponse.json");

        DeviceList deviceList = ApiOperationsFactory.getDeviceList("689661");
        sungrowClient.execute(deviceList);
        assertEquals(5, deviceList.getResponse().getRowCount());
    }

    @Test
    void basicPlantInfo() throws Exception {
        stub("/openapi/getPowerStationDetail", "queryBasicPlantInfoRequest.json", "queryBasicPlantInfoResponse.json");

        BasicPlantInfo basicPlantInfo = ApiOperationsFactory.getBasicPlantInfo("B2313140126");
        sungrowClient.execute(basicPlantInfo);
        assertEquals(11000, basicPlantInfo.getResponse().getInstalledPower());
    }

    @Test
    void realtimeData() throws Exception {
        stub("/openapi/getPVInverterRealTimeData", "realtimeDataRequest.json", "realtimeDataResponse.json");

        List<String> serials = Arrays.asList("SN_1", "SN_2", "SN_3");
        RealtimeData realtimeData = ApiOperationsFactory.getRealtimeData(serials);
        sungrowClient.execute(realtimeData);
        assertEquals(3, realtimeData.getResponse().getDevicePoints().size());
    }

    private void stub(String path, String requestFile, String responseFile) {
        stubFor(post(urlPathMatching(path))
                .withRequestBody(equalToJson(readResource("/" + requestFile), true, true))
                .withHeader("x-access-key", equalTo(Constants.SECRET_KEY))
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
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}