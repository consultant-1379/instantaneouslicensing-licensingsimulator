/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2020
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/

package com.ericsson.enm.shm.instantaneouslicensing.sslserver.response.builder;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import java.util.List;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.enm.shm.instantaneouslicensing.sslserver.model.batchresult.v1.ActivationMachineInfo;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.model.batchresult.v1.BatchStatusInformation;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.model.batchresult.v1.MachineList;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.model.batchresult.v1.ObjectFactory;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.request.Node;

/**
 * Wrapper builder for batch results xml
 */
public class BatchResultsBuilderV1 {
    private static final Logger logger = LoggerFactory.getLogger(BatchResultsBuilderV1.class);

    private BatchResultsBuilderV1() {
    }

    /**
     * @param requestId License request id
     * @param nodes     Nodes
     * @return XML Element for results file creation
     */
    public static JAXBElement<BatchStatusInformation> build(String requestId, List<Node> nodes) {
        logger.debug("Started the batch result file building for the request id {} ", requestId);
        MachineList machineLists =
                nodes.stream()
                        .map(
                                node ->
                                        ActivationMachineInfo.activationMachineInfoBuilder()
                                                .withMachineName(node.getFingerprint())
                                                .build())
                        .collect(
                                collectingAndThen(
                                        toList(),
                                        actList ->
                                                MachineList.machineListBuilder()
                                                        .withActivationMachineInfo(actList)
                                                        .build()));
        BatchStatusInformation batchStatusInformation =
                BatchStatusInformation.batchStatusInformationBuilder()
                        .withRequestId(requestId)
                        .withSuccessMachines(machineLists)
                        .withErrorMachines(MachineList.machineListBuilder().build())
                        .build();
        logger.debug("Completed the batch result file building for the request id {} ", requestId);
        return new ObjectFactory().createBatchStatusInformation(batchStatusInformation);
    }
}