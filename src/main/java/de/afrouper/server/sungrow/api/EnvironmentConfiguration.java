package de.afrouper.server.sungrow.api;

import java.time.Duration;

public class EnvironmentConfiguration {

    public static String getAppKey() {
        return get("APP_KEY", null);
    }

    public static String getSecretKey() {
        return get("SECRET_KEY", null);
    }

    public static String getAccountEmail() {
        return get("ACCOUNT_EMAIL", null);
    }

    public static String getAccountPassword() {
        return get("ACCOUNT_PASSWORD", null);
    }

    public static String getRSAPublicKey() {
        return get("RSA_PUBLIC_KEY", null);
    }

    public static String getApiCallPassword() {
        return get("API_CALL_PASSWORD", null);
    }

    public static String getAuthorizeUrl() {
        return get("AUTHORIZE_URL", null);
    }

    public static String getRedirectUrl() {
        return get("REDIRECT_URL", null);
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
        if (property != null) {
            return property;
        }
        property = System.getenv(key);
        if (property != null) {
            return property;
        } else {
            return defaultValue;
        }
    }
}
