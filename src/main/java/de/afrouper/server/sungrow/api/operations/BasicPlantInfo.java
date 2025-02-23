package de.afrouper.server.sungrow.api.operations;

import com.google.gson.annotations.SerializedName;
import de.afrouper.server.sungrow.api.dto.BaseRequest;

public class BasicPlantInfo extends BaseApiOperation<BasicPlantInfo.Request, BasicPlantInfo.Response> {

    private final Request request;

    BasicPlantInfo(String deviceSerialNumber) {
        super("/openapi/getPowerStationDetail");
        request = new Request();
        request.serialNumber = deviceSerialNumber;
    }

    @Override
    public Request getRequest() {
        return request;
    }

    public static class Request extends BaseRequest {
        @SerializedName("sn")
        private String serialNumber;

        public String getSerialNumber() {
            return serialNumber;
        }

        public void setSerialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
        }
    }

    public static class Response {
        @SerializedName("design_capacity")
        private Integer installedPower;

        @SerializedName("ps_status")
        private Status status;

        public Integer getInstalledPower() {
            return installedPower;
        }

        public Status getStatus() {
            return status;
        }
    }

    public enum Status {
        @SerializedName("1")
        Online,
        @SerializedName("0")
        Offline
    }
}
