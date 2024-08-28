package com.ericsson.enm.shm.instantaneouslicensing.sslserver.service;

import com.ericsson.enm.shm.instantaneouslicensing.sslserver.request.ElisRequest;

public class ElisRequestVersionStatus {

    private ElisRequest elisRequest;
    private String errorMessage;
    private boolean isElisRequestValid = false;
    private String headerVersion;

    boolean isElisRequestValid() {
        return isElisRequestValid;
    }

    void setElisRequestValid(final boolean elisRequestValid) {
        isElisRequestValid = elisRequestValid;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ElisRequest getElisRequest() {
        return elisRequest;
    }

    void setElisRequest(final ElisRequest elisRequest) {
        this.elisRequest = elisRequest;
    }

    public String getHeaderVersion() {
        return headerVersion;
    }

    public void setHeaderVersion(final String headerVersion) {
        this.headerVersion = headerVersion;
    }

}
