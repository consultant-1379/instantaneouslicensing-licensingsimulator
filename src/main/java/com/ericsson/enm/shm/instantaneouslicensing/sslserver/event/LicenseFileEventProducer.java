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

import com.ericsson.enm.shm.instantaneouslicensing.sslserver.request.LicenseFileGenEvent;
import com.lmax.disruptor.RingBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Event Producer implementation required by ring buffer
 * Used to publish events
 */
public class LicenseFileEventProducer {
    private static final Logger logger = LoggerFactory.getLogger(LicenseFileEventProducer.class);
    private final RingBuffer<LicenseFileGenEvent> ringBuffer;

    public LicenseFileEventProducer(final RingBuffer<LicenseFileGenEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    /**
     * Publishes Events(LicenseFileGenEvent)
     *
     * @param inputEvent publish license event
     */
    public void onData(LicenseFileGenEvent inputEvent) {
        long sequence = ringBuffer.next();  // Grab the next sequence
        LicenseFileGenEvent event = ringBuffer.get(sequence); // Get the entry in the Disruptor
        event.setLicenseKeyPackageBuilder(inputEvent.getLicenseKeyPackageBuilder());
        event.setLocalDateTime(inputEvent.getLocalDateTime());// Fill with data
        logger.info("LicenseFileEventProducer publishing the event to ring buffer with request id : {}  for process",
                event.getLicenseKeyPackageBuilder().getRequestId());
        ringBuffer.publish(sequence);
    }

}
