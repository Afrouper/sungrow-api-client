package de.afrouper.server.sungrow.api;

import com.google.gson.JsonObject;
import de.afrouper.server.sungrow.api.dto.*;
import de.afrouper.server.sungrow.api.dto.v2.DevicePointList;
import de.afrouper.server.sungrow.api.dto.v2.TokenResponse;

import java.io.Closeable;
import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

public class SungrowClientOAuth extends BaseSungrowClient implements Closeable {

    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private final BiConsumer<URI, SungrowClientOAuth> authorizeConsumer;
    private String redirectUrl;
    private TokenResponse tokenResponse;
    private List<String> plantIds;
    private Integer authorizedUser;

    SungrowClientOAuth(URI uri, String appKey, String secretKey, Duration connectTimeout, Duration requestTimeout, Language language, BiConsumer<URI, SungrowClientOAuth> authorizeConsumer) {
        super(uri, appKey, secretKey, connectTimeout, requestTimeout, language);
        this.authorizeConsumer = authorizeConsumer;
    }

    void triggerLogin(String authorizeUrl, String redirectUrl) {
        this.redirectUrl = redirectUrl;
        authorizeConsumer.accept(URI.create(authorizeUrl), this);
    }

    public void authCodeFlow(String authCode) {
        JsonObject json = new JsonObject();
        json.addProperty("grant_type", "authorization_code");
        json.addProperty("code", authCode);
        json.addProperty("redirect_uri", redirectUrl);

        tokenResponse = executeRequest("/openapi/apiManage/token", json, TokenResponse.class);
        if (tokenResponse.expiresIn() == null || tokenResponse.expiresIn() < 10) {
            refreshToken(); //Due to a bug in iSolarCloud; expires in can be negative after login...
            if (tokenResponse.expiresIn() == null || tokenResponse.expiresIn() < 10) {
                throw new SungrowApiException("Token expires in must be greater than 10 seconds");
            }
        }
        plantIds = tokenResponse.authorizedPlantIds();
        authorizedUser = tokenResponse.authorizedUser();
        executor.scheduleWithFixedDelay(this::refreshToken, tokenResponse.expiresIn() - 10, tokenResponse.expiresIn() - 10, TimeUnit.SECONDS);
    }

    @Override
    protected Optional<String> getAuthorizationHeader() {
        if (tokenResponse != null) {
            return Optional.of("Bearer " + tokenResponse.accessToken());
        } else {
            return Optional.empty();
        }
    }

    public void close() {
        executor.shutdown();
    }

    public List<String> getPlantIds() {
        return plantIds;
    }

    public Integer getAuthorizedUser() {
        return authorizedUser;
    }

    private void refreshToken() {
        JsonObject json = new JsonObject();
        json.addProperty("refresh_token", tokenResponse.refreshToken());

        tokenResponse = executeRequest("/openapi/apiManage/refreshToken", json, TokenResponse.class);
    }

    public PlantList getPlants() {
        JsonObject request = new JsonObject();

        request.addProperty("page", 1);
        request.addProperty("size", 10);

        return executeRequest("/openapi/platform/queryPowerStationList", request, PlantList.class);
    }

    public DeviceList getDevices(String plantId) {
        JsonObject request = new JsonObject();

        request.addProperty("page", 1);
        request.addProperty("size", 10);
        request.addProperty("ps_id", plantId);

        return executeRequest("/openapi/platform/getDeviceListByPsId", request, DeviceList.class);
    }

    public BasicPlantInfo getBasicPlantInfo(String plantId) {
        JsonObject request = new JsonObject();

        request.addProperty("ps_ids", plantId);

        return executeRequest("/openapi/platform/getPowerStationDetail", request, BasicPlantInfo.class);
    }

    public DevicePointList getDeviceRealTimeData(List<String> plantIds, List<String> pointIds, DeviceType deviceType) {
        JsonObject request = new JsonObject();

        request.add("ps_key_list", gson.toJsonTree(plantIds));
        request.add("point_id_list", gson.toJsonTree(pointIds));
        request.add("device_type", gson.toJsonTree(deviceType));
        request.addProperty("is_get_point_dict", "1");

        return executeRequest("/openapi/platform/getDeviceRealTimeData", request, DevicePointList.class);
    }
}
