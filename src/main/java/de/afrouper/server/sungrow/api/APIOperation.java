package de.afrouper.server.sungrow.api;

import de.afrouper.server.sungrow.api.dto.BaseRequest;

public interface APIOperation<REQ extends BaseRequest, RES> {

    String getPath();

    default Method getMethod() {
        return Method.POST;
    }

    REQ getRequest();

    void setResponse(RES response);

    RES getResponse();

    enum Method {
        GET,
        POST,
        PUT,
        DELETE
    }
}
