package de.afrouper.server.sungrow.api;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.BeforeEach;

@WireMockTest
public class SungrowOAuth2ClientTest {

    private SungrowClientOAuth sungrowClient;

    @BeforeEach
    void createClient(WireMockRuntimeInfo wireMockRuntimeInfo) throws Exception {
        //stub("/openapi/login", "v1/loginRequest.json", "v1/loginResponse.json");
    }
}
