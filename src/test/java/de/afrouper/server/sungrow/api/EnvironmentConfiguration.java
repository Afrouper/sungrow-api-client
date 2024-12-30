package de.afrouper.server.sungrow.api;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;

public class EnvironmentConfiguration {

    public static String getAppKey() {
        return get("APP_KEY", null);
    }

    public static String getSecretKey() {
        return get("SECRET_KEY", null);
    }

    public static String getRSAPublicKey() {
        return get("RSA_PUBLIC_KEY", null);
    }

    public static URI getURI() {
        try {
            return new URI(get("SUNGROW_URI", "https://gateway.isolarcloud.eu/"));
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static Duration getConnectionTimeout() {
        String connectionTimeout = get("CONNECTION_TIMEOUT", "10");
        return Duration.ofSeconds(Integer.parseInt(connectionTimeout));
    }

    public static Duration getRequestTimeout() {
        String requestTimeout = get("REQUEST_TIMEOUT", "30");
        return Duration.ofSeconds(Integer.parseInt(requestTimeout));
    }

    private static String get(String key, String defaultValue) {
        String property = System.getProperty(key);
        if(property != null) {
            return property;
        }
        property = System.getenv(key);
        if(property != null) {
            return property;
        }
        else {
            return defaultValue;
        }
    }
}
