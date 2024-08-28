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

package com.ericsson.enm.shm.instantaneouslicensing.sslserver.queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ericsson.enm.shm.instantaneouslicensing.sslserver.event.LicenseEventHandler;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.event.LicenseFileEventProducer;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.request.LicenseFileEventFactory;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.request.LicenseFileGenEvent;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * BlockingQueue and disruptor init and config
 */
@Configuration
public class Queue {
    private static final Logger logger = LoggerFactory.getLogger(Queue.class);
    @Value(("${capacity}"))
    private Integer capacity;

    @Value(("${buffer}"))
    private Integer buffer;

    /**
     * @return ArrayBlockingQueue
     */
    @Bean(name = "licenseRequest")
    public BlockingQueue<LicenseFileGenEvent> getBlockingQueue() {
        return new ArrayBlockingQueue<>(capacity);
    }

    /**
     * @return Disruptor Bean
     */
    @Bean(name = "disruptor")
    public Disruptor<LicenseFileGenEvent> getDisruptor() {
        final LicenseFileEventFactory factory = new LicenseFileEventFactory();
        final ThreadFactory threadFactory = Thread::new;
        return new Disruptor<>(factory, buffer, threadFactory, ProducerType.SINGLE, new BlockingWaitStrategy());

    }

    /**
     * @return Event Handler Beans
     */
    @Bean(name = "handlers")
    public LicenseEventHandler[] getEventHandlers() {
        LicenseEventHandler[] handlers = new LicenseEventHandler[Runtime.getRuntime().availableProcessors()];
        for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
            handlers[i] = new LicenseEventHandler(i);
        }
        logger.debug(" Number of LicenseEventHandler are started are : {} ", handlers.length);
        return handlers;
    }

    /**
     * @return Event Producer Bean
     */
    @Bean(name = "producer")
    public LicenseFileEventProducer getProducer() {
        final Disruptor<LicenseFileGenEvent> disruptor = getDisruptor();
        disruptor.handleEventsWith(getEventHandlers());
        disruptor.start();
        logger.debug(" Disruptor started ");
        final RingBuffer<LicenseFileGenEvent> ringBuffer = disruptor.getRingBuffer();
        return new LicenseFileEventProducer(ringBuffer);

    }

}


