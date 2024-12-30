package de.afrouper.server.sungrow.api.operations;

import de.afrouper.server.sungrow.api.APIOperation;
import de.afrouper.server.sungrow.api.dto.BaseRequest;

abstract class BaseApiOperation<REQ extends BaseRequest, RES> implements APIOperation<REQ, RES> {

    private RES response;

    @Override
    public void setResponse(RES response) {
        this.response = response;
    }

    @Override
    public RES getResponse() {
        return response;
    }
}
