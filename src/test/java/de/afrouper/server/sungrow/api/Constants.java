package de.afrouper.server.sungrow.api;

import java.net.URI;
import java.time.Duration;

public class Constants {
    static final String APP_KEY = "JUnit App Key";
    static final String SECRET_KEY = "JUnit Secret Key";
    static final String EMAIL = "junitTest@example.com";
    static final String EMAIL_FAIL = "junitTest@fail.com";
    static final String PASSWORD = "password";

    static SungrowClient createTestClient(int port) throws Exception {
        URI uri = new URI("http://localhost:" + port);
        SungrowClient sungrowClient = SungrowClientFactory.createSungrowClient(uri, APP_KEY, SECRET_KEY, Duration.ofSeconds(10), Duration.ofSeconds(30));
        sungrowClient.login(EMAIL, PASSWORD);
        return sungrowClient;
    }

    static void createFailTestClient(int port) throws Exception {
        URI uri = new URI("http://localhost:" + port);
        SungrowClient sungrowClient = SungrowClientFactory.createSungrowClient(uri, APP_KEY, SECRET_KEY, Duration.ofSeconds(10), Duration.ofSeconds(30));
        sungrowClient.login(EMAIL_FAIL, PASSWORD);
    }
}
