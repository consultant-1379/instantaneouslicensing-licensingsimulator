/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2020
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/

package com.ericsson.enm.shm.instantaneouslicensing.sslserver.service;

import static com.ericsson.enm.shm.instantaneouslicensing.sslserver.rest.controller.SecuredServerControllerDataConstants.EUFT_10_FORCED_BAD_REQUEST;
import static com.ericsson.enm.shm.instantaneouslicensing.sslserver.rest.controller.SecuredServerControllerDataConstants.EUFT_11_FORCED_INTERNAL_SERVER_ERROR;

import java.time.LocalDateTime;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ericsson.enm.shm.instantaneouslicensing.sslserver.event.ProduceEvents;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.request.ElisRequest;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.request.LicenseFileGenEvent;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.response.ElisResponse;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.response.ElisResponseError;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.response.ElisResponseId;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.response.LicenseKeyPackageBuilder;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.rest.controller.SecuredServerControllerDataConstants;
import static com.ericsson.enm.shm.instantaneouslicensing.sslserver.rest.controller.SecuredServerControllerDataConstants.EUFT_11_FORCED_INTERNAL_SERVER_ERROR;
import static com.ericsson.enm.shm.instantaneouslicensing.sslserver.rest.controller.SecuredServerControllerDataConstants.EUFT_14_SERVICE_UNAVAILABLE;
import static com.ericsson.enm.shm.instantaneouslicensing.sslserver.rest.controller.SecuredServerControllerDataConstants.EUFT_15_NOT_FOUND;
import static com.ericsson.enm.shm.instantaneouslicensing.sslserver.rest.controller.SecuredServerControllerDataConstants.EUFT_16_FORBIDDEN;
import static com.ericsson.enm.shm.instantaneouslicensing.sslserver.rest.controller.SecuredServerControllerDataConstants.EUFT_17_GATEWAY_TIMEOUT;
import static com.ericsson.enm.shm.instantaneouslicensing.sslserver.rest.controller.SecuredServerControllerDataConstants.EUFT_18_ELIS_CONNECTION_ERROR;
import static com.ericsson.enm.shm.instantaneouslicensing.sslserver.rest.controller.SecuredServerControllerDataConstants.EUFT_19_ECN_CONNECTION_ERROR;
/**
 * Handler for the Elis Request request
 * It will handle the Failure cases and Success Cases with Static EUFT id as well as dynamic euft generation
 *
 * @author seena.kali@ericsson.com
 * @Date 04th March 2020
 */
