package de.afrouper.server.sungrow.api;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.Objects;
import java.util.function.BiConsumer;

public final class SungrowClientBuilder {

    public Builder builder(Region region) {
        return new Builder(region.getBaseUrl());
    }

    public Builder builder(URI baseUri) {
        return new Builder(baseUri);
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

    public static final class Builder {

        private final URI baseUri;
        private String appKey;
        private String secretKey;
        private Duration connectionTimeout = Duration.ofSeconds(10);
        private Duration requestTimeout = Duration.ofSeconds(30);
        private String rsaPublicKey;
        private String apiCallPassword;

        private Builder(URI baseUri) {
            this.baseUri = baseUri;
        }

        public Builder withCredentials(String appKey,  String secretKey) {
            this.appKey = appKey;
            this.secretKey = secretKey;
            return this;
        }

        public Builder withConnectTimeout(Duration connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        public Builder withRequestTimeout(Duration requestTimeout) {
            this.requestTimeout = requestTimeout;
            return this;
        }

        public Builder withEncryption(String rsaPublicKey, String apiCallPassword) {
            this.rsaPublicKey = rsaPublicKey;
            this.apiCallPassword = apiCallPassword;
            return this;
        }

        private void check() {
            Objects.requireNonNull(appKey, "appKey must not be null");
            Objects.requireNonNull(secretKey, "secretKey must not be null");
            Objects.requireNonNull(requestTimeout, "requestTimeout must not be null");
            Objects.requireNonNull(connectionTimeout, "connectionTimeout must not be null");
        }

        public SungrowClient withLogin(String username, String password) {
            check();
            Objects.requireNonNull(username, "username must not be null");
            Objects.requireNonNull(password, "password must not be null");

            SungrowClient sungrowClient = new SungrowClient(baseUri, appKey, secretKey, connectionTimeout, requestTimeout);
            activateEncryption(sungrowClient);

            sungrowClient.login(username, password);

            return sungrowClient;
        }

        public OAuth2Builder withOAuth2(String authorizeUrl, String redirectUri) {
            check();
            Objects.requireNonNull(authorizeUrl, "authorizeUrl must not be null");
            Objects.requireNonNull(redirectUri, "redirectUri must not be null");

            return new OAuth2Builder(this, authorizeUrl, redirectUri);
        }

        private void activateEncryption(BaseSungrowClient baseSungrowClient) {
            if(rsaPublicKey != null && apiCallPassword != null) {
                baseSungrowClient.activateEncryption(rsaPublicKey, apiCallPassword);
            }
        }
    }

    public static final class OAuth2Builder {
        private final Builder builder;
        private final String authorizeUrl;
        private final String redirectUri;
        private BiConsumer<URI, SungrowClientOAuth> authorizeConsumer;

        private OAuth2Builder(Builder builder, String authorizeUrl, String redirectUri) {
            this.builder = builder;
            this.authorizeUrl = authorizeUrl;
            this.redirectUri = redirectUri;
            authorizeConsumer = DesktopAuthorizeHandler::authorize;
        }

        public OAuth2Builder withAuthorizeConsumer(BiConsumer<URI, SungrowClientOAuth> authorizeConsumer) {
            this.authorizeConsumer = authorizeConsumer;
            return this;
        }

        public SungrowClientOAuth build() {
            SungrowClientOAuth sungrowClient = new SungrowClientOAuth(builder.baseUri, builder.appKey, builder.secretKey, builder.connectionTimeout, builder.requestTimeout, authorizeConsumer);
            builder.activateEncryption(sungrowClient);

            sungrowClient.triggerLogin(authorizeUrl, redirectUri);

            return sungrowClient;
        }
    }
}
