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

public enum RequestType {

    INTEGRATION(""), FIRST_PERPETUAL("FIRST_PERPETUAL"), EXPANSION("EXPANSION"), UPGRADE("UPGRADE"), UPDATE("UPDATE");

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    RequestType(final String name) {
        this.name = name;
    }

    private final String name;

}
