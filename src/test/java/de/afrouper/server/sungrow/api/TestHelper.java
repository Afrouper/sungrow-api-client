package de.afrouper.server.sungrow.api;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.stream.Collectors;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;

public class TestHelper {

    static final String APP_KEY = "JUnit App Key";
    static final String SECRET_KEY = "JUnit Secret Key";
    static final String EMAIL = "junitTest@example.com";
    static final String EMAIL_FAIL = "junitTest@fail.com";
    static final String PASSWORD = "password";
    static final String TEST_PASSWORD = "test_password";

    static {
        initSystemProperties();
    }

    static SungrowClient createTestClient(int port) throws Exception {
        URI uri = new URI("http://localhost:" + port);
        return new SungrowClientBuilder()
                .builder(uri)
                .withCredentials(APP_KEY, SECRET_KEY)
                .withLogin(EMAIL, PASSWORD);
    }

    static void createFailTestClient(int port) throws Exception {
        URI uri = new URI("http://localhost:" + port);
        new SungrowClientBuilder()
                .builder(uri)
                .withCredentials(APP_KEY, SECRET_KEY)
                .withLogin(EMAIL_FAIL, PASSWORD);
    }

    static void initSystemProperties() {
        System.setProperty("APP_KEY", APP_KEY);
        System.setProperty("SECRET_KEY", SECRET_KEY);
        System.setProperty("ACCOUNT_EMAIL", EMAIL);
        System.setProperty("ACCOUNT_PASSWORD", PASSWORD);
        System.setProperty("RSA_PUBLIC_KEY", generateRSAKey());
        System.setProperty("API_CALL_PASSWORD", TEST_PASSWORD);
    }

    static String generateRSAKey() {
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(2048);
            KeyPair pair = keyPairGen.generateKeyPair();
            return Base64.getUrlEncoder().encodeToString(pair.getPublic().getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    static void stub(String path, String requestFile, String responseFile) {
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

    static String readResource(String name) {
        InputStream inputStream = TestHelper.class.getResourceAsStream(name);
        if (inputStream == null) {
            throw new IllegalStateException("Resource not found: " + name);
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
