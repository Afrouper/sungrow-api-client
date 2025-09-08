package de.afrouper.server.sungrow.api;

import com.google.gson.JsonObject;
import de.afrouper.server.sungrow.api.dto.*;
import de.afrouper.server.sungrow.api.dto.v1.DevicePointList;
import de.afrouper.server.sungrow.api.dto.v1.LoginResponse;

import java.net.URI;
import java.time.Duration;
import java.util.List;

public class SungrowClient extends BaseSungrowClient {

    private LoginResponse loginResponse;

    SungrowClient(URI uri, String appKey, String secretKey, Duration connectTimeout, Duration requestTimeout) {
        super(uri, appKey, secretKey, connectTimeout, requestTimeout);
    }

    void login(String username, String password) {
        JsonObject loginRequest = new JsonObject();
        loginRequest.addProperty("user_account", username);
        loginRequest.addProperty("user_password", password);

        LoginResponse loginResponse = executeRequest("/openapi/login", loginRequest, LoginResponse.class);
        if(LoginState.SUCCESS.equals(loginResponse.login_state())) {
            this.loginResponse = loginResponse;
        }
        else {
            throw new SungrowApiException("Login error. State: " + loginResponse.login_state());
        }
    }

    public PlantList getPlants() {
        JsonObject request = new JsonObject();

        request.addProperty("curPage", 1);
        request.addProperty("size", 10);

        return executeRequest("/openapi/getPowerStationList", request, PlantList.class);
    }

    public DeviceList getDevices(String plantId) {
        JsonObject request = new JsonObject();

        request.addProperty("curPage", 1);
        request.addProperty("size", 10);
        request.addProperty("ps_id", plantId);

        return executeRequest("/openapi/getDeviceList", request, DeviceList.class);
    }

    public DevicePointList getDeviceRealTimeData(DeviceType deviceType, List<String> plantPsKeys, List<String> measuringPoints) {
        JsonObject request = new JsonObject();

        request.add("device_type", gson.toJsonTree(deviceType));
        request.add("point_id_list", gson.toJsonTree(measuringPoints));
        request.add("ps_key_list", gson.toJsonTree(plantPsKeys));

        return  executeRequest("/openapi/getDeviceRealTimeData", request, DevicePointList.class);
    }

    public DevicePointInfoList getOpenPointInfo(DeviceType deviceType, String deviceModelId) {
        JsonObject request = new JsonObject();

        request.add("device_type", gson.toJsonTree(deviceType));
        request.addProperty("device_model_id", deviceModelId);
        request.addProperty("type", 2);
        request.addProperty("curPage", 1);
        request.addProperty("size", 999);

        return  executeRequest("/openapi/getOpenPointInfo", request, DevicePointInfoList.class);
    }

    public BasicPlantInfo getBasicPlantInfo(String serial) {
        JsonObject request = new JsonObject();

        request.addProperty("sn", serial);

        return executeRequest("/openapi/getPowerStationDetail", request, BasicPlantInfo.class);
    }

    @Override
    protected void addAuthorizationData(JsonObject request) {
        if(loginResponse != null) {
            request.addProperty("token", loginResponse.token());
        }
    }
}
