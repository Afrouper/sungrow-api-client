package de.afrouper.server.sungrow.api;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

final class DesktopAuthorizeHandler {

    static void authorize(URI authorizeUrl, SungrowClientOAuth sungrowClient) {
        try {
            Desktop.getDesktop().browse(authorizeUrl);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
