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

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.ericsson.enm.shm.instantaneouslicensing.sslserver.ConstantValue;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.request.ElisRequest;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.rest.controller.SecuredServerControllerDataConstants;
import com.google.gson.Gson;

/**
 * Service Layer for Controller Operations
 */
@Service
public class ControllerValidationImpl implements ControllerValidation {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ControllerValidationImpl.class);

    /**
     * Returns the generified Object for future versions more cases can be appended
     *
     * @param body    Request Payload
     * @param headers Request Headers
     * @return ElisRequestVersionStatus
     */
    @Override
    public ElisRequestVersionStatus fetchElisRequestVersion(final String body, final Map<String, String> headers) {
        final Gson gson = new Gson();
        final ElisRequestVersionStatus elisRequestVersionStatus = new ElisRequestVersionStatus();
        ElisRequest elisRequest = null;
        final String contentType = getContentType(headers);
        setElisRequestVersionStatusHeader(headers, elisRequestVersionStatus);
        logger.info(" Requested HTTP header version {} ", contentType);
        if (contentType != null && !contentType.isEmpty()) {
            validateRequestWithVersion(body, getVersionSchemaFile(contentType), elisRequestVersionStatus);
            if (elisRequestVersionStatus.isElisRequestValid()) {
                logger.info(" Validation of elisRequest with version {} is successful", contentType);
                elisRequest = gson.fromJson(body, ElisRequest.class);
                if (elisRequest.getEuft().equals(SecuredServerControllerDataConstants.EUFT_9_PARTIAL_BATCH_FAIL)
                    && elisRequest.getNodes()!= null && elisRequest.getNodes().size() < 2) {
                    elisRequestVersionStatus.setElisRequestValid(false);
                    elisRequestVersionStatus.setErrorMessage("EUFT9 was selected and less than 2 nodes were sent. Please select 2 or more nodes "
                                                             + "and one node as NR01gNodeBRadio00002_fp");
                    elisRequest = null;
                } else {
                    elisRequest = gson.fromJson(body, ElisRequest.class);
                }
                logger.info(" ELIS request fetched from payload {} ", elisRequest);
            } else {
                logger.error(" Failure to validate the ELIS request Payload {} with HTTP header version {}  is {} ", body,
                        elisRequestVersionStatus.getHeaderVersion(),
                        elisRequestVersionStatus.getErrorMessage());
            }
            elisRequestVersionStatus.setElisRequest(elisRequest);
        }
        return elisRequestVersionStatus;
    }

    /**
     * This method is to read the Http Header Accept value. If it gets the value with key Accept then it will returns it.
     * If it fails to read with key Accept, then it reads with key accept and returns
     * If it fails to read header with keys Accept and accept, then it will return by default Application/Json
     *
     * @param headers
     * @param elisRequestVersionStatus
     */
    private void setElisRequestVersionStatusHeader(final Map<String, String> headers,
            final ElisRequestVersionStatus elisRequestVersionStatus) {
        String headerVersion;
        if (headers.get(HttpHeaders.ACCEPT) == null) {
            if (headers.get(HttpHeaders.ACCEPT.toLowerCase()) == null) {
                headerVersion = "Application/Json";
            } else {
                headerVersion = headers.get(HttpHeaders.ACCEPT.toLowerCase());
            }
        } else {
            headerVersion = headers.get(HttpHeaders.ACCEPT);
        }
        elisRequestVersionStatus.setHeaderVersion(headerVersion);
    }

    /**
     * This method reads the Http Content-Type value, split the charset=UTF-8 and sends only the content-type value.
     *
     * @param headers
     * @return returns the Content-Type values from the HTTPHeader
     */
    private String getContentType(final Map<String, String> headers) {
        final String contentTypeWithCharset = headers.get(HttpHeaders.CONTENT_TYPE) == null ?
                headers.get(HttpHeaders.CONTENT_TYPE.toLowerCase()) :
                headers.get(
                        HttpHeaders.CONTENT_TYPE);
        return contentTypeWithCharset.contains(";charset=UTF-8") ?
                contentTypeWithCharset.substring(0, contentTypeWithCharset.indexOf(';')) :
                contentTypeWithCharset;
    }

    /**
     * This method maps the Http Header version with corresponding schema file and returns the schema file name.
     * by default it will returns the v1 schema file if the provided header is not match with any case.
     *
     * @param version
     * @return schemaFileName
     */
    private String getVersionSchemaFile(final String version) {
        final String versionSchemaFile;
        switch (version) {
            case "application/json":
            case "application/vnd.ericsson.eth.v1.0.0+json":
                versionSchemaFile = ConstantValue.ELIS_V1_SCHEMA_FILE;
                break;
            case "application/vnd.ericsson.eth.v2.0.0+json":
                versionSchemaFile = ConstantValue.ELIS_V2_SCHEMA_FILE;
                break;
            case "application/vnd.ericsson.eth_minilink.v1.0.0+json":
                versionSchemaFile = ConstantValue.ELIS_V2_SCHEMA_FILE;
                break;
            default:
                versionSchemaFile = ConstantValue.ELIS_V1_SCHEMA_FILE;
                break;
        }
        return versionSchemaFile;
    }

    /**
     * This method is validate the Http Payload against the given schema file. if the validation is fails then it will update the
     * ElisRequestVersionStatus with failure reason and validation status.
     *
     * @param body
     * @param schemaFile
     * @param elisRequestVersionStatus
     */
    private void validateRequestWithVersion(final String body, final String schemaFile,
            final ElisRequestVersionStatus elisRequestVersionStatus) {
        logger.info(" Requested message body {} ", body);
        try (final InputStream inputStream = getClass().getClassLoader().getResourceAsStream(schemaFile)) {
            final String elisSchema = IOUtils.toString(inputStream, UTF_8);
            final JSONObject rawSchema = new JSONObject(elisSchema);
            final Schema schema = SchemaLoader.load(rawSchema);
            schema.validate(new JSONObject(body));
            elisRequestVersionStatus.setElisRequestValid(true);
        } catch (final IOException | JSONException ex) {
            elisRequestVersionStatus.setElisRequestValid(false);
            elisRequestVersionStatus.setErrorMessage("JSONException or  IOException occurred " + ex.getMessage());
        } catch (final ValidationException validationEx) {
            elisRequestVersionStatus.setElisRequestValid(false);
            elisRequestVersionStatus.setErrorMessage(
                    "Schema validation failed, please check the request payload and request version : " + validationEx.getAllMessages());
        }
    }

}

