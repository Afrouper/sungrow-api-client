package de.afrouper.server.sungrow.api;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import de.afrouper.server.sungrow.api.dto.*;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class SungrowClient {

    private final HttpClient httpClient;
    private final String appKey;
    private final String secretKey;
    private final Duration requestTimeout;
    private final URI uri;
    private final Gson gson;
    private LoginResponse loginResponse;
    private EncryptionUtility encryptionUtility;
    private LocalDateTime lastAPICall;

    SungrowClient(URI uri, String appKey, String secretKey, Duration connectTimeout, Duration requestTimeout) {
        Objects.requireNonNull(uri, "URI cannot be null");
        Objects.requireNonNull(appKey, "App key cannot be null");
        Objects.requireNonNull(secretKey, "Secret key cannot be null");
        Objects.requireNonNull(connectTimeout, "Connect timeout cannot be null");
        Objects.requireNonNull(requestTimeout, "Request timeout cannot be null");
        this.uri = uri;
        this.appKey = appKey;
        this.secretKey = secretKey;
        this.requestTimeout = requestTimeout;
        this.httpClient = HttpClient
                .newBuilder()
                .connectTimeout(connectTimeout)
                .build();
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

    }

    void activateEncryption(String rsaPublicKey, String password) {
        encryptionUtility = new EncryptionUtility(rsaPublicKey, password);
    }

    public void login() throws IOException {
        login(EnvironmentConfiguration.getAccountEmail(), EnvironmentConfiguration.getAccountPassword());
    }

    public void login(String username, String password) throws IOException {
        JsonObject loginRequest = new JsonObject();
        loginRequest.addProperty("user_account", username);
        loginRequest.addProperty("user_password", password);

        LoginResponse loginResponse = executeRequest("/openapi/login", loginRequest, LoginResponse.class);
        if(LoginState.SUCCESS.equals(loginResponse.login_state())) {
            this.loginResponse = loginResponse;
            apiCallSuccess();
        }
        else {
            throw new IOException("Login error. State: " + loginResponse.login_state() );
        }
    }

    private String[] getDefaultHeaders() {
        List<String> headers = new ArrayList<>();
        headers.add("Content-Type");
        headers.add("application/json");
        headers.add("x-access-key");
        headers.add(secretKey);
        headers.add("sys_code");
        headers.add("901");
        if(encryptionUtility != null) {
            headers.add("x-random-secret-key");
            headers.add(encryptionUtility.createRandomPublicKey());
        }
        return headers.toArray(new String[0]);
    }

    private void apiCallSuccess() {
        lastAPICall = LocalDateTime.now();
    }

    public PlantList getPlants() throws IOException {
        JsonObject request = new JsonObject();

        request.addProperty("curPage", 1);
        request.addProperty("size", 10);

        return executeRequest("/openapi/getPowerStationList", request, PlantList.class);
    }

    public DeviceList getDevices(String plantId) throws IOException {
        JsonObject request = new JsonObject();

        request.addProperty("curPage", 1);
        request.addProperty("size", 10);
        request.addProperty("ps_id", plantId);

        return executeRequest("/openapi/getDeviceList", request, DeviceList.class);
    }

    public RealTimeData getRealTimeData(List<String> serials) throws IOException {
        JsonObject request = new JsonObject();

        request.add("sn_list", gson.toJsonTree(serials));

        return executeRequest("/openapi/getPVInverterRealTimeData", request, RealTimeData.class);
    }

    private <T> T executeRequest(String subPath, JsonObject request, Class<T> responseType) throws IOException {
        request.add("lang", gson.toJsonTree(Language.ENGLISH));
        request.addProperty("appkey", appKey);
        if(loginResponse != null) {
            request.addProperty("token", (loginResponse.token()));
        }

        String jsonRequest;
        if(encryptionUtility != null) {
            JsonElement apiKeyParameter = gson.toJsonTree(encryptionUtility.createApiKeyParameter());
            request.add("api_key_param", apiKeyParameter);
            jsonRequest = encryptionUtility.encrypt(gson.toJson(request));
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
            System.out.println(jsonResponse);

            if(send.statusCode() == 200) {
                JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);

                String requestSerial = jsonObject.getAsJsonPrimitive("req_serial_num").getAsString();
                String resultCode = jsonObject.getAsJsonPrimitive("result_code").getAsString();
                String resultMsg = jsonObject.getAsJsonPrimitive("result_msg").getAsString();

                if("1".equals(resultCode)) {
                    apiCallSuccess();
                    return gson.fromJson(jsonObject.getAsJsonObject("result_data"), responseType);
                }
                else {
                    throw new IOException("ResultCode: '" + resultCode + "', ResultMsg: '" + resultMsg + "', RequestSerial: " + requestSerial);
                }
            }
            else {
                throw new IOException("Operation failed. ResponseCode " + send.statusCode() + ": '" + jsonResponse + "'");
            }
        } catch (InterruptedException | NumberFormatException e) {
            throw new RuntimeException("Unable to execute Operation. Json from server: '" + jsonResponse + "'.", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
