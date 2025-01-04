package de.afrouper.server.sungrow.api.operations;

import com.google.gson.annotations.SerializedName;
import de.afrouper.server.sungrow.api.dto.BaseRequest;
import de.afrouper.server.sungrow.api.dto.BaseResponse;

import java.util.ArrayList;
import java.util.List;

public class RealtimeData extends BaseApiOperation<RealtimeData.Request, RealtimeData.Response> {

    public RealtimeData() {
        super("/openapi/getPVInverterRealTimeData");
        setRequest(new RealtimeData.Request());
    }

    public static class Request extends BaseRequest {

        @SerializedName("ps_key_list")
        private List<String> plantKeys;

        @SerializedName("sn_list")
        private List<String> serials;

        public Request() {
            serials = new ArrayList<>();
            plantKeys = new ArrayList<>();
        }

        public List<String> getSerials() {
            return serials;
        }

        public void setSerials(List<String> serials) {
            this.serials = serials;
        }

        public List<String> getPlantKeys() {
            return plantKeys;
        }

        public void setPlantKeys(List<String> plantKeys) {
            this.plantKeys = plantKeys;
        }
    }

    public static class Response {
        @SerializedName("device_point_list")
        private List<DevicePoint> devicePoints;

        public List<DevicePoint> getDevicePoints() {
            return devicePoints;
        }

        public void setDevicePoints(List<DevicePoint> devicePoints) {
            this.devicePoints = devicePoints;
        }
    }

    public static class DevicePoint {

    }

}
