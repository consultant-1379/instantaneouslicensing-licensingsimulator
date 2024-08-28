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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SecuredServerControllerDataConstants {
    private SecuredServerControllerDataConstants() {

    }

    public static final String EUFT_10_FORCED_BAD_REQUEST = "euft10";
    public static final String EUFT_11_FORCED_INTERNAL_SERVER_ERROR = "euft11";
    public static final String EUFT_12_ALL_NODE_FAILURE = "euft12";
    public static final String EUFT_13_BATCH_VERSION_V1 = "euft13";
    public static final String EUFT_14_SERVICE_UNAVAILABLE = "euft14";
    public static final String EUFT_15_NOT_FOUND = "euft15";
    public static final String EUFT_16_FORBIDDEN = "euft16";
    public static final String EUFT_17_GATEWAY_TIMEOUT = "euft17";
    public static final String EUFT_18_ELIS_CONNECTION_ERROR = "euft18";
    public static final String EUFT_19_ECN_CONNECTION_ERROR = "euft19";
    public static final String EUFT_20_ELIS_ERROR_18 = "euft20";
    public static final String EUFT_22_ELIS_WARNING_1 = "euft22";
    public static final List<String> errorEufts = Collections.unmodifiableList(Arrays.asList(EUFT_10_FORCED_BAD_REQUEST,
            EUFT_11_FORCED_INTERNAL_SERVER_ERROR,EUFT_14_SERVICE_UNAVAILABLE,EUFT_15_NOT_FOUND,EUFT_16_FORBIDDEN,EUFT_17_GATEWAY_TIMEOUT,EUFT_18_ELIS_CONNECTION_ERROR,EUFT_19_ECN_CONNECTION_ERROR));

    public static final String EUFT_21_INVALID_MD5 = "euft21";
    public static final String EUFT_5_INVALID_ZIP = "euft5";
    public static final String EUFT_6_MISSING_MD5 = "euft6";
    public static final String EUFT_7_MISSING_ZIP = "euft7";

    //Request must have more than 2 nodes with one node as NR01gNodeBRadio00002_fp
    //in this specific test case if request doesn't have 2 nodes, then request will fail as bad request
    public static final String EUFT_9_PARTIAL_BATCH_FAIL = "euft9";
    public static final String FP_FOR_PARTIAL_FAIL = "NR01gNodeBRadio00002_fp";
}
