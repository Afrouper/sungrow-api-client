package de.afrouper.server.sungrow.api;


import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import de.afrouper.server.sungrow.api.dto.DeviceList;
import de.afrouper.server.sungrow.api.dto.PlantList;
import de.afrouper.server.sungrow.api.dto.RealTimeData;
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

        PlantList plantList = sungrowClient.getPlants();
        assertEquals("JUnit Test Plant", plantList.plants().getFirst().plantName());
        assertEquals(Double.valueOf(585), plantList.plants().getFirst().currentPower().value());
    }

    @Test
    void deviceList() throws Exception {
        stub("/openapi/getDeviceList", "queryDeviceListRequest.json", "queryDeviceListResponse.json");

        DeviceList deviceList = sungrowClient.getDevices("689661");
        assertEquals(5, deviceList.rowCount());
    }

    @Test
    void realtimeData() throws Exception {
        stub("/openapi/getPVInverterRealTimeData", "realtimeDataRequest.json", "realtimeDataResponse.json");

        List<String> serials = Arrays.asList("SN_1", "SN_2", "SN_3");
        RealTimeData realTimeData = sungrowClient.getRealTimeData(serials);
        assertEquals(3, realTimeData.devicePoints().size());
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