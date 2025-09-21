package de.afrouper.server.sungrow.api;

import com.google.gson.*;
import de.afrouper.server.sungrow.api.dto.DevicePoint;
import de.afrouper.server.sungrow.api.dto.DevicePointInfoList;
import de.afrouper.server.sungrow.api.dto.Language;
import de.afrouper.server.sungrow.api.dto.SungrowApiException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

abstract class BaseSungrowClient {
    private final HttpClient httpClient;
    private final String appKey;
    private final String secretKey;
    private final Duration requestTimeout;
    private final URI uri;
    protected final Gson gson;
    private final Language language;
    private EncryptionUtility encryptionUtility;

    BaseSungrowClient(URI uri, String appKey, String secretKey, Duration connectTimeout, Duration requestTimeout, Language language) {
        Objects.requireNonNull(uri, "URI cannot be null");
        Objects.requireNonNull(appKey, "App key cannot be null");
        Objects.requireNonNull(secretKey, "Secret key cannot be null");
        Objects.requireNonNull(connectTimeout, "Connect timeout cannot be null");
        Objects.requireNonNull(requestTimeout, "Request timeout cannot be null");
        this.uri = uri;
        this.appKey = appKey;
        this.secretKey = secretKey;
        this.requestTimeout = requestTimeout;
        if (language == null) {
            this.language = Language.ENGLISH;
        }
        else {
            this.language = language;
        }
        this.httpClient = HttpClient
                .newBuilder()
                .connectTimeout(connectTimeout)
                .build();
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(DevicePoint.class, new DevicePointAdapter())
                .registerTypeAdapter(DevicePointInfoList.class, new DevicePointInfoListAdapter())
                .create();
    }

    void activateEncryption(String rsaPublicKey, String password) {
        encryptionUtility = new EncryptionUtility(rsaPublicKey, password);
    }

    protected void addAuthorizationData(JsonObject request) {
    }

    protected Optional<String> getAuthorizationHeader() {
        return Optional.empty();
    }

    protected <T> T executeRequest(String subPath, JsonObject request, Class<T> responseType) {
        request.add("lang", gson.toJsonTree(language));
        request.addProperty("appkey", appKey);
        addAuthorizationData(request);

        String jsonRequest;
        if(encryptionUtility != null) {
            JsonElement apiKeyParameter = gson.toJsonTree(encryptionUtility.createApiKeyParameter());
            request.add("api_key_param", apiKeyParameter);
            jsonRequest = gson.toJson(request);
            jsonRequest = encryptionUtility.encrypt(jsonRequest);
        }
        else {
            jsonRequest = gson.toJson(request);
        }

        HttpRequest httpRequest = HttpRequest.newBuilder(uri.resolve(subPath))
                .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                .timeout(requestTimeout)
                .headers(getDefaultHeaders())
                .build();

        String jsonResponse = null;
        try {
            HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            jsonResponse = send.body();
            if(send.statusCode() >= 200 && send.statusCode() < 500 && encryptionUtility != null) {
                jsonResponse = encryptionUtility.decrypt(jsonResponse);
            }

            if(send.statusCode() == 200) {
                JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);

                String requestSerial = getAsString(jsonObject, "req_serial_num");
                String resultCode = getAsString(jsonObject, "result_code");
                String resultMsg = getAsString(jsonObject, "result_msg");

                if("1".equals(resultCode)) {
                    return gson.fromJson(jsonObject.getAsJsonObject("result_data"), responseType);
                }
                else {
                    throw new SungrowApiException(resultMsg, resultCode, requestSerial);
                }
            }
            else {
                //Try to parse - perhaps the answer is json...
                try {
                    JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);
                    String requestSerial = getAsString(jsonObject, "req_serial_num");
                    String resultCode = getAsString(jsonObject, "result_code");
                    String resultMsg = getAsString(jsonObject, "result_msg");
                    throw new SungrowApiException("HTTP Status " + send.statusCode() + ", Message: " + resultMsg, resultCode, requestSerial);
                }
                catch (Throwable t) {
                    // NOP - no Json from server...
                    throw new SungrowApiException("HTTP Status " + send.statusCode() + ", Body: " + jsonResponse);
                }
            }
        } catch (InterruptedException | NumberFormatException | IOException e) {
            throw new RuntimeException("Unable to execute Operation. Json from server: '" + jsonResponse + "'.", e);
        }
    }

    private String getAsString(JsonObject jsonObject, String memberName) {
        JsonPrimitive jsonPrimitive = jsonObject.getAsJsonPrimitive(memberName);
        if(jsonPrimitive != null) {
            if (jsonPrimitive.isString()) {
                return jsonPrimitive.getAsString();
            } else {
                return jsonPrimitive.toString();
            }
        }
        else {
            return null;
        }
    }

    private String[] getDefaultHeaders() {
        List<String> headers = new ArrayList<>();
        headers.add("Content-Type");
        headers.add("application/json;charset=UTF-8");
        headers.add("x-access-key");
        headers.add(secretKey);
        headers.add("sys_code");
        headers.add("901");
        if(encryptionUtility != null) {
            headers.add("x-random-secret-key");
            headers.add(encryptionUtility.createRandomPublicKey());
        }
        getAuthorizationHeader().ifPresent(h -> {
            headers.add("Authorization");
            headers.add(h);
        });
        return headers.toArray(new String[0]);
    }
}
