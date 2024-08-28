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

package com.ericsson.enm.shm.instantaneouslicensing.sslserver;

import java.io.File;

public class ConstantValue {
    private ConstantValue() {

    }

    public static final String LICENSE_FILE_ROOT_DIRECTORY = File.separator + "data" + File.separator + "Store" + File.separator + "LicenseFiles";
    public static final String ELIS_SIM_ROOT_CA =
            "src" + File.separator + "main" + File.separator + "resources" + File.separator + "elis_sim_root_ca.p12";
    public static final String BATCH_RESULTS_XML = "batchLKFRequestResults_";
    public static final String XML_EXTENSION = ".xml";
    public static final String ZIP_EXTENSION = ".zip";
    public static final String MDF_EXTENSION = ".md5";
    public static final String SIGNATURE_PREFIX = "ELISOSS";
    public static final String INFO = "_info";
    public static final String KEYSTORE_TYPE_PKCS12 = "PKCS12";
    public static final String SUPPORTED_PROTOCOL_TLS_V_1_2 = "TLSv1.2";
    protected static final char[] KEYSTORE_PW = "athlone".toCharArray();
    public static final int MAX_NUMBER_OF_FILES_IN_ZIP = 100;
    public static final String ELIS_V1_SCHEMA_FILE = "requestSchema/LkfRequest.schema.json";
    public static final String ELIS_V2_SCHEMA_FILE = "requestSchema/LkfRequest_2.schema.json";
    public static final String ELIS_NODE_FAILURE_ERROR_CODE = "ELIS_ERROR_17";
    public static final String ELIS_ERROR_CODE_18 = "ELIS_ERROR_18";
    public static final String ELIS_NODE_FAILURE_ERROR_MSG = "LKF cannot be generated. One or more dependent licenses are missing.";
    public static final String LKF_GENERATION_FAILED = "LKF generation is failed.";
    public static final String ELIS_NODE_FAILURE_ERROR_CATEGORY = "Error";
    public static final String VERSION = "2.0.0";
    public static final String UNDER_SCORE = "_";
    public static final String ELIS_WARNING_1 = "ELIS_WARNING_1";
    public static final String WARNING = "Warning";
    public static final String ELIS_WARNING_1_MSG = "Additional resource request partially successful. Node requested for license elements that were omitted during activation because they cannot be activated on this hardware type.";
}
