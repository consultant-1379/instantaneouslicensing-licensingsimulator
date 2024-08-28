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

import java.time.LocalDateTime;

public class ElisResponseError implements ElisResponse {
    private LocalDateTime timeStamp;
    private String message;
    private String details;

    /**
     * @return the value of timeStamp
     */
    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    /**
     * @param timeStamp to set the value of timeStamp
     */
    public void setTimeStamp(final LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    /**
     * @return the value of message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message to set the value of message
     */
    public void setMessage(final String message) {
        this.message = message;
    }

    /**
     * @return the value of details
     */
    public String getDetails() {
        return details;
    }

    /**
     * @param details to set the value of details
     */
    public void setDetails(final String details) {
        this.details = details;
    }

}
