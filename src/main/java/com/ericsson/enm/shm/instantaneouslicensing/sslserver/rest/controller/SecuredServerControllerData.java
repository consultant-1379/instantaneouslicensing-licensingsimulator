/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2019
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/

package com.ericsson.enm.shm.instantaneouslicensing.sslserver.rest.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ericsson.enm.shm.instantaneouslicensing.sslserver.request.ElisRequest;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.response.ElisResponse;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.service.ControllerValidation;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.service.ElisRequestHandler;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.service.ElisRequestVersionStatus;

/**
 * Front Dispatcher
 */
@RestController
public class SecuredServerControllerData {

    private static final Logger logger = LoggerFactory.getLogger(SecuredServerControllerData.class);

    @Autowired
    private ControllerValidation controllerValidation;

    @Autowired
    private ElisRequestHandler elisRequestHandler;

    @Async
    @PostMapping("/requestIdFromElis/api/v1/licenses")
    public ResponseEntity<ElisResponse> requestIdFromElis(@RequestHeader final Map<String, String> headers, @RequestBody final String body) {
        logger.info(" Request Header {}  and Request Body {} ", headers, body);
        ElisRequest elisRequest;
        final ResponseEntity responseEntity;
        final ElisRequestVersionStatus elisRequestVersionStatus = controllerValidation.fetchElisRequestVersion(body, headers);
        if (elisRequestVersionStatus.getElisRequest() == null) {
            responseEntity = elisRequestHandler.getErrorResponseFromElis(ResponseEntity.badRequest().header(HttpHeaders.CONTENT_TYPE,
                    elisRequestVersionStatus.getHeaderVersion()),
                    "HTTP header version and ELIS request payload validation failure",
                    elisRequestVersionStatus.getErrorMessage());
            return responseEntity;
        } else {
            elisRequest = elisRequestVersionStatus.getElisRequest();
            logger.info("ELIS simulator received the elisRequest {} ", elisRequest);
            responseEntity = elisRequestHandler.handleElisRequest(elisRequest, elisRequestVersionStatus);
        }
        return responseEntity;
    }

    /**
     * This API is used to set the licensePackageBuildWaitTime in minutes
     * If this values is set as 3, then License files will be generated after 3 minutes.
     * Ex: https://131.160.227.119/requestIdFromElis/api/v1/licenses/3
     * By default waitTime is set to 0 minutes, If we want to reset back ,then we need to call this API with '0'
     *
     * @param waitTime
     * @retuns HttpStatus
     * @since 2019-12-05
     */
    @Async
    @GetMapping("/requestIdFromElis/api/v1/licenses/{waitTime}")
    @ResponseStatus(HttpStatus.OK)
    public void setLicensePackageBuildWaitTime(@PathVariable("waitTime") final int waitTime) {
        elisRequestHandler.setLicensePackageBuildWaitTime(waitTime);
        logger.info(" License package build wait time has changed to : {} ", waitTime);
    }

}