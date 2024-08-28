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

import com.lmax.disruptor.EventFactory;

/**
 * Event Factory definition required by Disruptor
 */
public class LicenseFileEventFactory implements EventFactory<LicenseFileGenEvent> {

    /**
     * @return LicenseFile wrapper event
     */
    @Override
    public LicenseFileGenEvent newInstance() {
        return new LicenseFileGenEvent();
    }
}
