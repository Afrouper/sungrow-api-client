package de.afrouper.server.sungrow.api;


import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import de.afrouper.server.sungrow.api.operations.ApiOperationsFactory;
import de.afrouper.server.sungrow.api.operations.BasicPlantInfo;
import de.afrouper.server.sungrow.api.operations.DeviceList;
import de.afrouper.server.sungrow.api.operations.PlantList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    void login() throws Exception {
        assertNotNull(sungrowClient);
    }

    @Test
    void plantList() throws Exception {
        stub("/openapi/getPowerStationList", "queryPlantListRequest.json", "queryPlantListResponse.json");

        PlantList plantList = ApiOperationsFactory.getPlantList();
        sungrowClient.execute(plantList);
        assertEquals("JUnit Test Plant", plantList.getResponse().getPlants().get(0).getPlantName());
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