package com.ericsson.enm.shm.instantaneouslicensing.sslserver.event;

import java.time.LocalDateTime;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ericsson.enm.shm.instantaneouslicensing.sslserver.request.LicenseFileGenEvent;

/**
 * Singleton wrapper for event producer
 */
@Component
public class ProduceEvents {
    private static final Logger logger = LoggerFactory.getLogger(ProduceEvents.class);
    @Autowired
    private LicenseFileEventProducer producer;

    @Autowired
    private BlockingQueue<LicenseFileGenEvent> licenseRequest;

    /**
     * Push License requests to BlockingQueue The BlockingQueue can be used for future cancel request
     *
     * @param event event representing a license request
     */
    public void produceEvents(LicenseFileGenEvent event) {
        licenseRequest.add(event);
        logger.info("Event Producer received the event with request id {} ", event.getLicenseKeyPackageBuilder().getRequestId());
    }

    /**
     * Push events to RingBuffer
     * TODO:Fixed delay as variable input
     */
    @Scheduled(fixedDelay = 100)
    public void pushEvents() {
        if (!licenseRequest.isEmpty() && LocalDateTime.now().isAfter(licenseRequest.peek().getLocalDateTime())) {
            logger.debug("Event producer hand over the event with request id : {} to LicenseFileEventProducer ",
                    licenseRequest.peek().getLicenseKeyPackageBuilder().getRequestId());
            producer.onData(licenseRequest.poll());
        }
    }
}
