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

import com.ericsson.enm.shm.instantaneouslicensing.sslserver.model.batchresult.v2.ActivationMachineInfo;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.model.batchresult.v2.BatchStatusInformation;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.model.batchresult.v2.ErrorList;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.model.batchresult.v2.MachineList;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.model.batchresult.v2.ObjectFactory;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.request.Node;

/**
 * Wrapper builder for batch results xml
 */
public class BatchResultsBuilderV2 {
    private static final Logger logger = LoggerFactory.getLogger(BatchResultsBuilderV2.class);

    private BatchResultsBuilderV2() {
    }

    /**
     * @param requestId    License request id
     * @param successNodes success nodes list
     * @param errorNodes   failure nodes list
     * @param errorList    error list that goes in failed nodes
     * @return XML Element for results file creation
     */
    public static JAXBElement<BatchStatusInformation> build(final String requestId, final List<Node> successNodes, final List<Node> errorNodes,
            final List<ErrorList> errorList) {
        logger.debug("Started the batch result file building for the request id {} ", requestId);
        MachineList machineLists = null;
        MachineList failureMachineLists = null;
        if (!successNodes.isEmpty()) {
            machineLists =
                    successNodes.stream()
                            .map(
                                    node ->
                                            ActivationMachineInfo.activationMachineInfoBuilder()
                                                    .withMachineName(node.getFingerprint())
                                                    .withErrorDetails(errorList)
                                                    .build())
                            .collect(
                                    collectingAndThen(
                                            toList(),
                                            actList ->
                                                    MachineList.machineListBuilder()
                                                            .withActivationMachineInfo(actList)
                                                            .build()));
        }
        if (!errorNodes.isEmpty()) {
            failureMachineLists =
                    errorNodes.stream()
                            .map(
                                    node ->
                                            ActivationMachineInfo.activationMachineInfoBuilder()
                                                    .withMachineName(node.getFingerprint())
                                                    .withErrorDetails(errorList)
                                                    .build())
                            .collect(
                                    collectingAndThen(
                                            toList(),
                                            actList ->
                                                    MachineList.machineListBuilder()
                                                            .withActivationMachineInfo(actList)
                                                            .build()));
        }

        final BatchStatusInformation batchStatusInformation =
                BatchStatusInformation.batchStatusInformationBuilder()
                        .withRequestId(requestId)
                        .withSuccessMachines(machineLists)
                        .withErrorMachines(failureMachineLists)
                        .build();
        logger.debug("Completed the batch result file building for the request id {} ", requestId);
        return new ObjectFactory().createBatchStatusInformation(batchStatusInformation);
    }
}
