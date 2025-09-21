package de.afrouper.server.sungrow.api;

import java.net.URI;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

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
}
