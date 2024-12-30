package de.afrouper.server.sungrow.api.dto;

import com.google.gson.annotations.SerializedName;

public class LoginResponse extends BaseResponse<LoginResponse.LoginResult> {

    public boolean isSuccess() {
        return getErrorCode() != null && getErrorCode().equals("1");
    }

    public static class LoginResult {

        private String token;
        @SerializedName("login_state")
        private LoginState loginState;
        @SerializedName("msg")
        private String message;

        public String getToken() {
            return token;
        }

        public LoginState getLoginState() {
            return loginState;
        }

        public String getMessage() {
            return message;
        }
    }
}
