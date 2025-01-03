package de.afrouper.server.sungrow.api.dto;


public class ApiKeyParameter {

    private String nonce;
    private String timestamp;

    public ApiKeyParameter() {
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
