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


package com.ericsson.enm.shm.instantaneouslicensing.sslserver.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.enm.shm.instantaneouslicensing.sslserver.request.LicenseFileGenEvent;
import com.lmax.disruptor.EventHandler;

/**
 * Event Handler for Wrapper License Ops
 */
public class LicenseEventHandler implements EventHandler<LicenseFileGenEvent> {
    private static final Logger logger = LoggerFactory.getLogger(LicenseEventHandler.class);
    private final int seq;

    public LicenseEventHandler(final int seq) {
        this.seq = seq;
    }

    /**
     * Receives the corresponding license event and processes the license files
     *
     * @param licenseEvent Event received by handler
     * @param sequence     id provided by ring buffer
     * @param batch        batching provided by disruptor
     */
    @Override
    public void onEvent(final LicenseFileGenEvent licenseEvent, final long sequence, final boolean batch) {
        if (sequence % Runtime.getRuntime().availableProcessors() == this.seq) {
            logger.info("LicenseEventHandler receives the event from ring buffer with request Id : {}  for processing",
                    licenseEvent.getLicenseKeyPackageBuilder().getRequestId());
            licenseEvent.getLicenseKeyPackageBuilder().build();
        }
    }
}




