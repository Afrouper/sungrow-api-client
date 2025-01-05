package de.afrouper.server.sungrow.api.operations;

import de.afrouper.server.sungrow.api.APIOperation;
import de.afrouper.server.sungrow.api.dto.BaseRequest;

abstract class BaseApiOperation<REQ extends BaseRequest, RES> implements APIOperation<REQ, RES> {

    private final String path;
    private REQ request;
    private RES response;

    protected BaseApiOperation(String path) {
        this.path = path;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public REQ getRequest() {
        return request;
    }

    public void setRequest(REQ request) {
        this.request = request;
    }

    @Override
    public void setResponse(RES response) {
        this.response = response;
    }

    @Override
    public RES getResponse() {
        return response;
    }
}
