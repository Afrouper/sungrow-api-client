package de.afrouper.server.sungrow.api.dto;

import com.google.gson.annotations.SerializedName;

public class BaseResponse<T> {

    @SerializedName("req_serial_num")
    private String serialNumber;

    @SerializedName("result_code")
    private String errorCode;

    @SerializedName("result_msg")
    private String resultMessage;

    @SerializedName("result_data")
    private T data;

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
