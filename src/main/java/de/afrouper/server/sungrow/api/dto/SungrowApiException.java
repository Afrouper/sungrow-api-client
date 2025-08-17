package de.afrouper.server.sungrow.api.dto;

public class SungrowApiException extends RuntimeException {

    private final String resultCode;
    private final String requestSerial;

    public SungrowApiException(String message, String resultCode, String requestSerial, Throwable cause) {
        super(message, cause);
        this.resultCode = resultCode;
        this.requestSerial = requestSerial;
    }

    public SungrowApiException(String message, String resultCode, String requestSerial) {
        super(message);
        this.resultCode = resultCode;
        this.requestSerial = requestSerial;
    }

    public SungrowApiException(String message) {
        this(message, null, null);
    }

    public String getResultCode() {
        return resultCode;
    }

    public String getRequestSerial() {
        return requestSerial;
    }
}
