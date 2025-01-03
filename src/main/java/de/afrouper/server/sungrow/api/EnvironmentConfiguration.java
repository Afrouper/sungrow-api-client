package de.afrouper.server.sungrow.api;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;

class EnvironmentConfiguration {

    static String getAppKey() {
        return get("APP_KEY", null);
    }

    static String getSecretKey() {
        return get("SECRET_KEY", null);
    }

    static String getAccountEmail() {
        return get("ACCOUNT_EMAIL", null);
    }

    static String getAccountPassword() {
        return get("ACCOUNT_PASSWORD", null);
    }

    static String getRSAPublicKey() {
        return get("RSA_PUBLIC_KEY", null);
    }

    static String getApiCallPassword() {
        return get("API_CALL_PASSWORD", null);
    }

    static Duration getConnectionTimeout() {
        String connectionTimeout = get("CONNECTION_TIMEOUT", "10");
        return Duration.ofSeconds(Integer.parseInt(connectionTimeout));
    }

    static Duration getRequestTimeout() {
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
