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
package com.ericsson.enm.shm.instantaneouslicensing.sslserver.response;

public class ElisResponseId implements ElisResponse{
    private String id;

    /**
     * @return the value of request id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            to set the value of request id
     */
    public void setId(final String id) {
        this.id = id;
    }

}
