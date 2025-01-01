package de.afrouper.server.sungrow.api;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;

public class SungrowClientFactory {

    private SungrowClientFactory() {}

    public static SungrowClient createSungrowClient(Region region) {
        return new SungrowClient(
                region.getBaseUrl(),
                EnvironmentConfiguration.getAppKey(),
                EnvironmentConfiguration.getSecretKey(),
                EnvironmentConfiguration.getConnectionTimeout(),
                EnvironmentConfiguration.getRequestTimeout());
    }

    public static SungrowClient createSungrowClient(Region region, String appKey, String secretKey, Duration connectionTimeout, Duration requestTimeout) {
        return new SungrowClient(
                region.getBaseUrl(),
                appKey,
                secretKey,
                connectionTimeout,
                requestTimeout);
    }

    public static SungrowClient createSungrowClient(URI baseUrl, String appKey, String secretKey, Duration connectionTimeout, Duration requestTimeout) {
        return new SungrowClient(
                baseUrl,
                appKey,
                secretKey,
                connectionTimeout,
                requestTimeout);
    }

    public enum Region {
        CHINA("https://gateway.isolarcloud.com/"),
        INTERNATIONAL("https://gateway.isolarcloud.com.hk/"),
        EUROPE("https://gateway.isolarcloud.eu/"),
        AUSTRALIA("https://augateway.isolarcloud.com/");

        private final URI baseUrl;

        Region(String baseUrl) {
            try {
                this.baseUrl = new URI(baseUrl);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }

        public URI getBaseUrl() {
            return baseUrl;
        }
    }
}
