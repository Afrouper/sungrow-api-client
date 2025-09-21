package de.afrouper.server.sungrow.api.dto.v1;

import de.afrouper.server.sungrow.api.dto.LoginState;

public record LoginResponse(
        String email,
        String language,
        LoginState login_state,
        String token,
        String user_id,
        String user_name,
        String country_name
) {
}
