package de.afrouper.server.sungrow.api.dto;

import com.google.gson.annotations.SerializedName;

public record LoginResponse(
    String email,
    String language,
    LoginState login_state,
    String token,
    String user_id,
    String user_name,
    String country_name
) {}
