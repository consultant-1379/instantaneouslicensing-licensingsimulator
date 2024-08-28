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

package com.ericsson.enm.shm.instantaneouslicensing.sslserver.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NodeCapacity {
    private String keyId;
    private String requiredLevel;

    /**
     * @return the keyId
     */
    public String getKeyId() {
        return keyId;
    }

    /**
     * @param keyId
     *            the keyId to set
     */
    public void setKeyId(final String keyId) {
        this.keyId = keyId;
    }

    /**
     * @return the requiredLevel
     */
    public String getRequiredLevel() {
        return requiredLevel;
    }

    /**
     * @param requiredLevel
     *            the requiredLevel to set
     */
    public void setRequiredLevel(final String requiredLevel) {
        this.requiredLevel = requiredLevel;
    }

}