@Service
public class ElisRequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(ElisRequestHandler.class);
    private int licensePackageBuildWaitTime = 0;

    @Autowired
    private ProduceEvents events;

    public ResponseEntity handleElisRequest(final ElisRequest elisRequest, final ElisRequestVersionStatus elisRequestVersionStatus) {
        final ResponseEntity responseEntity;
        final String euft = elisRequest.getEuft();
        if (isRequestError(euft)) {
            responseEntity = handleFailureRequest(euft, elisRequestVersionStatus);
        } else {
            final ElisResponseId elisResponseId = new ElisResponseId();
            elisResponseId.setId(UUID.randomUUID().toString());
            responseEntity = handleSuccessRequest(elisRequest, elisResponseId, elisRequestVersionStatus.getHeaderVersion(),
                    isRequestWithErrorNodes(elisRequest.getEuft()));
        }
        return responseEntity;
    }

    private boolean isRequestError(final String euft) {
        return (euft == null || SecuredServerControllerDataConstants.errorEufts.contains(euft));
    }

    private ResponseEntity handleFailureRequest(final String euft, final ElisRequestVersionStatus elisRequestVersionStatus) {
        ResponseEntity responseEntity = null;

        switch (euft) {
            case EUFT_10_FORCED_BAD_REQUEST:
                responseEntity = getErrorResponseFromElis(ResponseEntity.badRequest().header(HttpHeaders.CONTENT_TYPE,
                        elisRequestVersionStatus.getHeaderVersion()),
                        "Validation failure",
                        "The EUFT reported invalid due to the test case BAD_REQUEST_EUFT_10");
                break;
            case EUFT_11_FORCED_INTERNAL_SERVER_ERROR:
                responseEntity = getErrorResponseFromElis(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .header(HttpHeaders.CONTENT_TYPE, elisRequestVersionStatus.getHeaderVersion()),
                        "Internal Server Error", "EUFT11 will result in forced 500:Internal Server Error");
                break;
            case EUFT_14_SERVICE_UNAVAILABLE:
                responseEntity = getErrorResponseFromElis(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).header(HttpHeaders.CONTENT_TYPE,
                        elisRequestVersionStatus.getHeaderVersion()),
                        "Validation failure",
                        "The EUFT14 reported invalid due to the test case 503: ECN_CONNECTION_ERROR");
                break;
            case EUFT_15_NOT_FOUND:
                responseEntity = getErrorResponseFromElis(ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .header(HttpHeaders.CONTENT_TYPE, elisRequestVersionStatus.getHeaderVersion()),
                        "Internal Server Error", "EUFT15 will result in forced 404:CAS_NOT_REACHABLE");
                break;
            case EUFT_16_FORBIDDEN:
                responseEntity = getErrorResponseFromElis(ResponseEntity.status(HttpStatus.FORBIDDEN).header(HttpHeaders.CONTENT_TYPE,
                        elisRequestVersionStatus.getHeaderVersion()),
                        "Validation failure",
                        "The EUFT16 reported invalid due to the test case 403: EUFT_NOT_VALID");
                break;
            case EUFT_17_GATEWAY_TIMEOUT:
                responseEntity = getErrorResponseFromElis(ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT)
                                .header(HttpHeaders.CONTENT_TYPE, elisRequestVersionStatus.getHeaderVersion()),
                        "Internal Server Error", "EUFT17 will result in forced 504:ECN_CONNECTION_ERROR");
                break;
            case EUFT_18_ELIS_CONNECTION_ERROR:
                responseEntity = getErrorResponseFromElis(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                                .header(HttpHeaders.CONTENT_TYPE, elisRequestVersionStatus.getHeaderVersion()),
                        "Validation message 503 100001", "EUFT18 will result in forced 100001:ELIS_CONNECTION_ERROR");
                break;
            case EUFT_19_ECN_CONNECTION_ERROR:
                responseEntity = getErrorResponseFromElis(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).header(HttpHeaders.CONTENT_TYPE,
                        elisRequestVersionStatus.getHeaderVersion()),
                        "Validation failure 503 100000",
                        "The EUFT19 reported invalid due to the test case 503: ECN_CONNECTION_ERROR");
                break;
            default:
                //New scenarios must be first added to SecuredServerControllerDataConstants.errorEufts() list
                //before implementing here
                throw new IllegalArgumentException("EUFT is not mapped to any error scenario.");
        }

        return responseEntity;
    }

    private ResponseEntity handleSuccessRequest(final ElisRequest elisRequest,
            final ElisResponseId requestId, final String headerVersion, final boolean isRequestWithErrorNodes) {
        final ResponseEntity responseEntity;

        produceEventToGenerateLicense(elisRequest, requestId.getId(), isRequestWithErrorNodes);
        responseEntity = getSuccessfulResponseFromElis(requestId, headerVersion);
        return responseEntity;
    }

    private void produceEventToGenerateLicense(final ElisRequest elisRequest, final String request, final boolean isRequestWithErrorNodes) {
        final LicenseKeyPackageBuilder licenseKeyPackageBuilder = new LicenseKeyPackageBuilder(elisRequest, request, isRequestWithErrorNodes);
        final LicenseFileGenEvent licenseFileGenEvent =
                new LicenseFileGenEvent();
        licenseFileGenEvent.setLocalDateTime(LocalDateTime.now().plusMinutes(getLicensePackageBuildWaitTime()));
        licenseFileGenEvent.setLicenseKeyPackageBuilder(licenseKeyPackageBuilder);
        events.produceEvents(licenseFileGenEvent);
    }

    private boolean isRequestWithErrorNodes(final String euft) {
        boolean isErrorNode = false;
        if (euft != null) {
            isErrorNode = euft.equalsIgnoreCase(SecuredServerControllerDataConstants.EUFT_12_ALL_NODE_FAILURE);
        }
        return isErrorNode;
    }

    private ResponseEntity<ElisResponse> getSuccessfulResponseFromElis(
            final ElisResponseId requestId, final String headerVersion) {
        logger.info("Sent successful response with responseId {}", requestId.getId());
        return ResponseEntity.accepted().header(HttpHeaders.CONTENT_TYPE, headerVersion).body(requestId);
    }

    public ResponseEntity<ElisResponse> getErrorResponseFromElis(
            final ResponseEntity.BodyBuilder bodyBuilder, final String message, final String details) {
        final ElisResponseError elisResponseError = new ElisResponseError();
        handleElisError(elisResponseError, message, details);
        logger.error(" Returning ELIS failure response with  error message  {} , error details {}", message, details);
        return bodyBuilder.body(elisResponseError);
    }

    private static void handleElisError(final ElisResponseError elisResponseError,
            final String message, final String details) {
        elisResponseError.setTimeStamp(LocalDateTime.now());
        elisResponseError.setMessage(message);
        elisResponseError.setDetails(details);
    }

    private int getLicensePackageBuildWaitTime() {
        return licensePackageBuildWaitTime;
    }

    public void setLicensePackageBuildWaitTime(final int licensePackageBuildWaitTime) {
        this.licensePackageBuildWaitTime = licensePackageBuildWaitTime;
    }

}
