package de.afrouper.server.sungrow.api.dto.v2;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public record TokenResponse(
        @SerializedName("access_token")
        String accessToken,
        @SerializedName("token_type")
        String tokenType,
        @SerializedName("refresh_token")
        String refreshToken,
        @SerializedName("expires_in")
        Integer expiresIn,
        @SerializedName("auth_user")
        Integer authorizedUser,
        @SerializedName("auth_ps_list")
        List<String> authorizedPlantIds
) {
}
